package it.polimi.ingsw.model.match.markettray.MarkerMarble;

import it.polimi.ingsw.TextColors;

public enum MarbleColor {
    BLUE, YELLOW, GRAY, RED, PURPLE, WHITE;

    /**
     * Returns the name of this enum constant, as contained in the
     * declaration.
     * @return the name of this enum constant
     */
    @Override
    public String toString() {
        return TextColors.colorMarbleType(this);
    }
}
