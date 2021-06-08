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

public class OtherPlayerLeaderPanel extends GuiPanel {


    public OtherPlayerLeaderPanel(GUI gui, String nickname) {
        super(gui);

        setOpaque(false);
        this.add(Box.createRigidArea(new Dimension(0, 800)));

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        for (LiteLeaderCard card : gui.model.getLeader(nickname)){
            add(new OtherLeaderCardPanel(card.getCardID(), gui, card.isActivated()));
        }

        JButton back = new JButton("Return to PB");
        back.addActionListener(e -> {
            gui.switchPanels(new OtherPlayersPanel(gui, nickname));
        });
        add(back);
    }

    @Override
    public void reactToPacket(Packet packet) throws IOException {}

}

class OtherLeaderCardPanel extends JPanel {

    public OtherLeaderCardPanel(String id, GUI gui, boolean activated) {
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        InputStream url;
        if (activated){
            url = this.getClass().getResourceAsStream("/LeaderCardsImages/" + id + ".png");
        } else {
            url = this.getClass().getResourceAsStream("/LeaderCardsImages/covered.png");
        }

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



        label.setAlignmentX(Component.CENTER_ALIGNMENT);


        add(Box.createRigidArea(new Dimension(0, 10)));

        add(label);

        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        setVisible(true);
        setOpaque(false);
    }
}
