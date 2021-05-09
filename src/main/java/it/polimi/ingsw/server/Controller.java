package it.polimi.ingsw.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.communication.VirtualSocket;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.Command;
import it.polimi.ingsw.model.Model;

public class Controller implements Runnable {

    public final Server server;
    public final VirtualSocket socket;

    protected Model model;

    private ControllerState state = new InitState();

    protected String nickname = "undefined";

    public Controller(VirtualSocket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    protected void setState(ControllerState newState) {
        this.state = newState;
    }

    public Packet invalid(String message) {
        return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, message);
    }

    /**
     * Handle client messages
     */
    @Override
    public void run() {
        while (true) {
            // wait for a message to handle
            Packet received = socket.pollPacketFrom(ChannelTypes.PLAYER_ACTIONS);
            System.out.println(TextColors.colorText(TextColors.CYAN, nickname) + ": " + received);

            // handle the message and send back the response
            socket.send(state.handleMessage(received, this));
        }
    }
}

interface ControllerState {
    /**
     * handle the client message and create a response Packet
     * @param packet the message to handle
     * @param context the context of the state
     * @return the response message
     */
    Packet handleMessage(Packet packet, Controller context);
}

class InitState implements ControllerState {
    /**
     * handle the client message and create a response Packet
     *
     * @param packet the message to handle
     * @return the response message
     */
    @Override
    public Packet handleMessage(Packet packet, Controller context) {
        if (packet.header != HeaderTypes.HELLO)
            return context.invalid("invalid packet: " + packet.header + "; expected " + HeaderTypes.HELLO);

        if (!context.server.memorizePlayer(packet.body))
            return context.invalid("pls change nickname");

        context.nickname = packet.body;

        context.model = context.server.obtainModel();

        context.setState(context.model.initialized() ?
                new LobbyState():
                new CreatorState());

        return context.model.login(context, context.nickname);
    }
}

class LobbyState implements ControllerState {
    /**
     * handle the client message and create a response Packet
     *
     * @param packet  the message to handle
     * @param context the context of the state
     * @return the response message
     */
    @Override
    public Packet handleMessage(Packet packet, Controller context) {
        if (packet.header != HeaderTypes.WAIT_FOR_START)
            return context.invalid("invalid packet: " + packet.header + "; expected " + HeaderTypes.WAIT_FOR_START);

        System.out.println(context.nickname + " ready for start");
        context.setState(new InGameState());
        return context.model.readyForStart(context);
    }
}

class CreatorState implements ControllerState {
    /**
     * handle the client message and create a response Packet
     *
     * @param packet  the message to handle
     * @param context the context of the state
     * @return the response message
     */
    @Override
    public Packet handleMessage(Packet packet, Controller context) {
        if (packet.header != HeaderTypes.SET_PLAYERS_NUMBER)
            return context.invalid("invalid packet: " + packet.header + "; expected " + HeaderTypes.SET_PLAYERS_NUMBER);

        try {
            context.model.createMatch(Integer.parseInt(packet.body));
            context.setState(new LobbyState());
            return context.model.login(context, context.nickname);
        } catch (NumberFormatException e) {
            return context.invalid("invalid number format: " + packet.body);
        }
    }
}

class InGameState implements ControllerState {

    /**
     * handle the client message and create a response Packet
     *
     * @param packet  the message to handle
     * @param context the context of the state
     * @return the response message
     */
    @Override
    public Packet handleMessage(Packet packet, Controller context) {
        if (packet.header != HeaderTypes.DO_ACTION)
            return context.invalid("invalid packet: " + packet.header + "; expected " + HeaderTypes.DO_ACTION);

        try {
            return context.model.handleClientCommand(
                    context,
                    new ObjectMapper().readerFor(Command.class).readValue(packet.body)
            );
        } catch (JsonProcessingException e) {
            return context.invalid(e.getMessage());
        }
    }
}


