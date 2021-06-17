package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.ActivateLeaderCommand;
import it.polimi.ingsw.communication.packet.commands.DiscardLeaderCommand;
import it.polimi.ingsw.litemodel.litecards.LiteLeaderCard;
import it.polimi.ingsw.view.gui.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class LeaderPanel extends GuiPanel {

    public LeaderPanel(GUI gui) {
        super(gui);
    }

    @Override
    public JPanel update() throws IOException {
        JPanel result = new JPanel();

        result.setOpaque(false);
        result.add(Box.createRigidArea(new Dimension(0, 800)));

        result.setLayout(new BoxLayout(result, BoxLayout.X_AXIS));

        for (LiteLeaderCard card : gui.model.getLeader(gui.model.getMe())){
            result.add(new LeaderCardPanel(card.getCardID(), gui, card.isActivated()));
        }

        JButton back = new JButton("Return to PB");
        back.addActionListener(e -> gui.switchPanels(new PersonalBoardPanel(gui)));
        result.add(back);

        return result;
    }
}

class LeaderCardPanel extends JPanel {

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
