package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.effects.Effect;
import it.polimi.ingsw.model.player.Player;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * This abstract class generalize the concept of Card, every Card has a cardID and an effect.
 */
public abstract class Card{

    /**
     * This is the constructor of the class. It needs a cardID and an effect.
     * @param cardID of the Card.
     * @param effect of the Card.
     */
    public Card(String cardID, Effect effect) {
        this.cardID = cardID;
        this.effect = effect;
    }

    /**
     * This attribute is the ID of the card. Every card has a different cardID.
     */
    private String cardID;

    /**
     * This attribute is the effect of the card. The effect is highly tied with the card type.
     */
    private Effect effect;

    /**
     * This method returns the cardID of the card.
     * @return the cardID.
     */
    public String getCardID() {
        return this.cardID;
    }

    /**
     * This method is used by subclasses to implement their effect: AddDepot, AddDiscount, AddProduction, DestroyCards, MoveTwo, ShuffleMoveONe, WhiteMarble.
     */
    public void useEffect(Player p){
        effect.use(p);
    };


    /**
     * This method overloads the one on the Object class. It permits to check if two cards are the same card, comparing their cardIDs.
     * @param c to confront with the first.
     * @return true if cardID are equals, false if not.
     */
    public boolean equals(Card c) {
        return this.cardID.equals(c.cardID);
    }
}
