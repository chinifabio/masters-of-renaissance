package it.polimi.ingsw.model.exceptions.warehouse;

import static it.polimi.ingsw.TextColors.*;
import static it.polimi.ingsw.TextColors.RED_BRIGHT;

public class UnobtainableResourceException extends Exception{

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public UnobtainableResourceException() {
    }

    /**
     * Prints this throwable and its backtrace to the
     * standard error stream.
     */
    @Override
    public void printStackTrace() {
        System.out.println(colorText(RED_BRIGHT, "The Player can't receive this resource"));
    }
}
