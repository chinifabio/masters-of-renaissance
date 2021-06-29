package it.polimi.ingsw.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.communication.SocketListener;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.clientexecutable.ClientExecutable;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.clientexecutable.*;
import it.polimi.ingsw.communication.packet.commands.Command;
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
     * Create a controller from server and socket
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
     * Send an error message to the client
     * @param message the message to send
     */
    public void invalid(String message) {
        socket.send(new Packet(HeaderTypes.INVALID, ChannelTypes.MESSENGER, new PlayerError(message).jsonfy()));
    }

    /**
     * Send a message to the client
     * @param message the message to send
     */
    public void sendMessage(String message) {
        socket.send(new Packet(HeaderTypes.NOTIFY, ChannelTypes.MESSENGER, new PlayerMessage(message).jsonfy()));
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
                    server.print(Colors.color(Colors.CYAN, nickname) + ": " + received);
                    state.handleMessage(received, this);
                }

                case CONNECTION_STATUS -> {
                    if (received.header == HeaderTypes.TIMEOUT) {
                        state.handleDisconnection(this);
                        server.print(Colors.color(Colors.RED, nickname) + " disconnected");
                        disconnected = true;
                    }
                }
            }
        }
    }
}

/**
 * This class manages the Controller States
 */
interface ControllerState {
    /**
     * handle the client message and create a response Packet
     * @param packet the message to handle
     * @param context the context of the state
     */
    void handleMessage(Packet packet, Controller context);

    /**
     * Handle the disconnection of the controller based on his state
     * @param context the state of the controller
     */
    void handleDisconnection(Controller context);

    /**
     * Return a scene to render in the view of the client
     * @return a packet that fire the render of a scene
     */
    ClientExecutable renderCannonAmmo();
}

/**
 * This class is the State of the Controller where the Player choose his nickname
 */
class ChooseNickname implements ControllerState {
    /**
     * handle the client message and create a response Packet
     *
     * @param packet the message to handle
     * @param context the context of the state
     */
    @Override
    public void handleMessage(Packet packet, Controller context) {
        if (packet.header != HeaderTypes.HELLO) {
            context.invalid("invalid packet: " + packet.header + "; expected " + HeaderTypes.HELLO);
            return;
        }

        // check if the player where disconnected
        if (context.server.reconnect(packet.body, context)) {
            context.nickname = packet.body;
            if (context.model.reconnectPlayer(context.nickname, context)) context.sendMessage("You were reconnect");
            else context.invalid("Fail in reconnection");
            return;
        }

        // check if the player chose a valid nickname
        if (context.server.connect(packet.body, context)) {
            context.nickname = packet.body;

            if (context.server.isModelAvailable()) {
                Model temp = context.server.obtainModel();
                assert temp != null;
                context.model = temp;

                try {
                    temp.login(context, context.nickname);
                    context.setState(new WaitingInLobby());
                    temp.checkStart();
                } catch (Exception e) {
                    context.invalid(e.getMessage());
                    return;
                }

                return;
            }

            if (context.server.needACreator()) {
                context.sendMessage("Empty match, you have to create one.");
                context.setState(new ChoosePlayerNumber());
                return;
            }

            context.server.cleanNickname(context.nickname);
            context.invalid("Wait! Someone is creating the match...");
            return;
        }

        context.invalid("Please change the nickname, " + packet.body + " is not valid");
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
    public ClientExecutable renderCannonAmmo() {
        return null; // client always start with this render
    }
}

/**
 * This class is the State of the Controller where the Player choose the number of the players in the match
 */
class ChoosePlayerNumber implements ControllerState {
    /**
     * handle the client message and create a response Packet
     *
     * @param packet  the message to handle
     * @param context the context of the state
     */
    @Override
    public void
    handleMessage(Packet packet, Controller context) {
        try {
            int n = -1;
            try {
              n = Integer.parseInt(packet.body);
              if(n < 1 || n > 4) {
                  context.invalid(n + " is not a legal game size.");
                  return;
              }
            } catch (NumberFormatException e){
                context.invalid(n + " is not a number");
                return;
            } catch (NullPointerException e){
                context.invalid("insert a number");
                return;
            }

            context.model = new Model(n, context.server);
            context.server.hereSTheModel(context.model);

        } catch (NumberFormatException e) {
            context.invalid("invalid number format: " + packet.body);
            return;
        } catch (IOException e) {
            context.invalid("fail while creating mach");
            return;
        }

        try {
            context.model.login(context, context.nickname);
            context.setState(new WaitingInLobby());
            context.model.checkStart();
        } catch (Exception e) {
            context.invalid(e.getMessage());
        }
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
    public ClientExecutable renderCannonAmmo() {
        return new FireGameCreator();
    }
}

/**
 * This class is the State of the Controller where the Player waits for other players to connect to the match
 */
class WaitingInLobby implements ControllerState {
    /**
     * handle the client message and create a response Packet
     *
     * @param packet  the message to handle
     * @param context the context of the state
     */
    @Override
    public void handleMessage(Packet packet, Controller context) {
        context.invalid("Wait that other player join!");
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
    public ClientExecutable renderCannonAmmo() {
        return new FireLobbyWait();
    }
}

/**
 * This class is the State of the Controller where the Player choose LeaderCards and Resources at the beginning of the game
 */
class GameInit implements ControllerState {
    /**
     * handle the client message and create a response Packet
     *
     * @param packet  the message to handle
     * @param context the context of the state
     */
    @Override
    public void handleMessage(Packet packet, Controller context) {
        if (packet.header != HeaderTypes.DO_ACTION) {
            context.invalid("invalid packet received: " + packet.header + "; expected " + HeaderTypes.DO_ACTION);
            return;
        }

        try {
            context.model.handleClientCommand(context,
                    new ObjectMapper().readerFor(Command.class).readValue(packet.body));
            context.model.virtualView.updateClients();
        } catch (JsonProcessingException e) {
            context.invalid("Invalid packet received!" + e.getMessage());
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
    public ClientExecutable renderCannonAmmo() {
        return new FireGameInit();
    }
}

/**
 * This class is the State of the Controller where Player can do all the main actions
 */
class InGameState implements ControllerState {

    /**
     * handle the client message and create a response Packet
     *
     * @param packet  the message to handle
     * @param context the context of the state
     */
    @Override
    public void handleMessage(Packet packet, Controller context) {
        if (packet.header != HeaderTypes.DO_ACTION) {
            context.invalid("invalid packet received: " + packet.header + "; expected " + HeaderTypes.DO_ACTION);
            return;
        }

        try {
            context.model.handleClientCommand(context,
                    new ObjectMapper().readerFor(Command.class).readValue(packet.body));
            context.model.virtualView.updateClients();
        } catch (JsonProcessingException e) {
            context.invalid("Invalid packet received!" + e.getMessage());
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
    public ClientExecutable renderCannonAmmo() {
        return new FireGameSession();
    }
}
/**
 * This class is the State of the Controller where the Player waits the end of the game
 */
class WaitingResult implements ControllerState {
    /**
     * handle the client message and create a response Packet
     *
     * @param packet  the message to handle
     * @param context the context of the state
     */
    @Override
    public void handleMessage(Packet packet, Controller context) {
        context.invalid("The match is still running, wait that all the players end their turn!");
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
    public ClientExecutable renderCannonAmmo() {
        return new FireGameEnded();
    }
}

/**
 * This class is the State of the Controller where the Player can see the Leaderboard
 */
class GameScoreboard implements ControllerState {
    /**
     * handle the client message and create a response Packet
     *
     * @param packet  the message to handle
     * @param context the context of the state
     */
    @Override
    public void handleMessage(Packet packet, Controller context) {
        context.invalid("The match is ended!");
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
    public ClientExecutable renderCannonAmmo() {
        return new FireGameResult();
    }
}