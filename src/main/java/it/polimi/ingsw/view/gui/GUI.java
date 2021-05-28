package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.communication.ServerReply;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.gui.panels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GUI implements View {


    private LiteModel model;
    public HashMap<String, JPanel> panelContainer = new HashMap<>();
    public final Object userInteractionWait = new Object();
    public String userInput = "";
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
        mainPanel.setBackground(Color.gray);
        viewPanel("Homepage");
    }

    /**
     * Ask to the player something
     *
     * @param request the message to show
     * @return the input string submitted by the player
     */
    @Override
    public List<String> pollData(String request) throws InterruptedException {
        synchronized (userInteractionWait){
            userInteractionWait.wait();
            synchronized (userInput){
                return Arrays.asList(userInput.split(" "));
            }
        }
    }


    /**
     * Ask to the player something
     *
     * @param request the message to show
     * @return the input string submitted by the player
     */
    @Override
    public String askUser(String request) throws InterruptedException {
        viewPanel("RequestPanel");
        synchronized (userInteractionWait){
            userInteractionWait.wait();
            synchronized (userInput){
                return userInput;
            }
        }
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
        JFrame popUp = new JFrame();
        popUp.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JButton ok = new JButton("OK");
        JLabel text = new JLabel();
        text.setText(reply.obtainMessage());
        text.setBounds(100, 30, 400,50);
        ok.setBounds(100, 100,250,30);
        ok.setHorizontalAlignment(SwingConstants.CENTER);
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                popUp.dispose();
            }
        });
        text.setVisible(true);

        popUp.setForeground(new Color(50,50,50));
        popUp.setSize(500, 200);
        popUp.setResizable(false);

        popUp.add(text);
        popUp.add(ok);
        popUp.setLayout(null);
        popUp.setVisible(true);
    }

    /**
     * Save the lite model passed
     *
     * @param model the model to save
     */
    @Override
    public void receiveModel(LiteModel model) {
        this.model = model;
    }

    /**
     * Render a view of the leader cards of the player
     */
    @Override
    public void renderLeaderCards() {
        JPanel temp = panelContainer.get("Leader");
        ((JLabel) temp.getComponent(0)).setText(model.getLeader(model.getMe()).toString());

        viewPanel("Leader");
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
        //Panel for UserInput
        JPanel panel = new LogoPanel();
        panel.setName("RequestPanel");
        JTextField textArea = new JTextField(50);
        textArea.setBounds(650,650,200,20);
        textArea.setHorizontalAlignment(SwingConstants.CENTER);

        textArea.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (userInteractionWait){
                    synchronized (userInput) {
                        userInput = textArea.getText();
                        textArea.setText("");
                    }
                    userInteractionWait.notifyAll();
                }
            }
        });
        panel.setBounds(0,0,1920-380,1080-230);
        panel.setBackground(Color.gray);
        panel.setLayout(null);
        textArea.setVisible(true);
        panel.add(textArea, BorderLayout.SOUTH);


        //Panel for Homepage
        JPanel homepage = new PersonalBoardPanel();
        homepage.setName("Homepage");
        JButton viewLeader = new JButton("Show Leader Cards");
        viewLeader.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (userInteractionWait){
                    synchronized (userInput) {
                        userInput = "leadercards";
                    }
                    userInteractionWait.notifyAll();
                }
            }
        });
        viewLeader.setBounds(1350, 50, 150, 50);
        homepage.setBounds(0,0,1920-380,1080-230);
        homepage.setLayout(null);
        homepage.add(viewLeader);
        homepage.setVisible(false);


        JPanel leader = new JPanel();
        leader.setName("Leader");
        JLabel label = new JLabel();
        leader.setBounds(0,0,1920-380,1080-230);
        label.setBounds(10, 10, 300, 200);
        leader.add(label);
        leader.setVisible(false);

        panelContainer.put(panel.getName(), panel);
        panelContainer.put(leader.getName(), leader);
        panelContainer.put(homepage.getName(), homepage);


        mainPanel.setBounds(0,0,1920-380,1080-230);;
        mainPanel.setLayout(null);
        for (JPanel panels : panelContainer.values()){
            mainPanel.add(panels);
        }

        gameWindow.setResizable(false);
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

    public void viewPanel(String string){
        System.out.println("--------------------");
        for (JPanel p : panelContainer.values()){
            System.out.println(p.getName().equals(string));
            p.setVisible(p.getName().equals(string));
        }
    }

    public static void main(String[] args){
        try {
            GUI gui  = new GUI();
            Thread client = new Thread(new Client(gui));
            client.setDaemon(true);
            client.start();
            client.join();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
