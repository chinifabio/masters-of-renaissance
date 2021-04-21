package it.polimi.ingsw.model.exceptions;

import static it.polimi.ingsw.TextColors.RED_BRIGHT;
import static it.polimi.ingsw.TextColors.colorText;

public class ExtraProductionException extends Exception{
    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public ExtraProductionException() {}

    /**
     * Prints this throwable and its backtrace to the
     * standard error stream.
     */
    @Override
    public void printStackTrace() {
        System.out.println(colorText(RED_BRIGHT, "Player cannot get additional production"));
    }
}
