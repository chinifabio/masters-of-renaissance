package it.polimi.ingsw.model.cards;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * This abstract class generalize the concept of Card, everyone has a cardID and an effect.
 */
public abstract class Card{

    /**
     * This attribute is the ID of the card. Every card has a different cardID.
     */
    private String cardID;

    /**
     * This is the constructor of the class.
     * @param cardID
     */
    public Card(String cardID) {
        this.cardID = cardID;
    }

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
    public abstract void useEffect();

}
