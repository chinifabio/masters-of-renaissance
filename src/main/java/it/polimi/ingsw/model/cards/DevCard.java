package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.requisite.Requisite;
import java.util.List;

public class DevCard extends Card{
    public DevCard(String cardID, int victoryPoint, LevelDevCard level, ColorDevCard color, List<Requisite> cost) {
        super(cardID);
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
     * This attribute is the list of Loot that the DevCard requires to be bought.
     */
    private List<Requisite> cost;

    /**
     * This method is used to get the value of victoryPoint.
     * @return the victoryPoint of the DevCard.
     */
    public int getVictoryPoint() {
        return victoryPoint;
    }

    /**
     * This method returns the value of the level attribute.
     * @return the level of the DevCard.
     */
    public LevelDevCard getLevel() {
        return level;
    }

    /**
     * This method returns the value of the color attribute.
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

    /**
     * This method implements the required method from the abstract Card class. It permits to activate the effect AddProduction of the DevCard.
     */
    @Override
    public void useEffect() {

    }
}
