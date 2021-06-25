package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.communication.SocketListener;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.view.ClientPacketHandler;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.panels.*;
import it.polimi.ingsw.view.gui.panels.graphicComponents.BgJPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Objects;

public class GUI extends JFrame implements View {

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

    @Override
    public void popUpLorenzoMoves() {
        try {
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/SoloActionTokenImages/" + model.getSoloToken().getCardID() + ".png")));
            JOptionPane.showMessageDialog(
                    null,
                    null,
                    "Lorenzo moves", JOptionPane.INFORMATION_MESSAGE,
                    icon);
        } catch (NullPointerException e) {
            notifyPlayerError("No token to visualize");
        }
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
        switchPanels(new WaitingEndGamePanel(this));
    }

    @Override
    public void fireGameResult() {
        switchPanels(new ScoreboardPanel(this));
    }

    @Override
    public void emergencyExit(String message) {
        JOptionPane.showMessageDialog(null, message);
        System.exit(0);
    }

    @Override
    public void fireGameCreator() {
        switchPanels(new AskPlayers(this));
    }

    @Override
    public void fireLobbyWait() {
        switchPanels(new LoadingPanel(this));
    }

    public GUI(String address, int port) throws IOException {
        super ("Master of Renaissance");
        
        socket = new SocketListener(new Socket(address, port));

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

    @Override
    public void start() {
        new Thread(socket).start();
        new ClientPacketHandler(this, socket).start();
    }

    public void switchPanels(GuiPanel toSee){
        synchronized (gamePanel) {
            gamePanel.removeAll();
            actualPanel = toSee;

            try {
                gamePanel.add(toSee.update());
            } catch (Exception | Error e) {
                e.printStackTrace();
                emergencyExit("You missed some resource " + e.getMessage());
            }

            gamePanel.repaint();
            gamePanel.revalidate();
        }
    }

    @Override
    public void refresh() {
        gamePanel.removeAll();

        try {
            gamePanel.add(actualPanel.update());
        } catch (Exception | Error e) {
            emergencyExit("You missed some resource" + e.getMessage());
        }

        gamePanel.repaint();
        gamePanel.revalidate();
    }

    public static void main(String[] args){
        try {
            new GUI("localhost", 4444).start();
        } catch (Exception e) {
            //System.out.println(e.getMessage());
            System.out.println("Unable to start the GUI, check the connection to the server or start the CLI");
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
}
