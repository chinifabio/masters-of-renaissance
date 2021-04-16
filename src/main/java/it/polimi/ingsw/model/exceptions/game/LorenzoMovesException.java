package it.polimi.ingsw.model.exceptions.game;

import it.polimi.ingsw.TextColors;

public class LorenzoMovesException extends GameException{
    /**
     * msg to identify the error
     */
    private String msg;

    /**
     * costructor that save the message to handle
     * @param msg string
     */
    public LorenzoMovesException(String msg) {
        super(msg);
    }

    /**
     * return the message handled
     * @return string of the message
     */
    public String getMsg(){
        return TextColors.YELLOW + msg + TextColors.RESET;
    }
}
