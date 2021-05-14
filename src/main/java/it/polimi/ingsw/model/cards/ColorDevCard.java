package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.TextColors;

/**
 * This enumeration indicates the colors of the DevCards
 */
public enum ColorDevCard {
    GREEN(0, TextColors.GREEN), YELLOW(1, TextColors.YELLOW),
    BLUE(2,TextColors.BLUE), PURPLE(3, TextColors.PURPLE);

    private final int devSetupIndex;

    private final String color;

    ColorDevCard(int index, String color) {
        this.devSetupIndex = index;
        this.color = color;
    }

    public int getDevSetupIndex() {
        return this.devSetupIndex;
    }

    public String getDevCardColor(){
        return this.color;
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
