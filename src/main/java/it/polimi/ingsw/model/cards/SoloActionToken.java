package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.effects.Effect;


public class SoloActionToken extends Card{

    public SoloActionToken() {}

    /**
     * This is the constructor of the class, it needs the cardID because the class extends Card, which requires a cardID.
     * @param cardID for the upper class.
     */
    public SoloActionToken(String cardID, Effect effect) {
        super(cardID,effect);
    }

}
