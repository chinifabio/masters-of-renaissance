package it.polimi.ingsw.litemodel.litecards;

import com.fasterxml.jackson.annotation.*;
import it.polimi.ingsw.litemodel.litecards.liteeffect.LiteEffect;
import it.polimi.ingsw.litemodel.litecards.literequirements.LiteRequisite;
import it.polimi.ingsw.model.requisite.Requisite;

import java.util.List;

/**
 * This class represents the lite version of a LeaderCard
 */
public class LiteLeaderCard extends LiteCard {

    /**
     * This attribute is the number of victory points of the card
     */
    private final int victoryPoints;

    /**
     * This attribute is the list of requirements to activate the card
     */
    private final List<LiteRequisite> requirements;

    /**
     * This attribute shows if the card is activated
     */
    private final boolean activated;

    /**
     * This is the constructor of the class:
     * @param cardID String of the card
     * @param effect LiteEffect of the card
     * @param victoryPoints number of victory points
     * @param requirements list of LiteRequisite of the card
     * @param activated true if activated, false otherwise
     */
    @JsonCreator
    public LiteLeaderCard(@JsonProperty("cardID") String cardID,
                          @JsonProperty("effect") LiteEffect effect,
                          @JsonProperty("victoryPoint") int victoryPoints,
                          @JsonProperty("requirements") List<LiteRequisite> requirements,
                          @JsonProperty("activated") boolean activated) {
        super(cardID, effect);
        this.victoryPoints = victoryPoints;
        this.requirements = requirements;
        this.activated = activated;
    }

    /**
     * This method returns the cardID
     * @return a String with the ID of the card
     */
    public String getId(){
        return super.getCardID();
    }

    /**
     * This method returns the victory point of the card
     * @return the victory points
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * This method returns the requirements to activate the card
     * @return the list of LiteRequisite
     */
    public List<LiteRequisite> getRequirements() {
        return requirements;
    }

    /**
     * This method returns if the card is activated
     * @return true if activated, false if not
     */
    public boolean isActivated() {
        return activated;
    }

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return this.getId();
    }
}
