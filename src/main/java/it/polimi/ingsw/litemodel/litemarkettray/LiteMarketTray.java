package it.polimi.ingsw.litemodel.litemarkettray;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.litecards.LiteDevCard;

import java.util.ArrayList;
import java.util.List;

public class LiteMarketTray {

    private static final int MAX_VERT = 4;

    private  static final int MAX_HOR = 3;

    private final List<LiteMarble> marbles = new ArrayList<>();

    private final LiteMarble slideMarble;

    @JsonCreator
    public LiteMarketTray(@JsonProperty("marbles") List<LiteMarble> marbles, @JsonProperty("slideMarble") LiteMarble slideMarble, @JsonProperty("line") int row, @JsonProperty("column") int col) {
        /*this.marbles = new LiteMarble[row][col];

        int k = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                this.marbles[i][j] = marbles.get(k);
                k++;
            }
        }*/

        this.marbles.addAll(marbles);

        this.slideMarble = slideMarble;
    }

    public List<LiteMarble> getMarbles() {
        return this.marbles;
    }

    @JsonIgnore
    public LiteMarble[][] getMarbles(int z) {
        LiteMarble[][] res= new LiteMarble[MAX_HOR][MAX_VERT];
        int k = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                res[i][j] = this.marbles.get(k);
                k++;
            }
        }
        return res;
    }

    @JsonIgnore
    public LiteMarble getSlideMarble() {
        return slideMarble;
    }

}
