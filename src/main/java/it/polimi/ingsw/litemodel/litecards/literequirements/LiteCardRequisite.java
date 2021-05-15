package it.polimi.ingsw.litemodel.litecards.literequirements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;

public class LiteCardRequisite extends LiteRequisite {

    private LevelDevCard level;

    private ColorDevCard color;

    private int amount;

    @JsonCreator
    public LiteCardRequisite(@JsonProperty("level") LevelDevCard level, @JsonProperty("color") ColorDevCard color, @JsonProperty("amount") int amount) {
        this.level = level;
        this.color = color;
        this.amount = amount;
    }

    public LevelDevCard getLevel() {
        return level;
    }

    public ColorDevCard getColor() {
        return color;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public void printRequisite(String[][] leaderCard, int x, int y) {
        leaderCard[x + 1][y + 2] = String.valueOf(this.amount);
        leaderCard[x + 1][y + 3] = TextColors.colorTextBackGround(TextColors.BLACK,color.getDevCardColorBackground(),String.valueOf(this.level.getLevelCard()));
    }
}
