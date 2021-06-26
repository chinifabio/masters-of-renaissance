package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.litemodel.litecards.LiteLeaderCard;
import it.polimi.ingsw.view.gui.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class OtherPlayerLeaderPanel extends GuiPanel {


    private final String nickname;

    private final boolean isEnd;

    public OtherPlayerLeaderPanel(GUI gui, String nickname, boolean isEnd) {
        super(gui);
        this.nickname = nickname;
        this.isEnd = isEnd;
    }

    @Override
    public JPanel update() throws IOException {
        JPanel result = new JPanel();

        result.setOpaque(false);
        result.add(Box.createRigidArea(new Dimension(0, 800)));
        result.setLayout(new BoxLayout(result, BoxLayout.X_AXIS));

        for (LiteLeaderCard card : gui.model.getLeader(nickname)){
            result.add(new OtherLeaderCardPanel(card.getCardID(), gui, card.isActivated()));
        }

        JButton back = new JButton("Return");
        back.addActionListener(e -> gui.switchPanels(new OtherPlayersPanel(gui, nickname, isEnd)));
        result.add(back);

        return result;
    }
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
