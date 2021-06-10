package it.polimi.ingsw.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.communication.Disconnectable;
import it.polimi.ingsw.communication.VirtualSocket;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.Command;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.view.cli.Colors;

import java.io.IOException;

public class Controller implements Runnable, Disconnectable {

    public final Server server;
    public final VirtualSocket socket;

    protected Model model;

    private ControllerState state = new InitState();

    protected String nickname = "anonymous player";

    private boolean disconnected = false;

    public Controller(VirtualSocket socket, Server server) {
        this.socket = socket;
        this.server = server;

        this.socket.ponger(this);
        //SecureConnection.ponger(this);
    }

    void setState(ControllerState newState) {
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
        while (!this.disconnected) {
            // wait for a message to handle
            Packet received = socket.pollPacketFrom(ChannelTypes.PLAYER_ACTIONS);
            System.out.println(Colors.color(Colors.CYAN, nickname) + ": " + received);
            if (received.header == HeaderTypes.TIMEOUT) return;

            // save the result of the operation
            Packet done = state.handleMessage(received, this);

            // wait model of clients has updated lite model and then send the result
            socket.send(new Packet(HeaderTypes.LOCK, ChannelTypes.UPDATE_LITE_MODEL, "waiting ok"));
            if (socket.pollPacketFrom(ChannelTypes.UPDATE_LITE_MODEL).header == HeaderTypes.UNLOCK) socket.send(done);
            else System.out.println(nickname + ": something wrong in the communication...");
        }
    }

    @Override
    public void handleDisconnection() {
        state.handleDisconnection(this);
        System.out.println(Colors.color(Colors.RED, nickname) + " disconnected");
        disconnected = true;
    }

    @Override
    public VirtualSocket disconnectableSocket() {
        return this.socket;
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

    /**
     * Handle the disconnection of the controller based on his state
     * @param context the state of the controller
     */
    void handleDisconnection(Controller context);
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

        // the player reconnect to the game whit a legal nickname
        if (context.server.reconnect(packet.body, context)) {
            context.nickname = packet.body;
            context.setState(new InGameState());
            return context.model.reconnectPlayer(context.nickname, context) ?
                    context.model.reconnectPacket(context) :
                    context.invalid("fail in reconnection");
        }

        // the player connect to the server and based on the state of the model in the server he can join it or create one
        if (context.server.connect(packet.body, context)) {
            context.nickname = packet.body;

            if (context.server.isModelAvailable()) {
                Model temp = context.server.obtainModel();
                assert temp != null;
                context.model = temp;

                try {
                    temp.login(context, context.nickname);
                } catch (Exception e) {
                    return context.invalid(e.getMessage());
                }

                context.setState(new InGameState());
                return new Packet(HeaderTypes.GAME_INIT, ChannelTypes.PLAYER_ACTIONS, "You joined a Lobby of " + context.model.gameSize + " players.");
            }

            if (context.server.needACreator()) {
                context.setState(new CreatorState());
                return new Packet(HeaderTypes.SET_PLAYERS_NUMBER, ChannelTypes.PLAYER_ACTIONS, "Empty match, you have to create one.");
            }

            context.server.removeController(context.nickname);
            return context.invalid("Wait! Someone is creating the match...");
        }

        return context.invalid("Please change the nickname, " + packet.body + " is not valid");
    }

    /**
     * Handle the disconnection of the controller based on his state
     *
     * @param context the state of the controller
     */
    @Override
    public void handleDisconnection(Controller context) {
        // do nothing
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
            int n = -1;
            try {
              n = Integer.parseInt(packet.body);
              if(n < 1 || n > 4) return context.invalid(n + " is not a legal game size.");
            } catch (NumberFormatException e){
                return context.invalid(n + " is not a number");
            } catch (NullPointerException e){
                return context.invalid("insert a number");
            }

            context.model = new Model(n);
            context.server.hereSTheModel(context.model);

        } catch (NumberFormatException e) {
            return context.invalid("invalid number format: " + packet.body);
        } catch (IOException e) {
            return context.invalid("fail while creating mach");
        }


        try {
            context.model.login(context, context.nickname);
        } catch (Exception e) {
            return context.invalid(e.getMessage());
        }

        context.setState(new InGameState());
        return new Packet(HeaderTypes.GAME_INIT, ChannelTypes.PLAYER_ACTIONS, "You joined a Lobby of " + context.model.gameSize + " players.");
    }

    /**
     * Handle the disconnection of the controller based on his state
     *
     * @param context the state of the controller
     */
    @Override
    public void handleDisconnection(Controller context) {
        context.server.cannotCreateModel();
        context.server.removeController(context.nickname);
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
            return context.invalid("invalid packet received: " + packet.header + "; expected " + HeaderTypes.DO_ACTION);

        try {
            Command c = new ObjectMapper().readerFor(Command.class).readValue(packet.body);
            return context.model.handleClientCommand(context, c);
        } catch (JsonProcessingException e) {
            return context.invalid(e.getMessage());
        }
    }

    /**
     * Handle the disconnection of the controller based on his state
     *
     * @param context the state of the controller
     */
    @Override
    public void handleDisconnection(Controller context) {
        if (context.model.disconnectPlayer(context))                  // disconnection from model
            context.server.disconnect(context.nickname, context);             // disconnection from server
    }
}


