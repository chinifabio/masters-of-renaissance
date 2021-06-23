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

    /**
     * This attribute is the column of the cards' grid
     */
    private final int devSetupIndex;

    /**
     * This attribute is the color of the card
     */
    private final String color;

    /**
     * This attribute is the background color, used in CLI
     */
    private final String backGround;

    /**
     * This is the constructor of the class
     * @param index is the index in the cards' grid
     * @param color is the color of the card
     * @param backGround is the background color in CLI
     */
    ColorDevCard(int index, String color, String backGround) {
        this.devSetupIndex = index;
        this.color = color;
        this.backGround = backGround;
    }

    //Todo remove (?)
    public int getDevSetupIndex() {
        return this.devSetupIndex;
    }

    /**
     * @return the color of the card
     */
    public String getDevCardColor(){
        return this.color;
    }

    /**
     * @return the background color
     */
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
