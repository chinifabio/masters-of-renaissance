package it.polimi.ingsw.model.exceptions.game;

import static it.polimi.ingsw.TextColors.*;

public class GameTypeException extends GameException{
    /**
     * msg to identify the error
     */
    private String msg;

    /**
     * costructor that save the message to handle
     * @param msg string
     */
    public GameTypeException(String msg) {
        super(msg);
    }

    /**
     * return the message handled
     * @return string of the message
     */
    public String getMsg(){
        return RED_BRIGHT + msg + RESET;
    }

}
