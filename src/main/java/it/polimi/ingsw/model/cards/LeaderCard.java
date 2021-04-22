package it.polimi.ingsw.model.cards;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.cards.effects.Effect;
import it.polimi.ingsw.model.requisite.Requisite;

/**
 * This class implements the LeaderCard
 */
public class LeaderCard extends Card{

    /**
     * This method is the constructor of the LeaderCard class. The attribute activated is initially set to false.
     * @param victoryPoint of the card.
     * @param requirements of the card.
     */

    @JsonCreator
    public LeaderCard(@JsonProperty("cardID") String cardID, @JsonProperty("effect") Effect effect, @JsonProperty("victoryPoint") int victoryPoint, @JsonProperty("requirements") List<Requisite> requirements ) {
        super(cardID, effect);
        this.victoryPoint = victoryPoint;
        this.requirements = requirements;
        this.activated = false;
    }

    /**
     * This attribute is the victory point of the card.
     */
    private int victoryPoint;

    /**
     * This attribute is the list of Loot that the LeaderCard requires to be activated.
     */
    private List<Requisite> requirements;

    /**
     * This attribute determine whether a card is already activated or not.
     */
    private boolean activated;

    /**
     * This method is used to get the value of victoryPoint.
     * @return the victoryPoint of the LeaderCard.
     */
    public int getVictoryPoint() {
        return victoryPoint;
    }

    /**
     * This method is used to get the List of Requisite which are the requirements to activate the LeaderCard.
     * @return the requirements of the LeaderCard.
     */
    public List<Requisite> getRequirements() {
        return requirements;
    }

    /**
     * This method is used to get the List of Requisite which are the requirements to activate the LeaderCard.
     * @return the requirements of the LeaderCard.
     */
    public List<Requisite> getCost() {
        return requirements;
    }

    /**
     * This method is used to set the attribute activated to true.
     */
    public void activate(){  //potrebbe essere utile far chiamare un eccezione se chiamo activated su una carta già attiva
        this.activated = true;
    }

    /**
     * This method returns the value of the attribute activated.
     * @return the logical value of activated.
     */
    public boolean isActivated() {
        return activated;
    }

}