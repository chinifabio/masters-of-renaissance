package it.polimi.ingsw.litemodel.litecards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;

/**
 * This class represents the lite version of the DevSetup
 */
public class LiteDevSetup {

    /**
     * This attributes is the number of rows of the DevSetup
     */
    public final int row;

    /**
     * This attributes is the number of columns of the DevSetup
     */
    public final int col;

    /**
     * This attributes is the matrix that contains the top LiteDevCards
     */
    private final LiteDevCard[][] devSetup;

    /**
     * This is the constructor of the class
     * @param cards to insert in the matrix
     * @param row to set
     * @param col to set
     */
    public LiteDevSetup(@JsonProperty("devSetup") LiteDevCard[][] cards, @JsonProperty("row") int row, @JsonProperty("col") int col) {
        this.devSetup = new LiteDevCard[row][col];
        this.row = row;
        this.col = col;

        for (int i = 0; i < row; i++) {
            System.arraycopy(cards[i], 0, this.devSetup[i], 0, col);
        }
    }

    /**
     * This method returns a matrix that contains the top cards of the DevSetup
     * @return a matrix of LiteDevCards
     */
    public LiteDevCard[][] getDevSetup() {
        LiteDevCard[][] result = new LiteDevCard[row][col];

        for (int i = 0; i < row; i++) {
            System.arraycopy(this.devSetup[i], 0, result[i], 0, col);
        }

        return result;
    }

    /**
     * This method serves no purposes apart from letting Jackson do his work
     */
    @JsonIgnore
    public void setDevCard(LiteDevCard card, LevelDevCard row, ColorDevCard col) {}

}
