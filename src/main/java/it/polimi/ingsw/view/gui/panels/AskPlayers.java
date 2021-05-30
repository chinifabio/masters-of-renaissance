package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.view.gui.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class AskPlayers extends JPanel {
    public AskPlayers(GUI gui) {
        LayoutManager layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);

        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        setOpaque(false);


        JLabel request = new JLabel("Choose the desired number of players");
        request.setFont(new Font("Times New Roman", Font.ITALIC, 26));
        request.setForeground(new Color(220, 179, 120));

        /*
        JTextField textField = new JTextField(20);
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setFont(new Font("Times New Roman", Font.PLAIN, 18));

         */
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(2,2));
        JButton one = new JButton("1");
        JButton two = new JButton("2");
        JButton three = new JButton("3");
        JButton four = new JButton("4");

        Consumer<ActionEvent> b1 = e -> gui.socket.send(new Packet(HeaderTypes.SET_PLAYERS_NUMBER, ChannelTypes.PLAYER_ACTIONS, one.getText()));
        one.addActionListener(e -> gui.socket.send(new Packet(HeaderTypes.SET_PLAYERS_NUMBER, ChannelTypes.PLAYER_ACTIONS, one.getText())));

        Consumer<ActionEvent> b2 = e -> gui.socket.send(new Packet(HeaderTypes.SET_PLAYERS_NUMBER, ChannelTypes.PLAYER_ACTIONS, two.getText()));
        one.addActionListener(e -> gui.socket.send(new Packet(HeaderTypes.SET_PLAYERS_NUMBER, ChannelTypes.PLAYER_ACTIONS, two.getText())));

        Consumer<ActionEvent> b3 = e -> gui.socket.send(new Packet(HeaderTypes.SET_PLAYERS_NUMBER, ChannelTypes.PLAYER_ACTIONS, three.getText()));
        one.addActionListener(e -> gui.socket.send(new Packet(HeaderTypes.SET_PLAYERS_NUMBER, ChannelTypes.PLAYER_ACTIONS, three.getText())));

        Consumer<ActionEvent> b4 = e -> gui.socket.send(new Packet(HeaderTypes.SET_PLAYERS_NUMBER, ChannelTypes.PLAYER_ACTIONS, four.getText()));
        one.addActionListener(e -> gui.socket.send(new Packet(HeaderTypes.SET_PLAYERS_NUMBER, ChannelTypes.PLAYER_ACTIONS, four.getText())));


        /*
        textField.addActionListener(e -> {
            gui.socket.send(new Packet(HeaderTypes.SET_PLAYERS_NUMBER, ChannelTypes.PLAYER_ACTIONS, textField.getText()));

            Packet response = gui.socket.pollPacketFrom(ChannelTypes.PLAYER_ACTIONS);
            switch (response.header) {
                case OK:
                    gui.switchPanels(new PersonalBoardPanel(gui));
                    break;
                case JOIN_LOBBY:
                    gui.switchPanels(new PersonalBoardPanel(gui));
                    break;
                case INVALID:
                    gui.notifyPlayerError(response.body);
                    textField.setText("");
                    break;
            }

        });

         */

        buttons.add(one);
        buttons.add(two);
        buttons.add(three);
        buttons.add(four);
        add(Box.createRigidArea(new Dimension(0, 550)));
        add(request);
        add(buttons);


    }
}