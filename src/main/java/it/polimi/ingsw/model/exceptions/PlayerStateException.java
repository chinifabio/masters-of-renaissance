package it.polimi.ingsw.model.exceptions;

import static it.polimi.ingsw.TextColors.RED_BRIGHT;
import static it.polimi.ingsw.TextColors.colorText;

public class PlayerStateException extends Exception{
    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public PlayerStateException(String message) {
        super(message);
    }

    /**
     * Prints this throwable and its backtrace to the
     * standard error stream.
     */
    @Override
    public void printStackTrace() {
        System.out.println((colorText(RED_BRIGHT,this.getMessage())));
    }
}
