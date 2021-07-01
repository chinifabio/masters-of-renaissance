package it.polimi.ingsw.litemodel.litecards.liteeffect;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.view.cli.Colors;

/**
 * This class represents on of the effect of the cards
 */
public class LiteDestroyCardsEffect extends LiteEffect{

    /**
     * This attribute is the color of the devards to destroy
     */
    private final ColorDevCard color;

    /**
     * This is the constructor of the class
     * @param c is the ColorDevCard to destroy
     */
    @JsonCreator
    public LiteDestroyCardsEffect(@JsonProperty("color") ColorDevCard c) {
        this.color = c;
    }

    /**
     * This method is used to print the effect in cli
     * @param soloToken to print
     * @param x horizontal position
     * @param y vertical position
     */
    @Override
    public void printEffect(String[][] soloToken, int x, int y) {
        soloToken[x][y+2] = "-";
        soloToken[x][y+3] = "2";
        soloToken[x][y+4] = Colors.color(color.getDevCardColor(),"█");
    }
}
