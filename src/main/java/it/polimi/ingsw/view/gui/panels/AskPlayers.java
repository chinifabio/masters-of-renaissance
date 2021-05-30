package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.view.gui.GUI;

import javax.swing.*;
import java.awt.*;

public class AskPlayers extends JPanel {
    public AskPlayers(GUI gui) {
        LayoutManager layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);

        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        setOpaque(false);

        JLabel request = new JLabel("Choose the desired number of players");
        request.setFont(new Font("Times New Roman", Font.ITALIC, 26));
        request.setForeground(new Color(220, 179, 120));

        JTextField textField = new JTextField(20);
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setFont(new Font("Times New Roman", Font.PLAIN, 18));
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

        add(Box.createRigidArea(new Dimension(0, 400)));
        add(request);
        add(textField);
    }
}