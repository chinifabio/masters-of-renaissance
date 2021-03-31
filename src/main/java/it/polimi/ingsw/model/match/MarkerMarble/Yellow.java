package it.polimi.ingsw.model.match.MarkerMarble;

import it.polimi.ingsw.model.player.PlayerModifier;
import it.polimi.ingsw.model.resource.Coin;
import it.polimi.ingsw.model.resource.Resource;

/**
 * represent the yellow marble that can be obtained using the marketTray
 */
public class Yellow implements Marble {
    /**
     * return the color of the marble
     *
     * @return marble color
     */
    @Override
    public MarbleColor type() {
        return MarbleColor.YELLOW;
    }

    /**
     * recive the player and request its marble convert function to convert itself into resourceLoot and give it to the player
     *
     * @param player current player in the turn
     */
    @Override
    public void toPlayer(PlayerModifier player) {
        Resource res = new Coin(1);
        player.obtainResource(res);
    }
}
