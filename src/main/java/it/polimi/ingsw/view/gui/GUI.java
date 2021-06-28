package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.communication.SocketListener;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.FaithPointCheatCommand;
import it.polimi.ingsw.communication.packet.commands.ResourceCheatCommand;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.litemodel.litecards.LiteSoloActionToken;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.view.ClientPacketHandler;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.panels.*;
import it.polimi.ingsw.view.gui.panels.graphicComponents.BgJPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Objects;

/**
 * This is the class that manages the Graphical User Interface
 */
public class GUI extends JFrame implements View {

    /**
     * This attribute is the map that connects the Resource type with its image
     */
    public static HashMap<ResourceType, String> resourceImages = new HashMap<>(){{
        put(ResourceType.COIN, "WarehouseRes/coin.png");
        put(ResourceType.SHIELD, "WarehouseRes/shield.png");
        put(ResourceType.STONE, "WarehouseRes/stone.png" );
        put(ResourceType.SERVANT, "WarehouseRes/servant.png");
        put(ResourceType.EMPTY,"WarehouseRes/empty.png");
        put(ResourceType.UNKNOWN,"WarehouseRes/unknown.png");
    }};

    /**
     * This attribute is the color of the border in the background image
     */
    public static Color borderColor = new Color(220,179,120);

    /**
     * This attribute is the Model of the match
     */
    public final LiteModel model = new LiteModel();

    /**
     * This attribute is the SocketListener that manage the packets
     */
    public final SocketListener socket;

    /**
     * This attribute is the panel where the user can interact with the game
     */
    private final JPanel gamePanel = new BgJPanel("/LogoMasters.png", gameWidth, gameHeight);

    /**
     * This attribute is the panel where the player is notified during the match
     */
    private final NotifyPanel notifyPanel = new NotifyPanel();

    /**
     * This attribute is the width of the application window
     */
    public static final int width = 1640;

    /**
     * This attribute is the height of the application window
     */
    public static final int height = 810;

    /**
     * This attribute is the width of the gamePanel
     */
    public static final int gameWidth = 1340;

    /**
     * This attribute is the height of the gamePanel
     */
    public static final int gameHeight = 810;

    /**
     * This attribute indicates the actual Panel of the GamePanel
     */
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
     * This method show to the Player the Lorenzo move in the SinglePlayer Mode
     * @param token token to show
     */
    @Override
    public void popUpLorenzoMoves(LiteSoloActionToken token) {
        try {
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/SoloActionTokenImages/" + token + ".png")));
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

    /**
     * This method change the current panel to the InitGamePanel when the match starts
     */
    @Override
    public void fireGameInit() {
        switchPanels(new InitGamePanel(this));
    }

    /**
     * This method change the current panel to the PersonalBoardPanel
     */
    @Override
    public void fireGameSession() {
        switchPanels(new PersonalBoardPanel(this));
    }

    /**
     * This method change the current panel to the WaitingEndGamePanel when a player ends his match and has to wait the other players
     */
    @Override
    public void fireGameEnded() {
        switchPanels(new WaitingEndGamePanel(this));
    }

    /**
     * This method change the current panel to the ScoreboardPanel when the game ends
     */
    @Override
    public void fireGameResult() {
        switchPanels(new ScoreboardPanel(this));
    }

    /**
     * This method close the application
     * @param message is the message that explain why the application will be closed
     */
    @Override
    public void emergencyExit(String message) {
        JOptionPane.showMessageDialog(null, message);
        System.exit(0);
    }

    /**
     * This method change the current panel to the AskPlayers panel at the beginning of the game
     */
    @Override
    public void fireGameCreator() {
        switchPanels(new AskPlayers(this));
    }

    /**
     * This method change the current panel to the LoadingPanel when the player has to wait for other players to connect to the game
     */
    @Override
    public void fireLobbyWait() {
        switchPanels(new LoadingPanel(this));
    }

    /**
     * This is the constructor of the class
     * @param address is the IP address
     * @param port is the port of the Server
     * @throws IOException if an I/O error occurs when creating the socket.
     */
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

        setFocusable(true);
        addKeyListener(new KeyCheats(this));

        pack();
        setVisible(true);
    }

    /**
     * Read data from command line when needed
     */
    @Override
    public void start() {
        new Thread(socket).start();
        new ClientPacketHandler(this, socket).start();
    }

    /**
     * This method change the current panel with the one passed
     * @param panelGenerator is the new Panel to show
     */
    public synchronized void switchPanels(GuiPanel panelGenerator){
        actualPanel = panelGenerator;
        refresh();
    }

    /**
     * This method recreate the Panel after a change
     */
    @Override
    public synchronized void refresh() {
        try {
            JPanel newPanel = actualPanel.update();
            gamePanel.removeAll();
            gamePanel.add(newPanel);
            gamePanel.repaint();
            gamePanel.revalidate();
        } catch (Exception | Error e) {
            emergencyExit("You missed some resource" + e.getMessage());
        }
    }

    public static void main(String[] args){
        try {
            new GUI("localhost", 4444).start();
        } catch (Exception e) {
            //System.out.println(e.getMessage());
            System.out.println("Unable to start the GUI, check the connection to the server or start the CLI");
        }
    }

    /**
     * This method change the passed image size
     * @param srcImg is the image that will be scaled
     * @param w is the new width of the image
     * @param h is the new height of the image
     * @return the image with new dimension
     */
    public static Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }
}

/**
 * This class introduce the cheats to the game
 */
class KeyCheats implements KeyListener {

    /**
     * This attribute is the GUI that contains all the needed info
     */
    private final GUI gui;

    /**
     * This is the constructor of the class
     * @param gui is the GUI that contains all the needed info
     */
    public KeyCheats(GUI gui) {
        this.gui=gui;
    }

    /**
     * This method react to a keyboard's button pressed to generate cheat
     * @param e is the keyboard event
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_F4) {
            gui.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new FaithPointCheatCommand(1).jsonfy()));
        }
        if (key == KeyEvent.VK_F5) {
            gui.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new FaithPointCheatCommand(5).jsonfy()));
        }
        if(key == KeyEvent.VK_F6) {
            gui.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new ResourceCheatCommand().jsonfy()));
        }
    }

    /**
     * This method do nothing
     */
    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * This method do nothing
     */
    @Override
    public void keyReleased(KeyEvent e) {}
}