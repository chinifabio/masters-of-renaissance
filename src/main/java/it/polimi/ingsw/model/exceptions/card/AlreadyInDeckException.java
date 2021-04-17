package it.polimi.ingsw.model.exceptions.card;

import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.resource.Resource;

import static it.polimi.ingsw.TextColors.*;

public class AlreadyInDeckException extends Exception{
    /**
     * the required resource list of wrong production
     */
    private Card alredy;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public AlreadyInDeckException(Card alredy) {
        this.alredy = alredy;
    }

    /**
     * Prints this throwable and its backtrace to the
     * standard error stream.
     */
    @Override
    public void printStackTrace() {
        System.out.println(TextColors.colorText(RED_BRIGHT, "This card is alredy in the Deck: " + this.alredy.getCardID()));
    }
}
