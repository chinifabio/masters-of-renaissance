package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.panels.graphicComponents.BgJPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * This class is the GUI Panel where the Player can inserts his Nickname
 */
public class AskNickname extends GuiPanel {

    /**
     * This is the constructor of the class
     * @param gui is the GUi that contains all the needed info
     */
    public AskNickname(GUI gui) {
        super(gui);
    }

    /**
     * This method update the current panel after a change
     *
     * @return the current Panel updated
     * @throws IOException if there is an I/O problem
     */
    @Override
    public JPanel update() throws IOException {

        JPanel result = new BgJPanel("/LogoMasters.png", GUI.gameWidth, GUI.gameHeight);

        JPanel labelPanel = new JPanel();
        LayoutManager layout = new BoxLayout(labelPanel, BoxLayout.Y_AXIS);
        labelPanel.setLayout(layout);

        result.setPreferredSize(new Dimension(GUI.gameWidth, GUI.gameHeight));
        result.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel request = new JLabel("Insert your username");
        request.setFont(new Font("Times New Roman",Font.ITALIC,26));
        request.setForeground(GUI.borderColor);

        JTextField textField = new JTextField(20);
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setFont(new Font("Times New Roman",Font.PLAIN,18));
        textField.addActionListener(e -> {
            gui.model.setMyNickname(textField.getText());
            gui.socket.send(new Packet(HeaderTypes.HELLO, ChannelTypes.PLAYER_ACTIONS, textField.getText()));
        });

        labelPanel.add(Box.createRigidArea(new Dimension(0,500)));

        labelPanel.add(request);
        labelPanel.add(textField);
        labelPanel.setOpaque(false);

        result.add(labelPanel);

        return result;
    }
}
