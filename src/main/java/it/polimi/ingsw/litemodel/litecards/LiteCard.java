package it.polimi.ingsw.litemodel.litecards;

import com.fasterxml.jackson.annotation.*;
import it.polimi.ingsw.litemodel.litecards.liteeffect.LiteEffect;

import java.util.HashMap;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "Dev", value = LiteDevCard.class),
        @JsonSubTypes.Type(name = "Token", value = LiteSoloActionToken.class),
        @JsonSubTypes.Type(name = "Leader", value = LiteLeaderCard.class)
})
public abstract class LiteCard {

    private final String cardID;

    private final LiteEffect effect;

    @JsonCreator
    public LiteCard(@JsonProperty("cardID") String cardID, @JsonProperty("effect") LiteEffect effect) {
        this.cardID = cardID;
        this.effect = effect;
    }

    public String getCardID() {
        return cardID;
    }

    public LiteEffect getEffect() {
        return effect;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * @return true the two card are equals from their ID
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LiteCard) return cardID.equals(((LiteCard) obj).cardID);
        else return false;
    }
}
