package it.polimi.ingsw.litemodel.litecards;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.litecards.liteeffect.LiteEffect;

/**
 * This class represents the lite version of a SoloActionToken
 */
public class LiteSoloActionToken extends LiteCard {

    /**
     * This is the constructor of the class:
     * @param cardID of the token
     * @param effect of the token
     */
    @JsonCreator
    public LiteSoloActionToken(@JsonProperty("cardID") String cardID,@JsonProperty("effect") LiteEffect effect) {
        super(cardID, effect);
    }

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return getCardID();
    }
}
