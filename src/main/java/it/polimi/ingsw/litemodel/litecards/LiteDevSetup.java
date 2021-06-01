package it.polimi.ingsw.litemodel.litecards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;

public class LiteDevSetup {

    public final int row;

    public final int col;

    private final LiteDevCard[][] devSetup;

    public LiteDevSetup(@JsonProperty("devSetup") LiteDevCard[][] cards, @JsonProperty("row") int row, @JsonProperty("col") int col) {
        this.devSetup = new LiteDevCard[row][col];
        this.row = row;
        this.col = col;

        for (int i = 0; i < row; i++) {
            System.arraycopy(cards[i], 0, this.devSetup[i], 0, col);
        }
    }

    public LiteDevCard[][] getDevSetup() {
        LiteDevCard[][] result = new LiteDevCard[row][col];

        for (int i = 0; i < row; i++) {
            System.arraycopy(this.devSetup[i], 0, result[i], 0, col);
        }

        return result;
    }

    @JsonIgnore
    public void setDevCard(LiteDevCard card, LevelDevCard row, ColorDevCard col) {}

}
