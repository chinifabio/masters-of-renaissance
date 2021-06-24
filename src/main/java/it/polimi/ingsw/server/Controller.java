package it.polimi.ingsw.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.communication.SocketListener;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.Command;
import it.polimi.ingsw.communication.packet.rendering.*;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.view.cli.Colors;

import java.io.IOException;
import java.net.Socket;

/**
 * The Controller will receive all the packets sent by the client to the server and than, based on his state, handle it.
 * Controller will send back a response
 */
public class Controller implements Runnable {

    /**
     * A server reference used to obtain or share models
     */
    public final Server server;

    /**
     * A packet container used to store received packets
     */
    public final SocketListener socket;

    /**
     * The model representing the match joined by the client
     */
    Model model;

    /**
     * The nickname of the player. It is always initialized as "anonymous player"
     */
    String nickname = "anonymous player";

    /**
     * The state of the controller
     */
    private ControllerState state = new ChooseNickname();

    /**
     * A flag used to stop the controller
     */
    private boolean disconnected = false;

    /**
     * Create a contrller from server and socket
     * @param socket the socket used for the communication
     * @param server the server
     * @throws IOException when creator is unable to create a socketListener
     */
    public Controller(Socket socket, Server server) throws IOException {
        this.socket = new SocketListener(socket);
        this.server = server;

        this.server.executeRunnable(this.socket);
    }

    /**
     * Update the state of the controller and update the view of the client
     * @param newState the new state of the controller
     */
    public void setState(ControllerState newState) {
        state = newState;
        socket.send(new Packet(HeaderTypes.OK, ChannelTypes.RENDER_CANNON, state.renderCannonAmmo().jsonfy()));
    }

    /**
     * Create and invalid packet for the player action channel
     * @param message the message to handle
     * @return the created packet
     */
    public Packet invalid(String message) {
        return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, message);
    }

    /**
     * Set the state of the controller as game initialization
     */
    public void gameInit() {
        setState(new GameInit());
    }

    /**
     * Set the state of the controller as game started
     */
    public void gameStart() {
        setState(new InGameState());
    }

    /**
     * Set the state of the controller as game ended
     */
    public void gameEnd() {
        setState(new WaitingResult());
    }

    /**
     * Set the state of the controller as game result
     */
    public void gameScoreboard() {
        server.cleanNickname(nickname);
        setState(new GameScoreboard());
    }

    /**
     * update the view of the client once he has reconnected
     */
    public void fireReconnection() {
        socket.send(new Packet(HeaderTypes.OK, ChannelTypes.RENDER_CANNON, state.renderCannonAmmo().jsonfy()));
    }

    /**
     * copy the information from an old controller
     * @param remove the old controller
     */
    public void transferStatus(Controller remove) {
        model = remove.model;
        state = remove.state;
    }

    /**
     * Receive packet and handle the behavior of it
     */
    @Override
    public void run() {
        while (!disconnected) {
            // wait for a message to handle
            Packet received = socket.pollPacket();
            switch (received.channel) {
                case PLAYER_ACTIONS -> {
                    System.out.println(Colors.color(Colors.CYAN, nickname) + ": " + received);

                    // save the result of the operation
                    socket.send(state.handleMessage(received, this));
                }

                case MESSENGER -> {
                    // model.sendChat()
                }

                case CONNECTION_STATUS -> {
                    if (received.header == HeaderTypes.TIMEOUT) {
                        state.handleDisconnection(this);
                        System.out.println(Colors.color(Colors.RED, nickname) + " disconnected");
                        disconnected = true;
                    }
                }
            }
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

    /**
     * Handle the disconnection of the controller based on his state
     * @param context the state of the controller
     */
    void handleDisconnection(Controller context);

    /**
     * Return a scene to render in the view of the client
     * @return a packet that fire the render of a scene
     */
    Lighter renderCannonAmmo();
}

class ChooseNickname implements ControllerState {
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

        // check if the player where disconnected
        if (context.server.reconnect(packet.body, context)) {
            context.nickname = packet.body;
            return context.model.reconnectPlayer(context.nickname, context) ?
                    new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "reconnect") :
                    context.invalid("fail in reconnection");
        }

        // check if the player chose a valid nickname
        if (context.server.connect(packet.body, context)) {
            context.nickname = packet.body;

            if (context.server.isModelAvailable()) {
                Model temp = context.server.obtainModel();
                assert temp != null;
                context.model = temp;

                context.setState(new WaitingInLobby());
                try {
                    temp.login(context, context.nickname);
                } catch (Exception e) {
                    return context.invalid(e.getMessage());
                }

                return new Packet(HeaderTypes.GAME_INIT, ChannelTypes.PLAYER_ACTIONS, "You joined a Lobby of " + context.model.gameSize + " players.");
            }

            if (context.server.needACreator()) {
                context.setState(new ChoosePlayerNumber());
                return new Packet(HeaderTypes.SET_PLAYERS_NUMBER, ChannelTypes.PLAYER_ACTIONS, "Empty match, you have to create one.");
            }

            context.server.cleanNickname(context.nickname);
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

    /**
     * Return a scene to render in the view of the client
     *
     * @return a packet that fire the render of a scene
     */
    @Override
    public Lighter renderCannonAmmo() {
        return null; // client always start with this render
    }
}

class ChoosePlayerNumber implements ControllerState {
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


        context.setState(new WaitingInLobby());
        try {
            context.model.login(context, context.nickname);
        } catch (Exception e) {
            return context.invalid(e.getMessage());
        }

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
        context.server.cleanNickname(context.nickname);
    }

    /**
     * Return a scene to render in the view of the client
     *
     * @return a packet that fire the render of a scene
     */
    @Override
    public Lighter renderCannonAmmo() {
        return new FireGameCreator();
    }
}

class WaitingInLobby implements ControllerState {
    /**
     * handle the client message and create a response Packet
     *
     * @param packet  the message to handle
     * @param context the context of the state
     * @return the response message
     */
    @Override
    public Packet handleMessage(Packet packet, Controller context) {
        return context.invalid("Wait that other player join!");
    }

    /**
     * Handle the disconnection of the controller based on his state
     *
     * @param context the state of the controller
     */
    @Override
    public void handleDisconnection(Controller context) {
        if (context.model.disconnectPlayer(context)) context.server.disconnect(context.nickname, context);
        else context.server.cleanNickname(context.nickname);
    }

    /**
     * Return a scene to render in the view of the client
     *
     * @return a packet that fire the render of a scene
     */
    @Override
    public Lighter renderCannonAmmo() {
        return new FireLobbyWait();
    }
}

class GameInit implements ControllerState {
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
        if (context.model.disconnectPlayer(context)) context.server.disconnect(context.nickname, context);
        else context.server.cleanNickname(context.nickname);
    }

    /**
     * Return a scene to render in the view of the client
     *
     * @return a packet that fire the render of a scene
     */
    @Override
    public Lighter renderCannonAmmo() {
        return new FireGameInit();
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
        if (context.model.disconnectPlayer(context)) context.server.disconnect(context.nickname, context);
        else context.server.cleanNickname(context.nickname);
    }

    /**
     * Return a scene to render in the view of the client
     * @return a packet that fire the render of a scene
     */
    @Override
    public Lighter renderCannonAmmo() {
        return new FireGameSession();
    }
}

class WaitingResult implements ControllerState {
    /**
     * handle the client message and create a response Packet
     *
     * @param packet  the message to handle
     * @param context the context of the state
     * @return the response message
     */
    @Override
    public Packet handleMessage(Packet packet, Controller context) {
        return context.invalid("The match is still running, wait that all the players end their turn!");
    }

    /**
     * Handle the disconnection of the controller based on his state
     *
     * @param context the state of the controller
     */
    @Override
    public void handleDisconnection(Controller context) {
        if (context.model.disconnectPlayer(context)) context.server.disconnect(context.nickname, context);
        else context.server.cleanNickname(context.nickname);
    }

    /**
     * Return a scene to render in the view of the client
     *
     * @return a packet that fire the render of a scene
     */
    @Override
    public Lighter renderCannonAmmo() {
        return new FireGameEnded();
    }
}

class GameScoreboard implements ControllerState {
    /**
     * handle the client message and create a response Packet
     *
     * @param packet  the message to handle
     * @param context the context of the state
     * @return the response message
     */
    @Override
    public Packet handleMessage(Packet packet, Controller context) {
        return context.invalid("The match is ended!");
    }

    /**
     * Handle the disconnection of the controller based on his state
     *
     * @param context the state of the controller
     */
    @Override
    public void handleDisconnection(Controller context) {
        context.server.cleanNickname(context.nickname);
    }

    /**
     * Return a scene to render in the view of the client
     *
     * @return a packet that fire the render of a scene
     */
    @Override
    public Lighter renderCannonAmmo() {
        return new FireGameResult();
    }
}