package it.polimi.ingsw.view;

import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.model.player.Player;

import java.io.IOException;

/**
 * This interface is the View
 */
public interface View {

    /**
     * This method prints the current status of the FaithTrack
     */
    void showFaithTrack(LiteModel model) throws IOException;

    void showMarketTray();

    void showPersonalBoard(Player player);

    void showDevSetup();

    void askToPlayer();

    static void clearScreen() {}

}
