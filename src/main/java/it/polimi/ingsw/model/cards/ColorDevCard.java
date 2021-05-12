package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.TextColors;

/**
 * This enumeration indicates the colors of the DevCards
 */
public enum ColorDevCard {
    GREEN(0), YELLOW(1), BLUE(2), PURPLE(3);

    private final int devSetupIndex;

    ColorDevCard(int index) {
        this.devSetupIndex = index;
    }

    public int getDevSetupIndex() {
        return this.devSetupIndex;
    }

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
