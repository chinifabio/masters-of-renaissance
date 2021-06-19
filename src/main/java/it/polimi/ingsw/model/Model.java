package it.polimi.ingsw.model;

import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.Command;
import it.polimi.ingsw.communication.packet.updates.ScoreboardUpdater;
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
    public final Match match;

    /**
     * This attribute map all the controller to his player in the model
     */
    private final Map<Controller, Player> players = new HashMap<>();

    /**
     * The virtual view used to notify all changes to clients
     */
    public final Dispatcher dispatcher = new Dispatcher();

    /**
     * selected game size of the match
     */
    public final int gameSize;

    private int playerJoined = 0;

    public Model(int size) throws IOException {
        gameSize = size;

        match = size == 1 ?
                new SingleplayerMatch(dispatcher):
                new MultiplayerMatch(size, dispatcher);

        match.setModel(this);
    }

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
     */
    public void login(Controller client, String nickname) throws Exception {
        if (this.players.containsKey(client)) throw new Exception("Something strange is going on ༼ つ ◕_◕ ༽つ");

        this.dispatcher.subscribe(nickname, client.socket);

        Player p = new Player(nickname, this.match, this.dispatcher);
        this.players.put(client, p);
        if (!this.match.playerJoin(p)) throw new Exception("Something strange is going on ༼ つ ◕_◕ ༽つ");
        playerJoined++;

        if (playerJoined == match.gameSize) {
            System.out.println("sto mandando le munizioni");
            players.forEach((controller, player) -> controller.gameInit());
        }
    }

    /**
     * Return the number of available seats
     * @return the number of available seats
     */
    public int availableSeat() {
        return gameSize - playerJoined;
    }

    public void gameSetupDone() {
        players.forEach((controller, player) -> controller.gameStart());
    }

    public void playerEndGame() {
        players
                .entrySet()
                .stream()
                .filter(e -> e.getValue().equals(match.currentPlayer()))
                .forEach(e -> e.getKey().gameEnd());
    }

    public void matchEnded() {
        dispatcher.publish(new ScoreboardUpdater(match.winnerCalculator()));
        // mandare i risultati
        players.forEach((controller, player) -> controller.gameScoreboard());
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
        context.gameStart();
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