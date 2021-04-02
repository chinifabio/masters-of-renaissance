package it.polimi.ingsw.model.exceptions;

/**
 * exception thrown when someone tries to draw or discard a card from an empty deck
 */
public class EmptyDeckException extends Exception{
    /**
     * msg to identify the error
     */
    private String msg;
    /**
     * costructor that save the message to handle
     * @param msg string
     */
    public EmptyDeckException(String msg){this.msg = msg;}

    /**
     * return the message handled
     * @return string of the message
     */
    public String getMsg(){return msg;}
    }