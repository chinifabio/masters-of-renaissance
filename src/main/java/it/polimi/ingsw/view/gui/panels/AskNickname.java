package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.view.gui.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class AskNickname extends GuiPanel {

    private Image background;

    public AskNickname(GUI gui) {
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

        JPanel labelPanel = new JPanel();
        LayoutManager layout = new BoxLayout(labelPanel, BoxLayout.Y_AXIS);
        labelPanel.setLayout(layout);

        this.setPreferredSize(new Dimension(gui.width-300, gui.height));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

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

        this.add(labelPanel);
       //setOpaque(false);
    }

    @Override
    public void reactToPacket(Packet packet) throws IOException {
        switch (packet.header) {
            case RECONNECTED -> gui.switchPanels(new PersonalBoardPanel(gui));
            case GAME_INIT -> gui.switchPanels(new InitGamePanel(gui));
            case SET_PLAYERS_NUMBER -> gui.switchPanels(new AskPlayers(gui));
            case INVALID -> gui.notifyPlayerError(packet.body);
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
