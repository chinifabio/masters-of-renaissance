package it.polimi.ingsw.litemodel.litecards;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.litecards.liteeffect.LiteEffect;

public class LiteSoloActionToken extends LiteCard {

    @JsonCreator
    public LiteSoloActionToken(@JsonProperty("cardID") String cardID,@JsonProperty("effect") LiteEffect effect) {
        super(cardID, effect);
    }
}
