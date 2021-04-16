package it.polimi.ingsw.model.exceptions.game.movesexception;

import static it.polimi.ingsw.TextColors.RESET;
import static it.polimi.ingsw.TextColors.YELLOW;

/**
 * Used to handle the error if someone do an illegal moves.
 * For example if someone in the same turn buy two dev Cards
 */
public class MainActionDoneException extends Exception{
    /**
     * handle a message to debug the error
     */
    private String msg;

    /**
     * construct the exception with the message to handle
     * @param m the message
     */
    public MainActionDoneException(String m){
        this.msg = m;
    }

    /**
     * print to the console the error message
     */
    public void print() {
        System.out.println(YELLOW + "Illegal move: " + msg + RESET);
    }
}
