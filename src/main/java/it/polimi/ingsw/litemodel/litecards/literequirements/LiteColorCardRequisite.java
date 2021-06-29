package it.polimi.ingsw.litemodel.litecards.literequirements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.view.cli.Colors;

/**
 * This class represents the requisite to activate a LeaderCard
 */
public class LiteColorCardRequisite extends LiteRequisite {

    /**
     * This attribute is the required color of the LeaderCard
     */
    private final ColorDevCard color;

    /**
     * This attribute is the required amount of cards of the LeaderCard
     */
    private final int amount;

    /**
     * This is the constructor of the class:
     * @param color ColorDevCard required
     * @param amount amount required
     */
    @JsonCreator
    public LiteColorCardRequisite(@JsonProperty("color") ColorDevCard color, @JsonProperty("amount") int amount) {
        this.color = color;
        this.amount = amount;
    }

    /**
     * This method returns the required color
     * @return the ColorDevCard required
     */
    public ColorDevCard getColor() {
        return color;
    }

    /**
     * This method returns the required amount of cards
     * @return the required number of cards
     */
    public int getAmount() {
        return amount;
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
        leaderCard[x + 1][y + 3] = Colors.color(color.getDevCardColor(),"█");
    }
}
