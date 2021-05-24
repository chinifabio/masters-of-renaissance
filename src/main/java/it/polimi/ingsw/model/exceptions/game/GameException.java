package it.polimi.ingsw.model.exceptions.game;

import static it.polimi.ingsw.TextColors.*;

public class GameException extends Exception{

    /**
     * costructor that save the message to handle
     * @param msg string
     */
    public GameException(String msg) {
        super(msg);
    }

    /**
     * Prints this throwable and its backtrace to the
     * standard error stream.
     */
    @Override
    public void printStackTrace() {
        System.out.println(super.getMessage());
    }
}
