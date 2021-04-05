package it.polimi.ingsw.model.match.markettray.MarkerMarble;

import it.polimi.ingsw.model.player.PlayerModifier;
import it.polimi.ingsw.model.resource.builder.Resource;
import it.polimi.ingsw.model.resource.builder.ResourceDirector;

/**
 * represent the gray marble that can be obtained using the marketTray
 */
public class Gray extends Marble{
    /**
     * return the color of the marble
     *
     * @return marble color
     */
    @Override
    public MarbleColor type() {
        return MarbleColor.GRAY;
    }

    /**
     * recive the player and request its marble convert function to convert itself into resourceLoot and give it to the player
     *
     * @param player current player in the turn
     */
    @Override
    public void toPlayer(PlayerModifier player) {
        Resource res = ResourceDirector.buildStone();
        player.obtainResource(res);
    }

    /**
     * copy the marble in a new instance
     *
     * @return new instance equals this
     */
    @Override
    public Marble copy() {
        return new Gray();
    }

    /**
     * Returns a string representation of the object
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "GRAY";
    }
}
