package it.polimi.ingsw.view;

import it.polimi.ingsw.model.player.Player;

/**
 * This interface is the View
 */
public interface View {

    /**
     * This method prints the current status of the FaithTrack
     */
    void showFaithTrack();

    void showMarketTray();

    void showPersonalBoard(Player player);

    void showDevSetup();

    void askToPlayer();

    void clearScreen();

}
