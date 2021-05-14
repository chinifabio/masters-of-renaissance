package it.polimi.ingsw.litemodel.litecards;

import com.fasterxml.jackson.annotation.*;
import it.polimi.ingsw.litemodel.litecards.liteeffect.LiteEffect;
import it.polimi.ingsw.litemodel.litecards.literequirements.LiteRequirementsType;
import it.polimi.ingsw.model.requisite.Requisite;

import java.util.List;


public class LiteLeaderCard extends LiteCard {

    private final int victoryPoints;

    private final List<Requisite> requirements;

    private final boolean activated;

    @JsonCreator
    public LiteLeaderCard(@JsonProperty("cardID") String cardID,
                          @JsonProperty("effect") LiteEffect effect,
                          @JsonProperty("victoryPoint") int victoryPoints,
                          @JsonProperty("requirements") List<Requisite> requirements,
                          @JsonProperty("activated") boolean activated) {
        super(cardID, effect);
        this.victoryPoints = victoryPoints;
        this.requirements = requirements;
        this.activated = activated;
    }

    public String getId(){
        return super.getCardID();
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public List<Requisite> getRequirements() {
        return requirements;
    }

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
