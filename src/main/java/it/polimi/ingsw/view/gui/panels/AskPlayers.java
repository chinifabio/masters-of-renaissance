package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.panels.graphicComponents.BgJPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class AskPlayers extends GuiPanel {

    public AskPlayers(GUI gui) {
        super(gui);
    }

    @Override
    public JPanel update() throws IOException {
        JPanel result = new BgJPanel("/LogoMasters.png", GUI.gameWidth, GUI.gameHeight);

        result.setPreferredSize(new Dimension(GUI.gameWidth, GUI.gameHeight));

        JPanel buttonPanel = new JPanel();

        LayoutManager layout = new BoxLayout(buttonPanel, BoxLayout.Y_AXIS);

        buttonPanel.setLayout(layout);

        result.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel request = new JLabel("Choose the desired number of players");
        request.setFont(new Font("Times New Roman", Font.ITALIC, 26));
        request.setForeground(new Color(220, 179, 120));

        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(2,2));
        JButton one = new JButton("1");
        JButton two = new JButton("2");
        JButton three = new JButton("3");
        JButton four = new JButton("4");

        one.addActionListener(e -> gui.socket.send(new Packet(HeaderTypes.SET_PLAYERS_NUMBER, ChannelTypes.PLAYER_ACTIONS, one.getText())));

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
        result.add(buttonPanel);

        return result;
    }
}