package it.polimi.ingsw.litemodel.litemarkettray;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class represents the lite version of the MarketTray
 */
public class LiteMarketTray {

    /**
     * This attributes is the number of rows of the tray
     */
    public final int row;

    /**
     * This attributes is the number of columns of the tray
     */
    public final int col;

    /**
     * This attributes is the matrix that contains the LiteMarbles
     */
    private final LiteMarble[][] marbles;

    /**
     * This attribute is the slideMarble
     */
    private final LiteMarble slideMarble;

    /**
     * This is the constructor of the class: after setting every attribute, it fills the matrix
     * @param marbles of the matrix
     * @param slideMarble slide one
     * @param row number of rows
     * @param col number of columns
     */
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

    /**
     * This method returns the matrix of marbles
     * @return a matrix of LiteMarbles
     */
    public LiteMarble[][] getMarbles() {
        LiteMarble[][] res = new LiteMarble[row][col];

        for (int i = 0; i < this.row; i++) {
            System.arraycopy(this.marbles[i], 0, res[i], 0, this.col);
        }

        return res;
    }

    /**
     * This method returns the slideMarble
     * @return the slideMarble
     */
    public LiteMarble getSlideMarble() {
        return slideMarble;
    }

}
