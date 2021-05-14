package it.polimi.ingsw.litemodel.litecards.literequirements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;

public class LiteCardRequirements extends LiteRequirementsType {

    private LevelDevCard level;

    private ColorDevCard color;

    private int amount;

    @JsonCreator
    public LiteCardRequirements(@JsonProperty("level") LevelDevCard level,@JsonProperty("color") ColorDevCard color,@JsonProperty("amount") int amount) {
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
}
