package it.polimi.ingsw.model.match.markettray.MarkerMarble;

import it.polimi.ingsw.model.player.PlayerModifier;

/**
 * interface that contains the method to handle marbles obtained from the marketTray
 */
public abstract class Marble {
    /**
     * return the color of the marble
     * @return marble color
     */
    public abstract MarbleColor type();

    /**
     * recive the player and request its marble convert function to convert itself into resourceLoot and give it to the player
     * @param player current player in the turn
     */
    public abstract void toPlayer(PlayerModifier player);

    /**
     * copy the marble in a new instance
     * @return new instance equals this
     */
    public abstract Marble copy();

    /**
     * two marble are equals if they are the same type
     * @param obj another marble
     * @return true if the two marbles are the same type
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Marble) {
            return (this.type() == ((Marble) obj).type());
        } else return false;
    }
}
