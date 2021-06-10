package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.view.gui.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.function.Consumer;

public class AskPlayers extends GuiPanel {

    private Image background;

    public AskPlayers(GUI gui) {
        super(gui);

        InputStream is = getClass().getResourceAsStream("/LogoMasters.png");
        if (is == null) try {
            throw new IOException("LogoMasters.png not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            background = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.setPreferredSize(new Dimension(gui.width-300, gui.height));

        JPanel buttonPanel = new JPanel();

        LayoutManager layout = new BoxLayout(buttonPanel, BoxLayout.Y_AXIS);

        buttonPanel.setLayout(layout);

        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel request = new JLabel("Choose the desired number of players");
        request.setFont(new Font("Times New Roman", Font.ITALIC, 26));
        request.setForeground(new Color(220, 179, 120));

        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(2,2));
        JButton one = new JButton("1");
        JButton two = new JButton("2");
        JButton three = new JButton("3");
        JButton four = new JButton("4");

        one.addActionListener(e -> {
            gui.actionPerformed(e);
            gui.socket.send(new Packet(HeaderTypes.SET_PLAYERS_NUMBER, ChannelTypes.PLAYER_ACTIONS, one.getText()));
        });

        two.addActionListener(e -> gui.socket.send(new Packet(HeaderTypes.SET_PLAYERS_NUMBER, ChannelTypes.PLAYER_ACTIONS, two.getText())));

        three.addActionListener(e -> gui.socket.send(new Packet(HeaderTypes.SET_PLAYERS_NUMBER, ChannelTypes.PLAYER_ACTIONS, three.getText())));

        four.addActionListener(e -> gui.socket.send(new Packet(HeaderTypes.SET_PLAYERS_NUMBER, ChannelTypes.PLAYER_ACTIONS, four.getText())));

        buttons.add(one);
        buttons.add(two);
        buttons.add(three);
        buttons.add(four);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 550)));
        buttonPanel.add(request);
        buttonPanel.add(buttons);

        buttonPanel.setOpaque(false);
        this.add(buttonPanel);


    }

    @Override
    public void reactToPacket(Packet packet) throws IOException {
        switch (packet.header) {
            case RECONNECTED -> {
                gui.switchPanels(new PersonalBoardPanel(gui));
                gui.notifyPlayer(packet.body);
            }
            case INVALID -> {
                gui.notifyPlayerError(packet.body);
                gui.switchPanels(new AskPlayers(gui));
            }
            case GAME_INIT -> {
                gui.switchPanels(new InitGamePanel(gui));
                gui.notifyPlayer(packet.body);
            }
        }
    }


    /**
     * Draw the background
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = gui.width-300;
        int height = gui.height;
        g.drawImage(background, 0, 0,width,height, null);
    }
}