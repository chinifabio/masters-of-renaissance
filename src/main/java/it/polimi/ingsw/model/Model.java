package it.polimi.ingsw.model;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.Command;
import it.polimi.ingsw.model.match.match.Match;
import it.polimi.ingsw.model.match.match.MultiplayerMatch;
import it.polimi.ingsw.model.match.match.SingleplayerMatch;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.server.Controller;
import it.polimi.ingsw.view.cli.Colors;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The model class, contains all the data and logic of the game
 */
public class Model {
    /**
     * The instance of the game in the model
     */
    private Match match;

    /**
     * This attribute map all the controller to his player in the model
     */
    private final Map<Controller, Player> players = new HashMap<>();

    /**
     * The virtual view used to notify all changes to clients
     */
    public final Dispatcher dispatcher = new Dispatcher();

    /**
     * Queue for waiting the initialization of the match
     */
    private final Object initializingQueue = new Object();

    /**
     * selected game size of the match
     */
    private int gameSize = -1;

    /**
     * initialization flag
     */
    private boolean init = false;

    /**
     * This method execute the command passed on the mapped player of the passed client
     * @param client the player who run the command
     * @param command the command to execute
     * @return the packet containing the result of the command to send back to the client
     */
    public Packet handleClientCommand(Controller client, Command command) {
        return command.execute(this.players.get(client));
    }

    /**
     * Join a player to the match
     * @param client the new client
     * @param nickname the nickname of the controller
     * @return the succeed of the operation
     */
    public Packet login(Controller client, String nickname) {
        if (!init) {
            this.init = true;
            return new Packet(HeaderTypes.SET_PLAYERS_NUMBER, ChannelTypes.PLAYER_ACTIONS, "Empty match, you have to create one.");
        }

        if (this.players.containsKey(client)) return client.invalid("Something strange is going on ༼ つ ◕_◕ ༽つ");

        this.dispatcher.subscribe(nickname, client.socket);

        try {
            Player p = new Player(nickname, this.match, this.dispatcher);
            this.players.put(client, p);
            return this.match.playerJoin(p) ?
                    new Packet(HeaderTypes.GAME_INIT, ChannelTypes.PLAYER_ACTIONS, "You joined a Lobby of " + this.gameSize + " players."):
                    client.invalid("Can't join lobby \\(>_<)/");

        } catch (Exception e) {
            return client.invalid(e.getMessage());
        }
    }

    /**
     * Create a match of size player
     * @param size the new size of the match
     * @throws IOException if the thread can't wait
     */
    public void createMatch(int size) throws IOException {
        this.gameSize = size;

        this.match = size == 1 ?
                new SingleplayerMatch(dispatcher):
                new MultiplayerMatch(size, dispatcher);

        synchronized (this.initializingQueue) {
            this.initializingQueue.notifyAll();
        }
    }

    /**
     * Return the state of initialized flag
     * @return true if the game is set, false if not
     */
    public boolean initialized() {
        return this.init;
    }

    /**
     * Return the available seats of the game -1 if the game does not have a game size.
     * If it has a game size return the available seats
     * @return an int of available seats
     * @throws InterruptedException throw when thread can't wait
     */
    public int availableSeats() throws InterruptedException {
        if (!init) return -1;

        synchronized (this.initializingQueue) {
            while (this.match == null) {
                dispatcher.sendMessage("aspetta che venga inserito il numero di giocatori");
                this.initializingQueue.wait();
            }
        }

        return this.match.gameSize - this.match.playerInGame();
    }

    /**
     * Disconnect the player associated whit the controller in the match
     * @param controller the disconnected controller
     * @return the succeed of the operation
     */
    public boolean disconnectPlayer(Controller controller) {
        return this.match.disconnectPlayer(this.players.get(controller));
    }

    /**
     * Search the player from the given nickname and reconnect it with the passed controller
     * @param nickname nickname of the player to reconnect
     * @param context the controller to reconnect
     * @return the succeed of the operation
     */
    public boolean reconnectPlayer(String nickname, Controller context) {
        this.players.put(context, this.match.reconnectPlayer(nickname));
        this.dispatcher.subscribe(nickname, context.socket);
        System.out.println(Colors.color(Colors.GREEN_BRIGHT, nickname) + " reconnected");
        return true;
    }

    /**
     * Return the packet to send back when a controller reconnect
     * @param context the reconnecting controller
     * @return the packet to send to the user
     */
    public Packet reconnectPacket(Controller context) {
        return this.players.get(context).reconnectPacket();
    }
}