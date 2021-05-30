package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.view.gui.GUI;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class AskNickname extends GuiPanel {

    public AskNickname(GUI gui) {
        super(gui);

        LayoutManager layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);

        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel request = new JLabel("Insert your username");
        request.setFont(new Font("Times New Roman",Font.ITALIC,26));
        request.setForeground(new Color(220,179,120));

        JTextField textField = new JTextField(20);
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setFont(new Font("Times New Roman",Font.PLAIN,18));
        textField.addActionListener(e -> {
            gui.model.setMyNickname(textField.getText());
            gui.socket.send(new Packet(HeaderTypes.HELLO, ChannelTypes.PLAYER_ACTIONS, textField.getText()));
        });

        add(Box.createRigidArea(new Dimension(0,500)));

        add(request);
        add(textField);

        setOpaque(false);
    }

    @Override
    public void reactToPacket(Packet packet) throws IOException {
        System.out.println(packet);
        switch (packet.header) {
            case JOIN_LOBBY -> gui.switchPanels(new PersonalBoardPanel(gui));
            case SET_PLAYERS_NUMBER -> gui.switchPanels(new AskPlayers(gui));
            case INVALID -> gui.notifyPlayerError(packet.body);
        }
    }

}
