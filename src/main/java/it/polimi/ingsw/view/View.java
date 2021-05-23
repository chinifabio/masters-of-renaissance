package it.polimi.ingsw.view;

import it.polimi.ingsw.communication.ServerReply;
import it.polimi.ingsw.litemodel.LiteModel;

/**
 * This interface is the View
 */
public interface View {

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
     * Ask to the player something
     * @param request the message to show
     * @return the input string submitted by the player
     */
    String askToPlayer(String request);

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
     * return the locking object on which synchronize to print
     * @return the lock object
     */
    Object obtainLock();

}
