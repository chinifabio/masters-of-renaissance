package it.polimi.ingsw.view;

import it.polimi.ingsw.litemodel.LiteModel;

/**
 * This interface is the View
 */
public interface View {

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

    void popUpLorenzoMoves();

    void fireGameCreator();
    void fireLobbyWait();
    void fireGameInit();
    void fireGameSession();
    void fireGameEnded();
    void fireGameResult();

    void refresh();

    void start();

    /**
     * return the liteModel of the view
     * @return the model of the view
     */
    LiteModel getModel();

    void emergencyExit(String message);
}
