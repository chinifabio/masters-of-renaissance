package it.polimi.ingsw.model;

import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.Command;
import it.polimi.ingsw.communication.server.ClientController;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
import it.polimi.ingsw.model.match.match.Match;
import it.polimi.ingsw.model.match.match.MultiplayerMatch;
import it.polimi.ingsw.model.match.match.SingleplayerMatch;
import it.polimi.ingsw.model.player.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * The model class, contains all the data and logic of the game
 */
public class Model {
    /**
     * The size of the match -> -1 implies that no player set the number
     */
    private int size = -1;

    /**
     * The instance of the game in the model
     */
    private Match match;

    /**
     * This attribute map all the controller to his player in the model
     */
    private final Map<ClientController, Player> map = new HashMap<>();

    /**
     * Save the entry value of the player creator to then ad it when he create the match.
     * When the creator invokes the start method we can't add immediately the Player instance to the match because it doesn't exists
     */
    private Player creatorPlayer = null;

    /**
     * This attribute flag the start of the model, so you can known if the match exists
     */
    private boolean startedFlag = false;

    /**
     * Create a model from a creator player
     * @param creator the creator of the game
     * @throws IllegalTypeInProduction when something goes wrong in the constructor of the creator player
     */
    public void start(ClientController creator) throws IllegalTypeInProduction {
        // creating the player instance
        Player newPlayer;
        try {
            newPlayer = new Player(creator.nickname, true).linkModel(this);
        } catch (IllegalTypeInProduction illegalTypeInProduction) {
            System.out.println("error while creating player");
            return;
        }

        // saving the entry and linking the model
        this.map.put(creator, newPlayer);
        this.creatorPlayer = newPlayer;

        creator.linkModel(this);
    }

    /**
     * This method execute the command passed on the mapped player of the passed client
     * @param client the player who run the command
     * @param command the command to execute
     * @return the packet containing the result of the command to send back to the client
     */
    public synchronized Packet handleClientCommand(ClientController client, Command command) {
        //System.out.println(client.nickname + " " + command);
        return command.execute(this.map.get(client));
    }

    /**
     * Join a player to the match
     * @param client the new client
     * @return the succeed of the operation
     */
    public synchronized boolean connectController(ClientController client) {
        if (this.map.containsKey(client) || !startedFlag) return false;

        Player newPlayer;
        try {
            newPlayer = new Player(client.nickname, false).linkModel(this);
        } catch (IllegalTypeInProduction illegalTypeInProduction) {
            System.out.println("error while creating player");
            return false;
        }
        this.map.put(client, newPlayer);
        client.linkModel(this);

        return this.match.playerJoin(newPlayer);
    }

    /**
     * This method create a match of a passed number of player
     * @param number the number of player of the new match
     */
    public void createMatchOf(int number) {
        this.match = (number == 1) ?
                new SingleplayerMatch():
                new MultiplayerMatch(number);

        this.match.playerJoin(creatorPlayer);

        // flag the start of the model
        this.startedFlag = true;
        this.size = number;
    }

    /**
     * Return the match instance
     * @return the match instance
     */
    public Match getMatch() {
        return this.match;
    }

    /**
     * return the size of the model
     * @return the size. -1 implies that no player set the number
     */
    public int getSize() {
        return size;
    }

    /**
     * Return the available seat in the game. -1 implies that no player set the match size
     * @return the available seat in the game
     */
    public int availableSeats() {
        return this.startedFlag ? size - this.match.playerInGame() : -1;
    }
}
