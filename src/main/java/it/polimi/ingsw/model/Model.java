package it.polimi.ingsw.model;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.Command;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
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
public class Model implements Runnable{
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
    public final VirtualView virtualView = new VirtualView();

    /**
     * Queue for waiting the initialization of the match
     */
    private final Object initializingQueue = new Object();

    /**
     * Queue for wait all the player is waiting
     */
    public final Object startGameQueue = new Object();

    /**
     * Queue for detaching model from server to create new model matches
     */
    public final Object detachModel = new Object();

    /**
     * Players ready for start (waiting for start)
     */
    private int readyPlayer = 0;

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
    public synchronized Packet handleClientCommand(Controller client, Command command) {
        return command.execute(this.players.get(client));
    }

    /**
     * Join a player to the match
     * @param client the new client
     * @return the succeed of the operation
     */
    public Packet login(Controller client, String nickname) {
        if (!this.init) {
            this.init = true;
            return new Packet(HeaderTypes.SET_PLAYERS_NUMBER, ChannelTypes.PLAYER_ACTIONS, "You have to create the Lobby!");
        }

        if (this.players.containsKey(client)) return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, "The Client Already Exists");

        try {
            Player p = new Player(nickname, this.match, this.virtualView);

            this.players.put(client, p);        // save the player
            return this.match.playerJoin(p) ?   // add the player to the match
                    new Packet(HeaderTypes.JOIN_LOBBY, ChannelTypes.PLAYER_ACTIONS, "Lobby joined, wait for start..."):
                    new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, "can't join the player");

        } catch (IllegalTypeInProduction e) {
            return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, "Error while creating player, sorry :(");
        }
    }

    public void createMatch(int size) {
        this.gameSize = size;
        this.match = size == 1 ?
                new SingleplayerMatch():
                new MultiplayerMatch(size);

        synchronized (this.initializingQueue) {
            this.initializingQueue.notifyAll();
        }
    }

    public boolean initialized() {
        return this.init;
    }

    public Packet readyForStart(Controller controller) {
        Player p = this.players.get(controller);
        synchronized (p.waitForWakeUp){
            try {
                this.readyPlayer ++;
                synchronized (this.startGameQueue){
                    this.startGameQueue.notifyAll();
                }
                p.waitForWakeUp.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return new Packet(HeaderTypes.START_TURN, ChannelTypes.PLAYER_ACTIONS, "Turn Started");
    }

    /**
     * Wait the notify and check if match can starts, then starts the match
     */
    @Override
    public void run() {
        synchronized (this.startGameQueue) {
            while (this.readyPlayer != this.gameSize) {
                try {
                    System.out.println("model: " + readyPlayer + " " + gameSize);
                    this.startGameQueue.wait();
                } catch (InterruptedException e) {
                    System.out.println("oooops");
                }
            }

            System.out.println("starting game");
            this.match.startGame();
        }

        synchronized (this.detachModel) {
            this.detachModel.notifyAll();
        }
    }

    public int availableSeats() {
        if (!init) return -1;

        if (this.match == null) {
            synchronized (this.initializingQueue) {
                try {
                    this.initializingQueue.wait();
                } catch (InterruptedException e) {
                    System.out.println("model wait error");
                }
            }
        }

        return this.match.gameSize - this.match.playerInGame();
    }
}