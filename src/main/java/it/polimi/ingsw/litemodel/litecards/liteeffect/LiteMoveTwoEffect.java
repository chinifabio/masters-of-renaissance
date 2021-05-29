package it.polimi.ingsw.litemodel.litecards.liteeffect;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.view.cli.Colors;

public class LiteMoveTwoEffect extends LiteEffect {

    @JsonCreator
    public LiteMoveTwoEffect() {}


    @Override
    public void printEffect(String[][] soloToken, int x, int y) {
        soloToken[x][y+2] = "+";
        soloToken[x][y+3] = "2";
        soloToken[x][y+4] = Colors.color(Colors.WHITE_BRIGHT,"┼");
    }
}
