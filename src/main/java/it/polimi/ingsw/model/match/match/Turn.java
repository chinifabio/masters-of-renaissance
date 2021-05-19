package it.polimi.ingsw.model.match.match;

import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.util.Pair;
import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

// TODO add player once the match is started it's an error

/**
 * This class manage the player order and which one player is the first one
 */
public class Turn {
    /**
     * an index indicating which one is the current player in the playerOrder array
     */
    private int curPlayer = 0;

    /**
     * an array containing all the player
     */
    private final List<Player> playerOrder = new ArrayList<>();

    /**
     * This attribute indicate if it need to be applied the end game logic
     */
    private boolean endGameLogic = false;

    /**
     * return the current player
     * @return the current player
     */
    public Player getCurPlayer(){
        return playerOrder.get(curPlayer);
    }

    /**
     * return a list of the non current player
     * @return list of other player
     */
    public List<Player> getOtherPlayer() {
        List<Player> ret = new ArrayList<>(playerOrder);
        ret.remove(curPlayer);
        return ret;
    }

    /**
     * randomize the order of player to set as index 0 the inkwell player
     * and set the initial setup for all player
     */
    public void randomizeInkwellPlayer() {
        Random rand = new Random();
        Collections.rotate(playerOrder, rand.nextInt(playerOrder.size()));

        List<Pair<Integer>> initialResourcesSetup = new ArrayList<>();

        initialResourcesSetup.add(new Pair<>(0, 0));
        initialResourcesSetup.add(new Pair<>(1, 0));
        initialResourcesSetup.add(new Pair<>(1, 1));
        initialResourcesSetup.add(new Pair<>(2, 1));

        for (int i = 0; i < playerOrder.size(); i++) {
            playerOrder.get(i).initialSetup = initialResourcesSetup.get(i);
        }

        this.playerOrder.forEach(Player::startHisTurn);
    }

    /**
     * set the new current player and return its instance
     * @return succeed of the operation
     */
    public void nextPlayer() throws EndGameException {
        // if the cur player is the right player of inkwell player the match ends
        if (endGameLogic && curPlayer == (playerOrder.size() - 1)) throw new EndGameException();

        curPlayer = (curPlayer + 1) % playerOrder.size();
        playerOrder.get(curPlayer).startHisTurn();
    }

    /**
     * add a new player to the player
     * @param player the player to add to the match
     * @return true if success, otherwise return false
     */
    public boolean joinPlayer(Player player) {
        if(this.playerOrder.contains(player) || playerOrder.size() > 4) return false;
        playerOrder.add(player);
        return true;
    }

    /**
     * This method returns the number of player in the game
     * @return the number of player
     */
    public int playerInGame() {
        return playerOrder.size();
    }

    /**
     * This method is used to set the end game logic to true
     */
    public void endGame() {
        this.endGameLogic = true;
    }

    /**
     * this method set the state af all player as counting points
     */
    public void countingPoints() {
        this.playerOrder.forEach(Player::setCountingPoints);
    }

    //Only for testing of warehouseUpdater
    public List<Player> getPlayerOrder() {
        return playerOrder;
    }
}
