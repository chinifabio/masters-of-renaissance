package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.view.litemodel.LiteFaithTrack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CLI implements View {

    public void test() throws IOException {
        LiteFaithTrack faith = new LiteFaithTrack();

        FaithTrackPrinter printer = new FaithTrackPrinter(faith);
        Random numbers = new Random();
        List<String> names = new ArrayList<>();
        names.add("Vinz");
        names.add("Cini");
        names.add("LastBuddy");
        names.add("Test");

        List <Integer> position = new ArrayList<>();
        for (String name : names){
            position.add(numbers.nextInt(24));
        }

        printer.printFaithTrack(names, position);
    }

    @Override
    public void showFaithTrack() {

    }

    @Override
    public void showMarketTray() {

    }

    @Override
    public void showPersonalBoard(Player player) {

    }

    @Override
    public void showDevSetup() {

    }

    @Override
    public void askToPlayer() {

    }
}
