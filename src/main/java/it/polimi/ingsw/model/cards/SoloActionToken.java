package it.polimi.ingsw.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.cards.effects.Effect;

/**
 * This class represents the Tokens used in the SinglePlayer mode of the game.
 */
public class SoloActionToken extends Card{

    /**
     * This is the constructor of the class, it needs the cardID because the class extends Card, which requires a cardID.
     * @param cardID for the upper class.
     */
    @JsonCreator
    public SoloActionToken(@JsonProperty("cardID") String cardID, @JsonProperty("effect") Effect effect) {
        super(cardID,effect);
    }

}
