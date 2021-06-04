package it.polimi.ingsw.model.match.markettray.MarkerMarble;

import java.util.ArrayList;
import java.util.List;

/**
 * This enumeration represents the Marbles' colors
 */
public enum MarbleColor {
    BLUE, YELLOW, GRAY, RED, PURPLE, WHITE;

    /**
     * Returns the name of this enum constant, as contained in the
     * declaration.
     * @return the name of this enum constant
     */
    @Override
    public String toString() {
        return this.name();
        //return TextColors.colorMarbleType(this);
    }

}
