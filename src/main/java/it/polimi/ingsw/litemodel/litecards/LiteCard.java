package it.polimi.ingsw.litemodel.litecards;

import com.fasterxml.jackson.annotation.*;
import it.polimi.ingsw.litemodel.litecards.liteeffect.LiteEffect;

/**
 * This class represents the lite version of a Card
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "Dev", value = LiteDevCard.class),
        @JsonSubTypes.Type(name = "Token", value = LiteSoloActionToken.class),
        @JsonSubTypes.Type(name = "Leader", value = LiteLeaderCard.class)
})
public abstract class LiteCard {

    /**
     * This attribute is the ID of the card
     */
    private final String cardID;

    /**
     * This attribute is the effect of the card
     */
    private final LiteEffect effect;

    /**
     * This is the constructor of the class
     * @param cardID String ID of the card
     * @param effect LiteEffect of the card
     */
    @JsonCreator
    public LiteCard(@JsonProperty("cardID") String cardID, @JsonProperty("effect") LiteEffect effect) {
        this.cardID = cardID;
        this.effect = effect;
    }

    /**
     * This method returns the cardID
     * @return String ID of the card
     */
    public String getCardID() {
        return cardID;
    }

    /**
     * This method returns the effect of the card
     * @return LiteEffect of the card
     */
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
