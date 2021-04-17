package it.polimi.ingsw.model.exceptions.game;

import static it.polimi.ingsw.TextColors.*;

public class GameConstructorException extends Exception {

    //TODO METTERE L'ECCEZIONE NEI COSTRUTTORI DELLE CLASSI PRINCIPALI
    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public GameConstructorException() {}

    /**
     * Prints this throwable and its backtrace to the
     * standard error stream.
     */
    @Override
    public void printStackTrace() {
        System.out.println(colorText(RED_BRIGHT, "Something went wrong in creating the game elements"));
    }
}
