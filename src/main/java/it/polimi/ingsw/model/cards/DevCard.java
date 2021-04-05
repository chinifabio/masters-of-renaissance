package it.polimi.ingsw.model.cards;

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
    public DevCard(String cardID, Effect effect, int victoryPoint, LevelDevCard level, ColorDevCard color, List<Requisite> cost){
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

 //   /**
 //   * This attribute is the list of Resource that the DevCard will produce when activated during the production phase.
 //   */
 //   private List<Resource> result;
 //   /**
 //    * This method is used to get the List of Resource that the DevCard produces.
 //    * @return the result of the production.
 //    */
 //   public List<Resource> getResult() {
 //       return result;
 //   }

    @Override
    public String toString() {
        return "DevCard{" +
                "VP=" + victoryPoint +
                ", lev=" + level +
                ", color=" + color +
                '}';
    }

}
