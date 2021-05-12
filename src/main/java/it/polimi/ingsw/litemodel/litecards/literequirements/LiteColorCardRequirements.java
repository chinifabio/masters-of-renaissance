package it.polimi.ingsw.litemodel.litecards.literequirements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.cards.ColorDevCard;

public class LiteColorCardRequirements extends LiteRequirementsType {

    private ColorDevCard color;

    private int amount;

    @JsonCreator
    public LiteColorCardRequirements(@JsonProperty("color") ColorDevCard color,@JsonProperty("amount") int amount) {
        this.color = color;
        this.amount = amount;
    }

    public ColorDevCard getColor() {
        return color;
    }

    public int getAmount() {
        return amount;
    }
}
