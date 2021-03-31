package it.polimi.ingsw.model.match.MarkerMarble;

import it.polimi.ingsw.model.player.PlayerModifier;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.Servant;

/**
 * represent the purple marble that can be obtained using the marketTray
 */
public class Purple implements Marble{
    /**
     * return the color of the marble
     *
     * @return marble color
     */
    @Override
    public MarbleColor type() {
        return MarbleColor.PURPLE;
    }

    /**
     * recive the player and request its marble convert function to convert itself into resourceLoot and give it to the player
     *
     * @param player current player in the turn
     */
    @Override
    public void toPlayer(PlayerModifier player) {
        Resource res = new Servant(1);
        player.obtainResource(res);
    }
}
