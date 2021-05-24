package it.polimi.ingsw.model.exceptions.card;

import it.polimi.ingsw.model.cards.Card;

public class AlreadyInDeckException extends Exception{

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public AlreadyInDeckException(Card already) {
        super("This card is already in the Deck: " + already.getCardID());
    }

    /**
     * Prints this throwable and its backtrace to the
     * standard error stream.
     */
    @Override
    public void printStackTrace() {
        System.out.println(super.getMessage());
    }
}
