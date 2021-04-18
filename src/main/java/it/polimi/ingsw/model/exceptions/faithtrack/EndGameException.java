package it.polimi.ingsw.model.exceptions.faithtrack;

import static it.polimi.ingsw.TextColors.*;

public class EndGameException extends Exception {

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public EndGameException() {}

    /**
     * Prints this throwable and its backtrace to the
     * standard error stream.
     */
    @Override
    public void printStackTrace() {
        System.out.println(colorText(RED_BRIGHT, "The player can't do this move!" ));
    }
}
