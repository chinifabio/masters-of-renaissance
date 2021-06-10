package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.communication.Disconnectable;
import it.polimi.ingsw.communication.ServerReply;
import it.polimi.ingsw.communication.VirtualSocket;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.litemodel.LiteModelUpdater;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.view.Messanger;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.panels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class GUI extends JFrame implements View, Disconnectable, ActionListener {

    public static HashMap<ResourceType, String> resourceImages = new HashMap<>(){{
        put(ResourceType.COIN, "WarehouseRes/coin.png");
        put(ResourceType.SHIELD, "WarehouseRes/shield.png");
        put(ResourceType.STONE, "WarehouseRes/stone.png" );
        put(ResourceType.SERVANT, "WarehouseRes/servant.png");
        put(ResourceType.EMPTY,"WarehouseRes/empty.png");
        put(ResourceType.UNKNOWN,"WarehouseRes/unknown.png");
    }};

    public static Color borderColor = new Color(220,179,120);
    public final LiteModel model = new LiteModel();
    public final VirtualSocket socket;

    private final JPanel gamePanel = new LogoPanel(this);
    private final NotifyPanel notifyPanel = new NotifyPanel();

    public final int width = 1640;
    public final int height = 810;

    private GuiPanel actualPanel;

    /**
     * Tell something to the player
     *
     * @param message the message to show up to the player
     */
    @Override
    public void notifyPlayer(String message) {
        this.notifyPanel.appendMessage(message, Color.GRAY);
    }

    /**
     * Show to the player a server reply
     *
     * @param reply the reply to show to the player
     */
    public void notifyServerReply(ServerReply reply) {

    }

    /**
     * show an error to the player
     *
     * @param errorMessage the error message
     */
    @Override
    public void notifyPlayerError(String errorMessage) {
        notifyPanel.appendMessage(errorMessage, new Color(255, 110, 102));
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

    @Override
    public VirtualSocket getSocket() {
        return this.socket;
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
        new Thread(new LiteModelUpdater(socket, model)).start();
        new Thread(new Messanger(this)).start();
        socket.pinger(this);

        boolean gino = true;
        while (gino) {
            try {
                Packet received = socket.pollPacketFrom(ChannelTypes.PLAYER_ACTIONS);
                actualPanel.reactToPacket(received);
            } catch (IOException e) {
                notifyPlayerError(e.getMessage());
                gino = false;
            }
        }

        this.notifyPlayerError("QUITTING");
    }

    public GUI(String address, int port) throws IOException {
        super ("Master of Renaissance");
        
        socket = new VirtualSocket(new Socket(address, port));

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        gamePanel.setPreferredSize(new Dimension(width - 300, height));

        notifyPanel.setPreferredSize(new Dimension(300, height));

        add(gamePanel, BorderLayout.CENTER);
        add(notifyPanel, BorderLayout.LINE_END);

        actualPanel = new AskNickname(this);
        gamePanel.add(actualPanel);


        pack();
        setVisible(true);
    }

    public void switchPanels(GuiPanel toSee){
        synchronized (gamePanel) {
            gamePanel.remove(actualPanel);
            //toSee.setBorder(BorderFactory.createEmptyBorder(
            //        (height - toSee.getHeight())/2,
            //        (width - toSee.getWidth())/2,
            //        (height - toSee.getHeight())/2,
            //        (width - toSee.getWidth())/2
            //));
            gamePanel.add(toSee);
            actualPanel = toSee;

            gamePanel.repaint();
            gamePanel.revalidate();
        }
    }

    public static void main(String[] args){
        try {
            Thread client = new Thread(new GUI("localhost", 4444));
            client.start();
            client.join();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(-1);
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

    @Override
    public void handleDisconnection() {
        System.out.println("disconnected");
        System.exit(-1);
    }

    @Override
    public VirtualSocket disconnectableSocket() {
        return null;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "render leader":
                switchPanels(new LeaderPanel(this));
                break;
            case "render user request":
                switchPanels(new AskNickname(this));
                break;
        }
    }
}
