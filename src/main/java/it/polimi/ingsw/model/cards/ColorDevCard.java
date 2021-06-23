package it.polimi.ingsw.model.cards;


import it.polimi.ingsw.view.cli.Colors;

/**
 * This enumeration indicates the colors of the DevCards
 */
public enum ColorDevCard {
    GREEN(Colors.GREEN,Colors.GREEN_BACKGROUND),
    YELLOW(Colors.YELLOW, Colors.YELLOW_BACKGROUND),
    BLUE(Colors.BLUE, Colors.BLUE_BACKGROUND),
    PURPLE(Colors.PURPLE, Colors.PURPLE_BACKGROUND),
    NOCOLOR(Colors.WHITE, Colors.WHITE);

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
     * @param color is the color of the card
     * @param backGround is the background color in CLI
     */
    ColorDevCard(String color, String backGround) {
        this.color = color;
        this.backGround = backGround;
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
}
