package it.polimi.ingsw.litemodel.litecards.literequirements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.view.cli.Colors;

/**
 * This class represents the requisite to activate a LeaderCard
 */
public class LiteCardRequisite extends LiteRequisite {

    /**
     * This attribute is the required level of the LeaderCard
     */
    private final LevelDevCard level;

    /**
     * This attribute is the required color of the LeaderCard
     */
    private final ColorDevCard color;

    /**
     * This attribute is the required number of cards to activate a LeaderCard
     */
    private final int amount;

    @JsonCreator
    public LiteCardRequisite(@JsonProperty("level") LevelDevCard level, @JsonProperty("color") ColorDevCard color, @JsonProperty("amount") int amount) {
        this.level = level;
        this.color = color;
        this.amount = amount;
    }

    /**
     * This method is used to print the requisite in CLI
     * @param leaderCard to print
     * @param x horizontal position
     * @param y vertical position
     */
    @Override
    public void printRequisite(String[][] leaderCard, int x, int y) {
        leaderCard[x + 1][y + 2] = String.valueOf(this.amount);
        leaderCard[x + 1][y + 3] = Colors.colorBackGround(Colors.BLACK,color.getDevCardColorBackground(),String.valueOf(this.level.getLevelCard()));
    }
}
