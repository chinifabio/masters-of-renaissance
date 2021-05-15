package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.TextColors;

/**
 * This enumeration indicates the colors of the DevCards
 */
public enum ColorDevCard {
    GREEN(0, TextColors.GREEN,TextColors.GREEN_BACKGROUND),
    YELLOW(1, TextColors.YELLOW, TextColors.YELLOW_BACKGROUND),
    BLUE(2,TextColors.BLUE, TextColors.BLUE_BACKGROUND),
    PURPLE(3, TextColors.PURPLE, TextColors.PURPLE_BACKGROUND),
    NOCOLOR(-1, TextColors.RED_BACKGROUND, TextColors.RED_BACKGROUND);

    private final int devSetupIndex;

    private final String color;

    private final String backGround;

    ColorDevCard(int index, String color, String backGround) {
        this.devSetupIndex = index;
        this.color = color;
        this.backGround = backGround;
    }

    public int getDevSetupIndex() {
        return this.devSetupIndex;
    }

    public String getDevCardColor(){
        return this.color;
    }

    public String getDevCardColorBackground(){
        return this.backGround;
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
