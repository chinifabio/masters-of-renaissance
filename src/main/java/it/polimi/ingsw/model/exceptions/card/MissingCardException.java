package it.polimi.ingsw.model.exceptions.card;

import it.polimi.ingsw.model.cards.Card;

import static it.polimi.ingsw.TextColors.*;

/**
 * exception thrown when someone tries to peek a card from a deck where the card is missing
 */
public class MissingCardException extends Exception{
    /**
     * the required resource list of wrong production
     */
    private String missed;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public MissingCardException(String missed) {
        this.missed = missed;
    }

    /**
     * Prints this throwable and its backtrace to the
     * standard error stream.
     */
    @Override
    public void printStackTrace() {
        System.out.println(colorText(RED_BRIGHT, "This card was not found: " + this.missed));
    }
}
