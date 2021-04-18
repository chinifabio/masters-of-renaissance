package it.polimi.ingsw.model.match.match;

import it.polimi.ingsw.model.exceptions.PlayerStateException;
import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;
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
    private int curPlayer;

    /**
     * an array containing all the player
     */
    private final List<Player> playerOrder;

    /**
     * the player with the inkwell start the match
     */
    private int inkwellPlayer;

    /**
     * create a turn instance and initialize the playerOrder array
     */
    public Turn() {
        curPlayer = 0;
        this.playerOrder = new ArrayList<>();
    }

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
        List<Player> ret = new ArrayList<>();
        ret.addAll(playerOrder);
        ret.remove(curPlayer);
        return ret;
    }

    /**
     * return the first player of the match
     * @return first player
     */
    public Player getInkwellPlayer() {
        Random rand = new Random();
        inkwellPlayer = rand.nextInt(playerOrder.size());
        curPlayer = inkwellPlayer;
        return playerOrder.get(inkwellPlayer);
    }

    /**
     * set the new current player and return its instance
     * @return succeed of the operation
     */
    public boolean nextPlayer() throws PlayerStateException {
        if((curPlayer + 1) > (playerOrder.size() - 1)) curPlayer = 0;
        else curPlayer++;
        return playerOrder.get(curPlayer).startHisTurn();
    }

    /**
     * add a new player to the player
     * @param player the player to add to the match
     * @return true if success, otherwise return false
     */
    public boolean joinPlayer(Player player) {
        if(this.playerOrder.contains(player)) return false;
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
}
