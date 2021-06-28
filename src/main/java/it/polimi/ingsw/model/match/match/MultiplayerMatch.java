package it.polimi.ingsw.model.match.match;

import it.polimi.ingsw.litemodel.Scoreboard;
import it.polimi.ingsw.model.Pair;
import it.polimi.ingsw.model.VirtualView;
import it.polimi.ingsw.model.player.CountingPointsPlayerState;
import it.polimi.ingsw.model.player.NotHisTurnPlayerState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.VaticanSpace;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class represents the MultiplayerMatch
 */
public class MultiplayerMatch extends Match{

    /**
     * List of all player connected to the match
     */
    private final List<Player> connectedPlayers = new ArrayList<>();

    /**
     * the current player of the game
     */
    private int curPlayer = 0;

    /**
     * flag that activate the end of the game end turn logic.
     * So if it is true and the lefter player of the inkwell player end the turn the match will ends
     */
    private boolean endGameLogic = false;

    /**
     * Number of players who finished the initial selection of resources
     */
    private int initializedPlayers = 0;

    /**
     * build a multiplayer match.
     * A multiplayer match has a game size of 4 players
     * and require at least 2 player to be started
     * @param number the game size of the match
     */
    public MultiplayerMatch(int number, VirtualView view) throws IOException {
        super(number, view);
    }

    /**
     * add a new player to the game
     *
     * @param joined player who join
     * @return true if success, false instead
     */
    @Override
    public boolean playerJoin(Player joined) {
        if (connectedPlayers.size() >= gameSize || connectedPlayers.contains(joined)) return false;
        return connectedPlayers.add(joined);
    }

    /**
     * Randomize the inkwell player and give the initial resources
     */
    @Override
    public void initialize() {
        Random rand = new Random();
        Collections.rotate(connectedPlayers, rand.nextInt(connectedPlayers.size()));

        // save player order into the lite model
        view.publish(model -> model.setPlayerOrder(connectedPlayers.stream().map(Player::getNickname).collect(Collectors.toList())));

        List<Pair<Integer>> initialResourcesSetup = new ArrayList<>();

        initialResourcesSetup.add(new Pair<>(0, 0));
        initialResourcesSetup.add(new Pair<>(1, 0));
        initialResourcesSetup.add(new Pair<>(1, 1));
        initialResourcesSetup.add(new Pair<>(2, 1));

        for (int i = 0; i < connectedPlayers.size(); i++) {
            connectedPlayers.get(i).setInitialSetup(initialResourcesSetup.get(i));
        }

        connectedPlayers.forEach(Player::startHisTurn); // set the state of all player as InitSelectionState
        gameOnAir = true;
    }

    /**
     * request to other player to flip the pope tile passed in the parameter
     *
     * @param toCheck the vatican space to check
     */
    @Override
    public void vaticanReport(VaticanSpace toCheck) {
        if(checkedPopeTile.get(toCheck)) return; // already checked tile
        connectedPlayers.forEach(p -> p.flipPopeTile(toCheck));
        checkedPopeTile.put(toCheck, true);
    }

    /**
     * Method called when player do action such that other players obtain faith point
     *
     * @param amount faith point given to other player
     */
    @Override
    public void othersPlayersObtainFaithPoint(int amount) {
        new ArrayList<>(connectedPlayers)
                .stream()
                .filter(p->!p.equals(connectedPlayers.get(curPlayer)))
                .forEach(p-> p.moveFaithMarker(amount));
    }

    /**
     * Tells to the match the end of the player turn;
     */
    @Override
    public void turnDone() {
        this.marketTray.unPaint();
        updateTray();

        // if the cur player is the right player of inkwell player the match ends
        if (endGameLogic) {
            if (curPlayer == (gameSize - 1)) {
                gameOnAir = false;
                if (model != null) model.matchEnded();
                return;
            }

            currentPlayer().setState(new CountingPointsPlayerState(currentPlayer()));
            if (model != null) model.playerEndGame();
        }

        curPlayer = (curPlayer + 1) % connectedPlayers.size();
        currentPlayer().startHisTurn();
        view.sendMessage("The turn of " + currentPlayer().getNickname() + " is started!");
    }

    /**
     * Tells to the match that a player has done the init phase
     */
    @Override
    public void initialSelectionDone() {
        initializedPlayers++;
        if (initializedPlayers == gameSize) {
            connectedPlayers.forEach(player -> player.setState(new NotHisTurnPlayerState(player)));
            connectedPlayers.get(curPlayer).startHisTurn();
            view.sendMessage("All players passed game initialization");
            view.sendMessage("Is the turn of " + currentPlayer());
            if (model != null) model.gameSetupDone();
        }
    }

    /**
     * This method starts the end game logic
     */
    @Override
    public void startEndGameLogic() {
        if (model != null) model.playerEndGame();
        view.sendMessage("End game logic started!");
        endGameLogic = true;
        turnDone();
    }

    /**
     * Return the number of player in the game
     *
     * @return the number of player
     */
    @Override
    public int playerInGame() {
        return connectedPlayers.size();
    }

    /**
     * disconnect a player from the match
     *
     * @param player the disconnected player
     */
    @Override
    public boolean disconnectPlayer(Player player) {
        view.sendMessage(player.getNickname() + " disconnected from the game");
        if (gameOnAir) {
            player.disconnect();
            return true;
        }

        connectedPlayers.remove(player);
        return false;
    }

    /**
     * reconnect a player to the game
     *
     * @param nickname the nickname of the player who need to be reconnected
     * @return the reconnected player
     */
    @Override
    public Player reconnectPlayer(String nickname) {
        Player reconnecting = connectedPlayers.stream().filter(player -> player.getNickname().equals(nickname)).findAny().orElse(null);
        assert reconnecting != null;

        reconnecting.reconnect();
        view.sendMessage(nickname + " return in game!");
        return reconnecting;
    }

    /**
     * This method is used to calculate and notify the winner of the match.
     * On each player is call the method to calculate the points obtained and the higher one wins
     */
    @Override
    public Scoreboard winnerCalculator() {
        Scoreboard board = new Scoreboard("Here's the results!");

        for (Player p : connectedPlayers) {
            board.addPlayerScore(p.getNickname(), p.calculateVictoryPoints(), p.resourcesNumber());
        }

        return board;
    }

    /**
     * Return the current player in the game
     *
     * @return current player
     */
    @Override
    public Player currentPlayer() {
        return connectedPlayers.get(curPlayer);
    }
}
