package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.TextColors;

import java.util.EnumMap;
import java.util.Map;

public enum ColorDevCard {
    GREEN, YELLOW, BLUE, PURPLE;

    /**
     * Returns the name of this enum constant, as contained in the
     * declaration.
     * @return the name of this enum constant
     */
    @Override
    public String toString() {
        return TextColors.colorDevCard(this);
    }
}
