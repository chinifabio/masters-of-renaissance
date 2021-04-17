package it.polimi.ingsw.model.exceptions.game.movesexception;

import static it.polimi.ingsw.TextColors.*;

/**
 * Used to handle the error if someone do an illegal moves.
 * For example if someone in the same turn buy two dev Cards
 */
public class MainActionDoneException extends Exception{
    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public MainActionDoneException() {}

    /**
     * Prints this throwable and its backtrace to the
     * standard error stream.
     */
    @Override
    public void printStackTrace() {
        System.out.println(colorText(RED_BRIGHT, "It is not possible to do further actions!"));
    }
}
