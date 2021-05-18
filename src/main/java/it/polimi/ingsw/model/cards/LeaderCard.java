package it.polimi.ingsw.model.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.litemodel.litecards.LiteLeaderCard;
import it.polimi.ingsw.model.cards.effects.Effect;
import it.polimi.ingsw.model.requisite.Requisite;

/**
 * This class represents the LeaderCard
 */
public class LeaderCard extends Card {

    /**
     * This method is the constructor of the LeaderCard class. The attribute activated is initially set to false.
     * @param cardID is the identifier of the Card
     * @param effect is the effect of the LeaderCard that is activated by the Player
     * @param victoryPoint is the number of victoryPoints of the card
     * @param requirements is the requirements to activate the LeaderCard
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
    private final int victoryPoint;

    /**
     * This attribute is the list of Loot that the LeaderCard requires to be activated.
     */
    private final List<Requisite> requirements;

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
    public void activate(){  // can launch an exception if the leader is activated yet
        this.activated = true;
    }

    /**
     * This method returns the value of the attribute activated.
     * @return the logical value of activated.
     */
    public boolean isActivated() {
        return activated;
    }

    /**
     * Create a lite version of the class and serialize it in json
     *
     * @return the json representation of the lite version of the class
     */
    @Override
    public LiteLeaderCard liteVersion() {
        return new LiteLeaderCard(this.cardID, this.effect.liteVersion(), this.victoryPoint,
                this.requirements.stream().map(Requisite::liteVersion).collect(Collectors.toList()), this.activated);
    }
}