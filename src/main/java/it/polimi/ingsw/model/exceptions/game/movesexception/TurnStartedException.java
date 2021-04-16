package it.polimi.ingsw.model.exceptions.game.movesexception;

public class TurnStartedException extends Exception{
    /**
     * message to handle
     */
    private String msg;

    /**
     * create an exception with a message
     * @param msg  message to handle
     */
    public TurnStartedException(String msg){
        this.msg = msg;
    }

    /**
     * Prints this throwable and its backtrace to the
     * standard error stream.
     */
    @Override
    public void printStackTrace() {
        System.out.println(msg);
    }
}
