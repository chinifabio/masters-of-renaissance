package it.polimi.ingsw.model.exceptions.game.movesexception;

import it.polimi.ingsw.TextColors;

import static it.polimi.ingsw.TextColors.RED_BRIGHT;
import static it.polimi.ingsw.TextColors.RESET;

public class NotHisTurnException extends Exception {
    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public NotHisTurnException() {}

    /**
     * Prints this throwable and its backtrace to the
     * standard error stream.
     */
    @Override
    public void printStackTrace() {
        System.out.println(TextColors.colorText(RED_BRIGHT, "The Player can't do this action, it isn't his turn" ));
    }
}
