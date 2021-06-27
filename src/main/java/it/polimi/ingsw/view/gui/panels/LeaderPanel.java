package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.ActivateLeaderCommand;
import it.polimi.ingsw.communication.packet.commands.DiscardLeaderCommand;
import it.polimi.ingsw.litemodel.litecards.LiteLeaderCard;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.panels.graphicComponents.BgJPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class is the GUI Panel of the Leader Cards
 */
public class LeaderPanel extends GuiPanel {

    /**
     * This is the constructor of the class
     * @param gui is the GUI that contains all the needed info
     */
    public LeaderPanel(GUI gui) {
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
        JPanel background = new BgJPanel("/Background.png",GUI.width-300, GUI.height);
        JPanel result = new JPanel();

        background.setPreferredSize(new Dimension(GUI.gameWidth, GUI.gameHeight));

        result.setOpaque(false);
        result.add(Box.createRigidArea(new Dimension(0, 800)));

        result.setLayout(new BoxLayout(result, BoxLayout.X_AXIS));

        for (LiteLeaderCard card : gui.model.getLeader(gui.model.getMe())){
            result.add(new LeaderCardPanel(card.getCardID(), gui, card.isActivated()));
        }

        JButton back = new JButton("Return to PB");
        back.addActionListener(e -> gui.switchPanels(new PersonalBoardPanel(gui)));
        result.add(back);

        background.add(result);
        return background;
    }

}

/**
 * This class is the GUI Panel of the Leader Card
 */
class LeaderCardPanel extends JPanel {

    /**
     * This is the constructor of the class
     * @param id is the ID of the Leader Card
     * @param gui is the GUI that contains all the needed info
     * @param activated indicates if the LeaderCard is activated or not
     */
    public LeaderCardPanel(String id, GUI gui, boolean activated) {
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        InputStream url = this.getClass().getResourceAsStream("/LeaderCardsImages/" + id + ".png");
        BufferedImage img = null;
        try {
            assert url != null;
            img = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Image scaledImage = GUI.getScaledImage(img, 462/2, 698/2);
        ImageIcon icon1 = new ImageIcon(scaledImage);
        JLabel label = new JLabel();

        label.setIcon(icon1);

        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));


        JButton activate = new JButton("Activate");
        activate.addActionListener(e -> gui.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new ActivateLeaderCommand(id).jsonfy())));

        JButton discard = new JButton("Discard");
        discard.addActionListener(e -> gui.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new DiscardLeaderCommand(id).jsonfy())));

        label.setAlignmentX(Component.CENTER_ALIGNMENT);


        add(Box.createRigidArea(new Dimension(0, 10)));

        if (!activated) {
            buttons.add(activate);
            buttons.add(Box.createHorizontalGlue());
            buttons.add(discard);
        } else {
            label.setBorder(BorderFactory.createLineBorder(Color.green, 5));
        }
        add(label);

        buttons.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttons.setOpaque(false);
        add(buttons);

        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        setVisible(true);
        setOpaque(false);
    }

}
