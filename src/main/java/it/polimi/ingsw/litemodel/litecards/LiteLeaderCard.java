package it.polimi.ingsw.litemodel.litecards;

import com.fasterxml.jackson.annotation.*;
import it.polimi.ingsw.litemodel.litecards.liteeffect.LiteEffect;
import it.polimi.ingsw.litemodel.litecards.literequirements.LiteRequirements;

import java.util.List;


public class LiteLeaderCard extends LiteCard {

    private final int victoryPoints;

    private final List<LiteRequirements> requirements;

    private final boolean activated;

    @JsonCreator
    public LiteLeaderCard(@JsonProperty("cardID") String cardID,
                          @JsonProperty("effect") LiteEffect effect,
                          @JsonProperty("victoryPoint") int victoryPoints,
                          @JsonProperty("requirements") List<LiteRequirements> requirements,
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

    public List<LiteRequirements> getRequirements() {
        return requirements;
    }

    public boolean isActivated() {
        return activated;
    }
}
