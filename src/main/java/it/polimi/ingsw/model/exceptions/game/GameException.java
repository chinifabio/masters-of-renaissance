package it.polimi.ingsw.model.exceptions.game;

import static it.polimi.ingsw.TextColors.*;

public class GameException extends Exception{
    /**
     * msg to identify the error
     */
    private String msg;

    /**
     * costructor that save the message to handle
     * @param msg string
     */
    public GameException(String msg) {
        this.msg = msg;
    }

    /**
     * Prints this throwable and its backtrace to the
     * standard error stream.
     */
    @Override
    public void printStackTrace() {
        System.out.println((colorText(RED_BRIGHT, this.msg)));
    }
}
