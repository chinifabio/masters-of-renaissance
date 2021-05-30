package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.communication.Disconnectable;
import it.polimi.ingsw.communication.ServerReply;
import it.polimi.ingsw.communication.VirtualSocket;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.litemodel.LiteModelUpdater;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.panels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;

public class GUI implements View, Disconnectable, ActionListener {


    public final LiteModel model = new LiteModel();
    public final VirtualSocket socket;

    private final JPanel gamePanel = new LogoPanel(this);
    private final JFrame gameWindow = new JFrame("Master of Renaissance");
    private final NotifyPanel notifyPanel = new NotifyPanel();

    public static final Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
    private GuiPanel actualPanel;

    /**
     * Tell something to the player
     *
     * @param message the message to show up to the player
     */
    @Override
    public void notifyPlayer(String message) {
        this.notifyPanel.appendMessage(message);
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

        gameWindow.setSize(screenDimension.width, screenDimension.height-18);
        actualPanel = new AskNickname(this);
        gamePanel.add(actualPanel);

        gameWindow.setSize(1920-380, 1080-230);

        boolean gino = true;
        while (gino) {
            try {
                actualPanel.reactToPacket(socket.pollPacketFrom(ChannelTypes.PLAYER_ACTIONS));
            } catch (IOException e) {
                notifyPlayerError(e.getMessage());
                gino = false;
            }
        }

        this.notifyPlayerError("QUITTING");
    }

    public GUI(String address, int port) throws IOException {
        socket = new VirtualSocket(new Socket(address, port));

        gameWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameWindow.setResizable(false);
        gameWindow.setLayout(new BorderLayout());

        gamePanel.setPreferredSize(new Dimension(screenDimension.width, screenDimension.height));
        notifyPanel.setPreferredSize(new Dimension(180, screenDimension.height));

        gameWindow.add(gamePanel);
        gameWindow.add(notifyPanel, BorderLayout.LINE_END);
    }

    public void switchPanels(GuiPanel toSee){
        gamePanel.remove(actualPanel);
        gamePanel.add(toSee);
        actualPanel = toSee;

        gamePanel.repaint();
        gamePanel.revalidate();
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
