package it.polimi.ingsw.model;

import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.Command;
import it.polimi.ingsw.communication.packet.updates.NewPlayerUpdater;
import it.polimi.ingsw.model.match.match.Match;
import it.polimi.ingsw.model.match.match.MultiplayerMatch;
import it.polimi.ingsw.model.match.match.SingleplayerMatch;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.server.Controller;

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

        if (this.players.containsKey(client)) return client.invalid("something strange is going on ༼ つ ◕_◕ ༽つ");

        this.dispatcher.subscribe(nickname, client.socket);

        try {
            Player p = new Player(nickname, this.match, this.dispatcher);
            this.players.put(client, p);
            return this.match.playerJoin(p) ?
                    new Packet(HeaderTypes.JOIN_LOBBY, ChannelTypes.PLAYER_ACTIONS, "You joined a Lobby of " + this.gameSize + " players."):
                    client.invalid("Can't join lobby \\(>_<)/");

        } catch (Exception e) {
            return client.invalid(e.getMessage());
        }
    }

    public void createMatch(int size) {
        this.gameSize = size;

        this.match = size == 1 ?
                new SingleplayerMatch(dispatcher):
                new MultiplayerMatch(size, dispatcher);

        synchronized (this.initializingQueue) {
            this.initializingQueue.notifyAll();
        }
    }

    public boolean initialized() {
        return this.init;
    }

    public int availableSeats() throws InterruptedException {
        if (!init) return -1;

        synchronized (this.initializingQueue) {
            while (this.match == null) this.initializingQueue.wait();
        }

        return this.match.gameSize - this.match.playerInGame();
    }

    public void disconnectMe(Controller controller) {
        this.players.get(controller).disconnect();
    }

    public boolean reconnect(String nickname, Controller context) {
        Player value = this.players.values().stream().filter(player -> player.getNickname().equals(nickname)).findAny().orElse(null);

        if (value == null) return false;

        this.dispatcher.subscribe(nickname, context.socket);
        System.out.println(TextColors.colorText(TextColors.GREEN_BRIGHT, nickname) + " reconnected");
        value.reconnect();
        this.players.put(context, value);
        return true;
    }
}