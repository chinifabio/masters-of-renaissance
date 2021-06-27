package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.litemodel.litecards.LiteLeaderCard;
import it.polimi.ingsw.view.gui.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class is the GUI Panel that allows the player to see other players' LeaderCards
 */
public class OtherPlayerLeaderPanel extends GuiPanel {

    /**
     * This attribute is the nickname of the Player
     */
    private final String nickname;

    /**
     * This attribute indicates if the Player is in the EndGame phase so he can only see other player
     */
    private final boolean isEnd;

    /**
     * This is the constructor of the class
     * @param gui is the GUI that contains all the needed info
     * @param nickname is the nickname of the player
     * @param isEnd indicates if the Player in in the endgame or not
     */
    public OtherPlayerLeaderPanel(GUI gui, String nickname, boolean isEnd) {
        super(gui);
        this.nickname = nickname;
        this.isEnd = isEnd;
    }

    /**
     * This method update the current panel after a change
     *
     * @return the current Panel updated
     * @throws IOException if there is an I/O problem
     */
    @Override
    public JPanel update() throws IOException {
        JPanel result = new JPanel();

        result.setOpaque(false);
        result.add(Box.createRigidArea(new Dimension(0, 800)));
        result.setLayout(new BoxLayout(result, BoxLayout.X_AXIS));

        for (LiteLeaderCard card : gui.model.getLeader(nickname)){
            result.add(new OtherLeaderCardPanel(card.getCardID(), card.isActivated()));
        }

        JButton back = new JButton("Return");
        back.addActionListener(e -> gui.switchPanels(new OtherPlayersPanel(gui, nickname, isEnd)));
        result.add(back);

        return result;
    }
}

/**
 * This class is the GUI Panel of the LeaderCard of other players
 */
class OtherLeaderCardPanel extends JPanel {

    /**
     * This is the constructor of the class
     * @param id is the ID of the card
     * @param activated indicates if the LeaderCard is activated or not
     */
    public OtherLeaderCardPanel(String id, boolean activated) {
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
