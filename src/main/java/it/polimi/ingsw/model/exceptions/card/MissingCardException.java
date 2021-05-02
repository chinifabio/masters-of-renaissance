package it.polimi.ingsw.model.exceptions.card;

import static it.polimi.ingsw.TextColors.*;

public class MissingCardException extends Exception{
    /**
     * the required resource list of wrong production
     */
    private final String missed;

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
