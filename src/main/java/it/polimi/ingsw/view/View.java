package it.polimi.ingsw.view;

import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.litemodel.litecards.LiteSoloActionToken;

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
     * Show an error to the player
     * @param errorMessage the error message
     */
    void notifyPlayerError(String errorMessage);

    /**
     * Show to the player the move performed by Lorenzo
     * @param token token to show
     */
    void popUpLorenzoMoves(LiteSoloActionToken token);

    /**
     * This method changes the state of the Client interface in the initial state
     */
    void fireGameCreator();

    /**
     * This method changes the state of the Client interface in the waiting lobby state
     */
    void fireLobbyWait();

    /**
     * This method changes the state of the Client interface in the initial phase of the game state
     */
    void fireGameInit();

    /**
     * This method changes the state of the Client interface in the main actions phase
     */
    void fireGameSession();

    /**
     * This method changes the state of the Client interface in the end game phase
     */
    void fireGameEnded();

    /**
     * This method changes the state of the Client interface in the leaderboard phase
     */
    void fireGameResult();

    /**
     * This method refresh the actual Client interface after a change
     */
    void refresh();

    /**
     * This method reads data when needed
     */
    void start();

    /**
     * return the liteModel of the view
     * @return the model of the view
     */
    LiteModel getModel();

    /**
     * This method close the application
     * @param message is the message that explain why the application will be closed
     */
    void emergencyExit(String message);
}
