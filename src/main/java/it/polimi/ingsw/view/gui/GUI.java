package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.communication.ServerReply;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.panels.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GUI implements View {


    private final JPanel mainPanel = new JPanel();
    private final ArrayList<JPanel> panels = new ArrayList<>();

    /**
     * This method prints the current status of the FaithTrack
     */
    @Override
    public void renderFaithTrack() {

    }

    /**
     * Render the a view of the market tray
     */
    @Override
    public void renderMarketTray() {

    }

    /**
     * Render the personal board of a player
     *
     * @param nickname the player to show personal board
     */
    @Override
    public void renderPersonalBoard(String nickname) {

    }

    /**
     * Render a view of the devSetup
     */
    @Override
    public void renderDevSetup() {

    }

    /**
     * Render the homepage of the cli
     */
    @Override
    public void renderHomePage() {

    }

    /**
     * Ask to the player something
     *
     * @param request the message to show
     * @return the input string submitted by the player
     */
    @Override
    public List<String> pollData(String request) throws InterruptedException {
        switchPanels(mainPanel, panels.get(0));
        return null;
    }

    /**
     * Tell something to the player
     *
     * @param message the message to show up to the player
     */
    @Override
    public void notifyPlayer(String message) {

    }

    /**
     * Show to the player a server reply
     *
     * @param reply the reply to show to the player
     */
    @Override
    public void notifyServerReply(ServerReply reply) {

    }

    /**
     * Save the lite model passed
     *
     * @param model the model to save
     */
    @Override
    public void receiveModel(LiteModel model) {

    }

    /**
     * Render a view of the leader cards of the player
     */
    @Override
    public void renderLeaderCards() {

    }

    /**
     * show an error to the player
     *
     * @param errorMessage the error message
     */
    @Override
    public void notifyPlayerError(String errorMessage) {

    }

    /**
     * Render a view of the warehouse
     *
     * @param nickname
     */
    @Override
    public void renderWarehouse(String nickname) {

    }

    /**
     * notify a warning message to the player
     *
     * @param s the waring message
     */
    @Override
    public void notifyPlayerWarning(String s) {

    }

    /**
     * Render a list of available commands
     */
    @Override
    public void renderHelp() {

    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

    }

    public GUI() {

        JFrame gameWindow = new JFrame();

        panels.add(new LogoPanel());
        panels.add(new FaithTrackPanel());
        panels.add(new CardsGridPanel());
        panels.add(new MarketTrayPanel());
        panels.add(new WarehousePanel());
        panels.add(new PersonalBoardPanel());

        for (JPanel panel : panels){
            mainPanel.add(panel);
        }

        gameWindow.add(mainPanel);
        gameWindow.setSize(1920-380, 1080-230);
        gameWindow.setLayout(null);
        gameWindow.setVisible(true);
    }

    public static void switchPanels(JPanel mainPanel , JPanel toSee){
        mainPanel.removeAll();
        mainPanel.add(toSee);
        mainPanel.repaint();
        mainPanel.revalidate();
    }
}
