package it.polimi.ingsw.model.cards;


import it.polimi.ingsw.view.cli.Colors;

/**
 * This enumeration indicates the colors of the DevCards
 */
public enum ColorDevCard {
    GREEN(0, Colors.GREEN,Colors.GREEN_BACKGROUND),
    YELLOW(1, Colors.YELLOW, Colors.YELLOW_BACKGROUND),
    BLUE(2,Colors.BLUE, Colors.BLUE_BACKGROUND),
    PURPLE(3, Colors.PURPLE, Colors.PURPLE_BACKGROUND),
    NOCOLOR(-1, Colors.WHITE, Colors.WHITE);

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
        return Colors.colorDevCard(this);
    }


}
