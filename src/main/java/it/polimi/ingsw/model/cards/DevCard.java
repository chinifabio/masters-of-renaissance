package it.polimi.ingsw.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.cards.effects.Effect;
import it.polimi.ingsw.model.requisite.Requisite;
import it.polimi.ingsw.model.resource.Resource;

import java.util.List;

public class DevCard extends Card{

    /**
     * This is the constructor of the class.
     * @param cardID requested from the upper class.
     * @param effect requested from the upper class.
     * @param victoryPoint that the card will give at the end of the game.
     * @param level of the card.
     * @param color of the card.
     */
    @JsonCreator
    public DevCard(@JsonProperty("cardID") String cardID, @JsonProperty("effect") Effect effect, @JsonProperty("victoryPoint") int victoryPoint, @JsonProperty("level") LevelDevCard level, @JsonProperty("color") ColorDevCard color, @JsonProperty("cost") List<Requisite> cost){
        super(cardID, effect);
        this.victoryPoint = victoryPoint;
        this.level = level;
        this.color = color;
        this.cost = cost;
    }

    /**
     * This attribute is the victory point of the card.
     */
    private int victoryPoint;

    /**
     * This attribute uses the enumeration LevelDeckCard to represent the level of the DevCard.
     */
    private LevelDevCard level;

    /**
     * This attribute uses the enumeration ColorDeckCard to represent the color of the DevCard.
     */
    private ColorDevCard color;

    /**
     * This attribute is the list of Requisite that the DevCard requires to be bought.
     */
    private List<Requisite> cost;

    /**
     * This method is used to get the value of victoryPoint of the Card.
     * @return the victoryPoint of the DevCard.
     */
    public int getVictoryPoint() {
        return victoryPoint;
    }

    /**
     * This method is used to get the value of level of the Card.
     * @return the level of the DevCard.
     */
    public LevelDevCard getLevel() {
        return level;
    }

    /**
     * This method is used to get the color of the Card.
     * @return the color of the DevCard.
     */
    public ColorDevCard getColor() {
        return color;
    }

    /**
     * This method is used to get the List of Requisite which are the requirements to buy the DevCard.
     * @return the cost of the DevCard.
     */
    public List<Requisite> getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return "DevCard{" +
                "CardID= " + this.getCardID() +
                " VP=" + victoryPoint +
                ", lev=" + level +
                ", color=" + color +
                '}';
    }

}
