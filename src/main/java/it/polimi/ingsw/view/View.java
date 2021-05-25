package it.polimi.ingsw.view;

import it.polimi.ingsw.client.Actions;
import it.polimi.ingsw.client.InputHandler;
import it.polimi.ingsw.communication.ServerReply;
import it.polimi.ingsw.litemodel.LiteModel;

import java.util.List;

/**
 * This interface is the View
 */
public interface View extends Runnable{

    /**
     * This method prints the current status of the FaithTrack
     */
    void renderFaithTrack();

    /**
     * Render the a view of the market tray
     */
    void renderMarketTray();

    /**
     * Render the personal board of a player
     * @param nickname the player to show personal board
     */
    void renderPersonalBoard(String nickname);

    /**
     * Render a view of the devSetup
     */
    void renderDevSetup();

    /**
     * Render the homepage of the cli
     */
    void renderHomePage();

    /**
     * Ask to the player something
     * @param request the message to show
     * @return the input string submitted by the player
     */
    List<String> pollData(String request) throws InterruptedException;

    /**
     * Tell something to the player
     * @param message the message to show up to the player
     */
    void notifyPlayer(String message);

    /**
     * Show to the player a server reply
     * @param reply the reply to show to the player
     */
    void notifyServerReply(ServerReply reply);

    /**
     * Save the lite model passed
     * @param model the model to save
     */
    void receiveModel(LiteModel model);

    /**
     * Render a view of the leader cards of the player
     */
    void renderLeaderCards();

    /**
     * show an error to the player
     * @param errorMessage the error message
     */
    void notifyPlayerError(String errorMessage);

    /**
     * Render a view of the warehouse
     */
    void renderWarehouse(String nickname);

    /**
     * notify a warning message to the player
     * @param s the waring message
     */
    void notifyPlayerWarning(String s);
}
