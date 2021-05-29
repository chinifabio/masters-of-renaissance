package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.view.gui.GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AskNickname extends JPanel {
    public AskNickname(GUI gui) {
        LayoutManager layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);

        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        setOpaque(false);

        JLabel request = new JLabel("Insert your username");

        JTextField textField = new JTextField(20);
        textField.addActionListener(e -> {
            gui.socket.send(new Packet(HeaderTypes.HELLO, ChannelTypes.PLAYER_ACTIONS, textField.getText()));

            Packet response = gui.socket.pollPacketFrom(ChannelTypes.PLAYER_ACTIONS);
            switch (response.header) {
                case SET_PLAYERS_NUMBER:
                    gui.switchPanels(new PersonalBoardPanel(gui));
                    break;
                case JOIN_LOBBY:
                    break;

                case INVALID:
                    gui.notifyPlayerError(response.body);
                    textField.setText("");
                    break;
            }
        });

        add(request);
        add(textField);
    }
}
