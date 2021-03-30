package it.polimi.ingsw.model.exceptions;

/**
 * exception thrown when wrong methon is invoked in a subclass of Loot
 */
public class LootTypeException extends Exception {
    /**
     * msg to identify the error
     */
    private String msg;

    /**
     * costructor that save the message to handle
     * @param msg string
     */
    public LootTypeException(String msg) {
        this.msg = msg;
    }

    /**
     * return the message handled
     * @return string of the message
     */
    public String getMsg(){
        return msg;
    }
}
