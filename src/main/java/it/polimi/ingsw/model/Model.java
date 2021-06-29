package it.polimi.ingsw.model;

import it.polimi.ingsw.communication.packet.commands.Command;
import it.polimi.ingsw.model.match.match.Match;
import it.polimi.ingsw.model.match.match.MultiplayerMatch;
import it.polimi.ingsw.model.match.match.SingleplayerMatch;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.server.Controller;
import it.polimi.ingsw.server.Server;
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
    public final VirtualView virtualView = new VirtualView();

    /**
     * selected game size of the match
     */
    public final int gameSize;

    /**
     * A reference to the server used for clean nicknames
     */
    private final Server server;

    public Model(int size, Server server) throws IOException {
        gameSize = size;
        this.server = server;

        match = size == 1 ?
                new SingleplayerMatch(virtualView):
                new MultiplayerMatch(size, virtualView);

        match.setModel(this);
    }

    /**
     * This method execute the command passed on the mapped player of the passed client
     * @param client the player who run the command
     * @param command the command to execute
     */
    public synchronized void handleClientCommand(Controller client, Command command) {
        command.execute(players.get(client));
    }

    /**
     * Join a player to the match
     * @param client the new client
     * @param nickname the nickname of the controller
     */
    public synchronized void login(Controller client, String nickname) throws Exception {
        if (players.containsKey(client)) throw new Exception("Something strange is going on ༼ つ ◕_◕ ༽つ");

        Player p = new Player(nickname, match, virtualView);
        if (!match.playerJoin(p)) throw new Exception("Something strange is going on ༼ つ ◕_◕ ༽つ");

        virtualView.sendMessage(nickname + " joined the lobby!");
        virtualView.subscribe(nickname, client.socket);
        players.put(client, p);

        virtualView.sendPlayerMessage(nickname, gameSize == 1 ?
                "You joined a singleplayer match" :
                "You joined a match of " + gameSize + " players");
    }

    /**
     * Check if the connected player are equals to the game size. If true then start the game
     */
    public synchronized void checkStart() {
        if (match.playerInGame() == match.gameSize) {
            match.initialize();
            virtualView.updateClients();
            players.forEach((controller, player) -> controller.gameInit());
        }
    }

    /**
     * Return the number of available seats
     * @return the number of available seats
     */
    public synchronized int availableSeat() {
        return gameSize - match.playerInGame();
    }

    /**
     * Set all the client render as in game so they can use market, productions ecc
     */
    public synchronized void gameSetupDone() {
        players.forEach((controller, player) -> controller.gameStart());
    }

    /**
     * The current player end the game
     */
    public synchronized void playerEndGame() {
        players
                .entrySet()
                .stream()
                .filter(e -> e.getValue().equals(match.currentPlayer()))
                .forEach(e -> e.getKey().gameEnd());
    }

    /**
     * Send the scoreboard to all the player and than fire the render of it
     */
    public synchronized void matchEnded() {
        virtualView.publish(model -> model.setScoreboard(match.winnerCalculator()));
        virtualView.updateClients();
        players.forEach((controller, player) -> controller.gameScoreboard());
    }

    /**
     * Disconnect the player associated whit the controller in the match
     * @param controller the disconnected controller
     * @return the succeed of the operation
     */
    public synchronized boolean disconnectPlayer(Controller controller) {
        virtualView.unsubscribe(players.get(controller).getNickname());
        return match.disconnectPlayer(players.get(controller));
    }

    /**
     * Search the player from the given nickname and reconnect it with the passed controller
     * @param nickname nickname of the player to reconnect
     * @param context the controller to reconnect
     * @return the succeed of the operation
     */
    public synchronized boolean reconnectPlayer(String nickname, Controller context) {
        players.put(context, match.reconnectPlayer(nickname));
        virtualView.subscribe(nickname, context.socket);
        server.print(Colors.color(Colors.GREEN_BRIGHT, nickname) + " reconnected");
        context.fireReconnection();
        return true;
    }

    /**
     * Remove all the saved players and controller and clean nicknames from the server
     */
    public void closeMatch() {
        for (String nickname : match.nicknames()) server.cleanNickname(nickname);
        server.print(Colors.color(Colors.RED_BRIGHT, "A match of " + gameSize + " player were closed"));
    }
}