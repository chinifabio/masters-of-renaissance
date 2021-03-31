package it.polimi.ingsw.model.match.MarkerMarble;

import it.polimi.ingsw.model.player.PlayerModifier;

/**
 * interface that contains the method to handle marbles obtained from the marketTray
 */
public interface Marble {
    /**
     * return the color of the marble
     * @return marble color
     */
    MarbleColor type();

    /**
     * recive the player and request its marble convert function to convert itself into resourceLoot and give it to the player
     * @param player current player in the turn
     */
    void toPlayer(PlayerModifier player);
}
