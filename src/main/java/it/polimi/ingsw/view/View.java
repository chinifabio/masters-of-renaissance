package it.polimi.ingsw.view;

import it.polimi.ingsw.litemodel.LiteModel;

/**
 * This interface is the View
 */
public interface View extends Runnable{

    /**
     * Tell something to the player
     * @param message the message to show up to the player
     */
    void notifyPlayer(String message);

    /**
     * show an error to the player
     * @param errorMessage the error message
     */
    void notifyPlayerError(String errorMessage);

    /**
     * notify a warning message to the player
     * @param s the waring message
     */
    void notifyPlayerWarning(String s);

    /**
     * return the liteModel of the view
     * @return the model of the view
     */
    LiteModel getModel();
}
