package it.polimi.ingsw.litemodel.litecards.literequirements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.view.cli.CLI;

public class LiteColorCardRequisite extends LiteRequisite {

    private final ColorDevCard color;

    private final int amount;

    @JsonCreator
    public LiteColorCardRequisite(@JsonProperty("color") ColorDevCard color, @JsonProperty("amount") int amount) {
        this.color = color;
        this.amount = amount;
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
        leaderCard[x + 1][y + 3] = TextColors.colorText(color.getDevCardColor(),"█");
    }
}
