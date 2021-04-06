package it.polimi.ingsw.model.match.markettray.MarkerMarble;

import it.polimi.ingsw.model.player.PlayerModifier;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;

/**
 * represent the blue marble that can be obtained using the marketTray
 */
public class Blue extends Marble {
    /**
     * return the color of the marble
     *
     * @return marble color
     */
    @Override
    public MarbleColor type() {
        return MarbleColor.BLUE;
    }

    /**
     * recive the player and request its marble convert function to convert itself into resourceLoot and give it to the player
     *
     * @param player current player in the turn
     */
    @Override
    public void toPlayer(PlayerModifier player) {
        Resource res = ResourceBuilder.buildShield();
        player.obtainResource(res);
    }

    /**
     * copy the marble in a new instance
     *
     * @return new instance equals this
     */
    @Override
    public Marble copy() {
        return new Blue();
    }

    /**
     * Returns a string representation of the object
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "BLUE";
    }
}
