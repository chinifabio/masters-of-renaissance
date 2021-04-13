package it.polimi.ingsw.model.exceptions;

import static it.polimi.ingsw.TextColors.RESET;
import static it.polimi.ingsw.TextColors.YELLOW;

public class NegativeResourcesDepotException extends Exception {
    /**
     * msg to identify the error
     */
    private String msg;

    /**
     * costructor that save the message to handle
     * @param msg string
     */
    public NegativeResourcesDepotException(String msg) {
        this.msg = msg;
    }

    /**
     * return the message handled
     * @return string of the message
     */
    public String getMsg(){
        return YELLOW + msg + RESET;
    }

}
