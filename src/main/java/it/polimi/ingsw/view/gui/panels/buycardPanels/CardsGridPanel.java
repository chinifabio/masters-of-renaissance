package it.polimi.ingsw.view.gui.panels.buycardPanels;


import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.BuyCardCommand;
import it.polimi.ingsw.communication.packet.commands.BuyDevCardCommand;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.panels.GuiPanel;
import it.polimi.ingsw.view.gui.panels.PersonalBoardPanel;
import it.polimi.ingsw.view.gui.panels.graphicComponents.BgJPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class CardsGridPanel extends GuiPanel {
    JOptionPane devSlot = new JOptionPane();

    public CardsGridPanel(GUI gui) {
        super(gui);
    }

    @Override
    public JPanel update() throws IOException {
        JPanel result = new BgJPanel("/Background.png",GUI.width+100, GUI.height);

        result.setPreferredSize(new Dimension(1920, 800));
        result.setBackground(GUI.borderColor);

        devSlot.setLayout(new FlowLayout());

        result.setLayout(new BoxLayout(result, BoxLayout.Y_AXIS));
        JPanel button = new JPanel();
        JPanel row1 = new JPanel();
        JPanel row2 = new JPanel();
        JPanel row3 = new JPanel();

        JButton r1c1 = new JButton();
        generateDevCardButton(r1c1, gui.model.getDevSetup().getDevSetup()[0][0].getCardID(), 0,0);
        JButton r1c2 = new JButton();
        generateDevCardButton(r1c2, gui.model.getDevSetup().getDevSetup()[0][1].getCardID(), 0, 1);
        JButton r1c3 = new JButton();
        generateDevCardButton(r1c3, gui.model.getDevSetup().getDevSetup()[0][2].getCardID(),0, 2);
        JButton r1c4 = new JButton();
        generateDevCardButton(r1c4, gui.model.getDevSetup().getDevSetup()[0][3].getCardID(),0, 3);
        JButton r2c1 = new JButton();
        generateDevCardButton(r2c1, gui.model.getDevSetup().getDevSetup()[1][0].getCardID(),1, 0);
        JButton r2c2 = new JButton();
        generateDevCardButton(r2c2, gui.model.getDevSetup().getDevSetup()[1][1].getCardID(), 1, 1);
        JButton r2c3 = new JButton();
        generateDevCardButton(r2c3, gui.model.getDevSetup().getDevSetup()[1][2].getCardID(), 1, 2);
        JButton r2c4 = new JButton();
        generateDevCardButton(r2c4, gui.model.getDevSetup().getDevSetup()[1][3].getCardID(), 1, 3);
        JButton r3c1 = new JButton();
        generateDevCardButton(r3c1, gui.model.getDevSetup().getDevSetup()[2][0].getCardID(), 2, 0);
        JButton r3c2 = new JButton();
        generateDevCardButton(r3c2, gui.model.getDevSetup().getDevSetup()[2][1].getCardID(), 2, 1);
        JButton r3c3 = new JButton();
        generateDevCardButton(r3c3, gui.model.getDevSetup().getDevSetup()[2][2].getCardID(), 2, 2);
        JButton r3c4 = new JButton();
        generateDevCardButton(r3c4, gui.model.getDevSetup().getDevSetup()[2][3].getCardID(), 2, 3);

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
        back.addActionListener(e -> gui.switchPanels(new PersonalBoardPanel(gui)));
        button.add(back);
        button.setOpaque(false);
        row1.setOpaque(false);
        row2.setOpaque(false);
        row3.setOpaque(false);

        result.add(button);
        result.add(row1);
        result.add(row2);
        result.add(row3);

        return result;
    }

    public JButton generateDevCardButton(JButton button, String name, int r, int c){
        InputStream url = this.getClass().getResourceAsStream("/DevCardsImage/" + name + ".png");
        BufferedImage img = null;
        try {
            assert url != null;
            img = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Object[] possibleValues = {"Yes", "No"};
        button.setPreferredSize(new Dimension(462/3, 698/3));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        Image scaledImage = GUI.getScaledImage(img, 462/3, 698/3);
        ImageIcon icon1 = new ImageIcon(scaledImage);
        button.setIcon(icon1);
        button.addActionListener(e -> {
            int n = JOptionPane.showOptionDialog(null,
                    "Would you like to buy this card? ",
                    "Buy Development Card",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    possibleValues,
                    possibleValues[1]);

            if (n == 0) {
                gui.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new BuyCardCommand().jsonfy()));
                gui.switchPanels(new MoveResourcesBuyCardPanel(gui, r, c, scaledImage));
            }
        });
        if (name.equals("EMPTY")){
            button.setEnabled(false);
        }
        return button;
    }
}
