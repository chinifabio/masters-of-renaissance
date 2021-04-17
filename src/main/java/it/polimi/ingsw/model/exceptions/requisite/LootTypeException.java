package it.polimi.ingsw.model.exceptions.requisite;

import static it.polimi.ingsw.TextColors.*;
import static it.polimi.ingsw.TextColors.RED_BRIGHT;

/**
 * exception thrown when wrong method is invoked in a subclass of Loot
 */
public class LootTypeException extends Exception {
    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public LootTypeException() {}

    /**
     * Prints this throwable and its backtrace to the
     * standard error stream.
     */
    @Override
    public void printStackTrace() {
        System.out.println(colorText(RED_BRIGHT, "This attribute cannot be obtained from this Requisite"));
    }
}
