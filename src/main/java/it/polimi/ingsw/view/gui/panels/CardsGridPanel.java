package it.polimi.ingsw.view.gui.panels;


import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.BuyDevCardCommand;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;
import it.polimi.ingsw.view.gui.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class CardsGridPanel extends GuiPanel {

    private DevCardSlot slot;
    JOptionPane devSlot = new JOptionPane();

    public CardsGridPanel(GUI gui) {
        super(gui);
        this.setPreferredSize(new Dimension(1920, 800));
        this.setBackground(GUI.borderColor);

        devSlot.setLayout(new FlowLayout());
        DevCardSlot[] possibleValues = { DevCardSlot.LEFT, DevCardSlot.CENTER, DevCardSlot.RIGHT };

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel button = new JPanel();
        JPanel row1 = new JPanel();
        JPanel row2 = new JPanel();
        JPanel row3 = new JPanel();

        JButton r1c1 = new JButton();
        generateDevCardButton(r1c1, gui.model.getDevSetup().getDevSetup()[0][0].getCardID(), possibleValues);
        JButton r1c2 = new JButton();
        generateDevCardButton(r1c2, gui.model.getDevSetup().getDevSetup()[0][1].getCardID(), possibleValues);
        JButton r1c3 = new JButton();
        generateDevCardButton(r1c3, gui.model.getDevSetup().getDevSetup()[0][2].getCardID(), possibleValues);
        JButton r1c4 = new JButton();
        generateDevCardButton(r1c4, gui.model.getDevSetup().getDevSetup()[0][3].getCardID(), possibleValues);
        JButton r2c1 = new JButton();
        generateDevCardButton(r2c1, gui.model.getDevSetup().getDevSetup()[1][0].getCardID(), possibleValues);
        JButton r2c2 = new JButton();
        generateDevCardButton(r2c2, gui.model.getDevSetup().getDevSetup()[1][1].getCardID(), possibleValues);
        JButton r2c3 = new JButton();
        generateDevCardButton(r2c3, gui.model.getDevSetup().getDevSetup()[1][2].getCardID(), possibleValues);
        JButton r2c4 = new JButton();
        generateDevCardButton(r2c4, gui.model.getDevSetup().getDevSetup()[1][3].getCardID(), possibleValues);
        JButton r3c1 = new JButton();
        generateDevCardButton(r3c1, gui.model.getDevSetup().getDevSetup()[2][0].getCardID(), possibleValues);
        JButton r3c2 = new JButton();
        generateDevCardButton(r3c2, gui.model.getDevSetup().getDevSetup()[2][1].getCardID(), possibleValues);
        JButton r3c3 = new JButton();
        generateDevCardButton(r3c3, gui.model.getDevSetup().getDevSetup()[2][2].getCardID(), possibleValues);
        JButton r3c4 = new JButton();
        generateDevCardButton(r3c4, gui.model.getDevSetup().getDevSetup()[2][3].getCardID(), possibleValues);

        row1.add(r1c1);
        row1.add(r1c2);
        row1.add(r1c3);
        row1.add(r1c4);
        row2.add(r2c1);
        row2.add(r2c2);
        row2.add(r2c3);
        row2.add(r2c4);
        row3.add(r3c1);
        row3.add(r3c2);
        row3.add(r3c3);
        row3.add(r3c4);

        JButton back = new JButton("Return to PB");
        back.addActionListener(e -> {
            try {
                gui.switchPanels(new PersonalBoardPanel(gui));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        button.add(back);
        button.setOpaque(false);
        row1.setOpaque(false);
        row2.setOpaque(false);
        row3.setOpaque(false);

        this.add(button);
        this.add(row1);
        this.add(row2);
        this.add(row3);
    }

    @Override
    public void reactToPacket(Packet packet) throws IOException {
        switch (packet.header){
            case INVALID -> gui.notifyPlayerError(packet.body);
        }
    }

    public JButton generateDevCardButton(JButton button, String name, DevCardSlot[] possibleValues){
        InputStream url = this.getClass().getResourceAsStream("/DevCardsImage/" + name + ".png");
        BufferedImage img = null;
        try {
            assert url != null;
            img = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        button.setPreferredSize(new Dimension(462/3, 698/3));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        Image scaledImage = GUI.getScaledImage(img, 462/3, 698/3);
        ImageIcon icon1 = new ImageIcon(scaledImage);
        button.setIcon(icon1);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DevCardSlot slot = (DevCardSlot) JOptionPane.showInputDialog(null, "Select the Slot of the PersonalBoard \n where the Development Card will be placed", "BuyCards",
                        JOptionPane.INFORMATION_MESSAGE, null,
                        possibleValues, possibleValues[0]);
                gui.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new BuyDevCardCommand( gui.model.getDevSetup().getDevSetup()[0][0].getLevel(),  gui.model.getDevSetup().getDevSetup()[0][0].getColor(), slot).jsonfy()));
            }
        });
        return button;
    }
}
