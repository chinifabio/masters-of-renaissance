package it.polimi.ingsw.litemodel.litecards.liteeffect;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.view.cli.Colors;

/**
 * This class represents on of the effect of the cards
 */
public class LiteShuffleMoveOneEffect extends LiteEffect {

    /**
     * This method is the constructor of the class
     */
    @JsonCreator
    public LiteShuffleMoveOneEffect() {}

    /**
     * This method is used to print the effect in cli
     * @param soloToken to print
     * @param x horizontal position
     * @param y vertical position
     */
    @Override
    public void printEffect(String[][] soloToken, int x, int y) {
        soloToken[x][y+2] = "+";
        soloToken[x][y+3] = "1";
        soloToken[x][y+4] = Colors.color(Colors.WHITE_BRIGHT,"┼");
        soloToken[x+1][y+2] = "(";
        soloToken[x+1][y+3] = "S";
        soloToken[x+1][y+4] = ")";
    }
}
