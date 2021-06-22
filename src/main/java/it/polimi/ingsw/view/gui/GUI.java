package it.polimi.ingsw.view.gui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.communication.Disconnectable;
import it.polimi.ingsw.communication.SocketListener;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.rendering.Lighter;
import it.polimi.ingsw.communication.packet.updates.Updater;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.cli.Colors;
import it.polimi.ingsw.view.gui.panels.*;
import it.polimi.ingsw.view.gui.panels.graphicComponents.BgJPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class GUI extends JFrame implements View, Disconnectable {

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
    public final SocketListener socket;

    private final JPanel gamePanel = new BgJPanel("/LogoMasters.png", gameWidth, gameHeight);
    private final NotifyPanel notifyPanel = new NotifyPanel();

    public static final int width = 1640;
    public static final int height = 810;

    public static final int gameWidth = 1340;
    public static final int gameHeight = 810;

    private boolean activeClient = true;

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
     * show an error to the player
     *
     * @param errorMessage the error message
     */
    @Override
    public void notifyPlayerError(String errorMessage) {
        notifyPanel.appendMessage(errorMessage, new Color(255, 110, 102));
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
    public void fireGameInit() {
        switchPanels(new InitGamePanel(this));
    }

    @Override
    public void fireGameSession() {
        switchPanels(new PersonalBoardPanel(this));
    }

    @Override
    public void fireGameEnded() {
        JOptionPane.showMessageDialog(null, "work in progress");
    }

    @Override
    public void fireGameResult() {
        JOptionPane.showMessageDialog(null, "work in progress");
    }

    @Override
    public void fireGameCreator() {
        switchPanels(new AskPlayers(this));
    }

    @Override
    public void fireLobbyWait() {
        switchPanels(new LoadingPanel(this));
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
    public void start() {
        new Thread(socket).start();
        // todo can be changed implementing .execute(View view) in Packet, so we can avoid switch statement
        while (activeClient) {
            Packet received = socket.pollPacket();
            switch (received.channel) {
                case MESSENGER -> notifyPlayer(received.body);

                case PLAYER_ACTIONS -> {
                    // todo send with OK header the new model
                    if (received.header != HeaderTypes.INVALID) {
                        notifyPlayer(received.body);
                        updatePanel();
                    } else
                        notifyPlayerError(received.body);
                }

                case UPDATE_LITE_MODEL -> {
                    try {
                        Updater up = new ObjectMapper().readerFor(Updater.class).readValue(received.body);
                        up.update(this.model);
                        //updatePanel(); todo de comment when model will send all the changes in only one packet
                        // todo in the OK packet there is a serialized "image" of the match
                    } catch (JsonProcessingException e) {
                        System.out.println(Colors.color(Colors.RED, "update view error: ") + e.getMessage());
                    }
                }

                case RENDER_CANNON -> {
                    try {
                        Lighter ammo = new ObjectMapper().readerFor(Lighter.class).readValue(received.body);
                        ammo.fire(this);
                    } catch (JsonProcessingException e) {
                        System.out.println(Colors.color(Colors.RED, "render cannon error: ") + e.getMessage());
                    }
                }

                case CONNECTION_STATUS -> {}
            }
        }

    }

    public GUI(String address, int port) throws IOException {
        super ("Master of Renaissance");
        
        socket = new SocketListener(new Socket(address, port), this);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        gamePanel.setPreferredSize(new Dimension(width - 300, height));

        JScrollPane scrollPane = new JScrollPane(notifyPanel);
        scrollPane.setPreferredSize(new Dimension(300, height));

        add(gamePanel, BorderLayout.CENTER);

        JPanel scrollContainer = new JPanel();
        scrollContainer.add(scrollPane);
        scrollContainer.setMinimumSize(new Dimension(NotifyPanel.width, height));
        add(scrollPane, BorderLayout.LINE_END);

        actualPanel = new AskNickname(this);
        gamePanel.add(actualPanel.update());

        pack();
        setVisible(true);
    }

    public void switchPanels(GuiPanel toSee){
        synchronized (gamePanel) {
            gamePanel.removeAll(); // todo can cause problems
            actualPanel = toSee;

            try {
                gamePanel.add(toSee.update());
            } catch (IOException e) {
                // todo break the game
                e.printStackTrace();
            }

            gamePanel.repaint();
            gamePanel.revalidate();
        }
    }

    public void updatePanel() {
        gamePanel.removeAll(); // todo can cause problems

        try {
            gamePanel.add(actualPanel.update());
        } catch (IOException e) {
            // todo break the game
            e.printStackTrace();
        }

        gamePanel.repaint();
        gamePanel.revalidate();
    }

    public static void main(String[] args){
        try {
            new GUI("localhost", 4444).start();
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




    @Override
    public void handleDisconnection() {
        activeClient = false;
        JOptionPane.showMessageDialog(null, "Connection lost :(");
    }
}
