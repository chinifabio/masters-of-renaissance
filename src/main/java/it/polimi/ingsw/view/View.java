package it.polimi.ingsw.view;

import it.polimi.ingsw.model.player.Player;

public interface View {

    void showFaithTrack();

    void showMarketTray();

    void showPersonalBoard(Player player);

    void showDevSetup();

    void askToPlayer();

}
