package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.communication.Disconnectable;
import it.polimi.ingsw.communication.ServerReply;
import it.polimi.ingsw.communication.VirtualSocket;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.litemodel.LiteModelUpdater;
import it.polimi.ingsw.litemodel.litecards.LiteLeaderCard;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.panels.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.*;
import java.util.List;

public class GUI implements View, Disconnectable {


    private final LiteModel model = new LiteModel();
    public final VirtualSocket socket;

    public HashMap<String, JPanel> panelContainer = new HashMap<>();
    private final JPanel mainPanel = new LogoPanel();
    private final JFrame gameWindow = new JFrame("Master of Renaissance");

    private final Dimension screenDimension = new Dimension();


    /**
     * This method prints the current status of the FaithTrack
     */
    public void renderFaithTrack() {

    }

    /**
     * Render the a view of the market tray
     */
    public void renderMarketTray() {
        JPanel panelTemp = panelContainer.get("Market");

        //questa roba è orribile.
        JLabel labelTemp0 = new JLabel();
        JLabel labelTemp1 = new JLabel();
        JLabel labelTemp2 = new JLabel();
        String test0 = model.getTray().toString(0);
        String test1 = model.getTray().toString(1);
        String test2 = model.getTray().toString(2);
        labelTemp0.setText(test0);
        labelTemp1.setText(test1);
        labelTemp2.setText(test2);
        labelTemp0.setBounds(300,100,600,50);
        labelTemp1.setBounds(300,125,600,50);
        labelTemp2.setBounds(300,150,600,50);
        panelTemp.add(labelTemp0);
        panelTemp.add(labelTemp1);
        panelTemp.add(labelTemp2);


        //((JLabel) panelTemp.getComponent(0)).setText(model.getLeader(model.getMe()).toString());
        JButton back = new JButton("Return to PB");
        back.setBounds(20,20,150,40);
        back.addActionListener(e -> viewPanel("Homepage"));

        panelTemp.add(back);
        panelTemp.setLayout(null);
        viewPanel("Market");
    }

    /**
     * Render the personal board of a player
     *
     * @param nickname the player to show personal board
     */
    public void renderPersonalBoard(String nickname) {

    }

    /**
     * Render a view of the devSetup
     */
    public void renderDevSetup() {

    }

    /**
     * Render the homepage of the cli
     */
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
    public String askUser(String request) throws InterruptedException {
        //JPanel panelTemp = panelContainer.get("RequestPanel");
        //JLabel labelTemp = (JLabel) Arrays.stream(panelTemp.getComponents()).filter(p-> p.getName().equals("requestText")).findAny().get();
        //labelTemp.setText(request);
        viewPanel("RequestPanel");
        //synchronized (userInteractionWait){
        //    userInteractionWait.wait();
        //    synchronized (userInput){
        //        return userInput;
        //    }
        //}
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
    public void notifyServerReply(ServerReply reply) {
        JFrame popUp = new JFrame();
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
     * Render a view of the leader cards of the player
     */
    public void renderLeaderCards() {
        JPanel temp = panelContainer.get("Leader");
        temp.removeAll();
        int i = 20;
        //((JLabel) temp.getComponent(0)).setText(model.getLeader(model.getMe()).toString());
        temp.setLayout(null);
        JPanel tempCard;
        for (LiteLeaderCard card : model.getLeader(model.getMe())){
            tempCard = generateLeaderFromId(card.getCardID());
            tempCard.setBounds(i, 100, 462/2+5,380);
            temp.setVisible(true);
            temp.add(tempCard);
            i += 462/2+5+10;
        }

        JButton back = new JButton("Return to PB");
        back.setBounds(20,20,150,40);
        back.addActionListener(e -> viewPanel("Homepage"));
        temp.add(back);
        viewPanel("Leader");
    }

    /**
     * show an error to the player
     *
     * @param errorMessage the error message
     */
    @Override
    public void notifyPlayerError(String errorMessage) {
        JFrame popUp = new JFrame("Invalid!");
        JButton ok = new JButton("OK");
        JLabel text = new JLabel();
        text.setText(errorMessage);
        text.setBounds(100, 30, 400,50);
        ok.setBounds(100, 100,250,30);
        ok.setHorizontalAlignment(SwingConstants.CENTER);
        ok.addActionListener(e -> popUp.dispose());
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
     * notify a warning message to the player
     *
     * @param s the waring message
     */
    @Override
    public void notifyPlayerWarning(String s) {

    }

    /**
     * return the liteModel of the view
     *
     * @return the model of the view
     */
    @Override
    public LiteModel getModel() {
        return this.model;
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
        new Thread(socket).start();
        socket.pinger(this);
        new Thread(new LiteModelUpdater(socket, model)).start();

        gameWindow.setVisible(true);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.PAGE_END;

        mainPanel.add(new AskNickname(this), c);

        gameWindow.setSize(1920-380, 1080-230);
    }

    public GUI(String address, int port) throws IOException {
        socket = new VirtualSocket(new Socket(address, port));

        gameWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        gameWindow.setResizable(false);

        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setSize(screenDimension.width, screenDimension.height);
        gameWindow.add(mainPanel);

        //Panel for UserInput
        /*
        JPanel panel = new LogoPanel(this);

        //Panel for Homepage
        JPanel homepage = new PersonalBoardPanel(this
        );



        JPanel leader = new JPanel();
        leader.setName("Leader");
        leader.setBounds(0,0,1920-380,1080-230);
        //label.setBounds(10, 10, 300, 200);
        //leader.add(label);
        leader.setVisible(false);

        JPanel market = new JPanel();
        market.setName("Market");
        JLabel marketLabel = new JLabel();
        market.setBounds(0,0,1920-380,1080-230);
        marketLabel.setBounds(10, 10, 300, 200);
        market.add(marketLabel);
        market.setVisible(false);

        //panelContainer.put(panel.getName(), panel);
        //panelContainer.put(leader.getName(), leader);
        //panelContainer.put(homepage.getName(), homepage);
        //panelContainer.put(market.getName(), market);


        mainPanel.setBounds(0,0,1920-380,1080-230);;
        mainPanel.setLayout(null);
        for (JPanel panels : panelContainer.values()){
            mainPanel.add(panels);
        }
        */

        //gameWindow.setLayout(null);
    }

    public void switchPanels(JPanel toSee){
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
            Thread client = new Thread(new GUI("localhost", 4444));
            client.start();
            client.join();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

    private JPanel generateLeaderFromId(String name){
        JPanel panel = new JPanel();
        InputStream url = this.getClass().getResourceAsStream("/LeaderCardsImages/" + name + ".png");
        BufferedImage img = null;
        try {
            assert url != null;
            img = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Image scaledImage = getScaledImage(img, 462/2, 698/2);
        ImageIcon icon1 = new ImageIcon(scaledImage);
        JLabel label = new JLabel();
        label.setBounds(0,0,462/2,698/2);

        label.setIcon(icon1);

        JButton activate = new JButton("Activate");
        activate.setBounds(0, (698/2) + 5, 30, 20);
        activate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //synchronized (userInteractionWait){
                //    synchronized (userInput) {
                //        userInput = "activateleader" + " " + name.replace("LC", "");
                //    }
                //    userInteractionWait.notifyAll();
                //}
            }
        });

        JButton discard = new JButton("Discard");
        discard.setBounds(40, (698/2) + 5, 30, 20);
        discard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //synchronized (userInteractionWait){
                //    synchronized (userInput) {
                //        userInput = "discardleader" + " " + name.replace("LC", "");
                //    }
                //    userInteractionWait.notifyAll();
                //}
            }
        });



        label.setLayout(null);
        panel.add(label);
        panel.add(activate);
        panel.add(discard);
        panel.setLayout(null);
        panel.setVisible(true);
        return panel;
    }

    @Override
    public void handleDisconnection() {
        System.out.println("disconnected");
        System.exit(-1);
    }

    @Override
    public VirtualSocket disconnectableSocket() {
        return null;
    }
}
