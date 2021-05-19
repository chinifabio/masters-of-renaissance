package it.polimi.ingsw.litemodel.litemarkettray;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LiteMarketTray {

    public final int col;

    public final int row;

    private final LiteMarble[][] marbles;

    private final LiteMarble slideMarble;

    @JsonCreator
    public LiteMarketTray(@JsonProperty("marbles") LiteMarble[][] marbles, @JsonProperty("slideMarble") LiteMarble slideMarble, @JsonProperty("row") int row, @JsonProperty("col") int col) {
        this.col = col;
        this.row = row;
        this.marbles = new LiteMarble[row][col];

        for (int i = 0; i < row; i++) {
            System.arraycopy(marbles[i], 0, this.marbles[i], 0, col);
        }

        this.slideMarble = slideMarble;
    }


    public LiteMarble[][] getMarbles() {
        LiteMarble[][] res = new LiteMarble[row][col];

        for (int i = 0; i < this.row; i++) {
            System.arraycopy(this.marbles[i], 0, res[i], 0, this.col);
        }

        return res;
    }

    public LiteMarble getSlideMarble() {
        return slideMarble;
    }

}