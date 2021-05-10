package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.TextColors;

/**
 * This enumeration indicates the colors of the DevCards
 */
public enum ColorDevCard {
    GREEN, PURPLE, BLUE, YELLOW;

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
