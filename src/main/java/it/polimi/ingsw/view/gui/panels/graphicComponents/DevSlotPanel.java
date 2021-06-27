package it.polimi.ingsw.view.gui.panels.graphicComponents;

import it.polimi.ingsw.litemodel.litecards.LiteDevCard;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;
import it.polimi.ingsw.view.gui.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class is the GUI Panel of the DevCards slots of the PersonalBoard
 */
public class DevSlotPanel extends JPanel {

    /**
     * This attribute is the GUI that contains all the needed info of the DevCards Slots
     */
    private final GUI gui;

    /**
     * This is the constructor of the class
     * @param gui is the GUI that contains all the needed info
     * @param player is the nickname of the Player that own the DevCardSlots
     * @throws IOException if there is an I/O problem
     */
    public DevSlotPanel(GUI gui, String player) throws IOException {
        this.gui = gui;

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setPreferredSize(new Dimension(810,400));

        JPanel leftCard = new JPanel();
        leftCard.setLayout(new OverlayLayout(leftCard));
        createDevSpace(leftCard, DevCardSlot.LEFT, player);

        JPanel centerCard = new JPanel();
        centerCard.setLayout(new OverlayLayout(centerCard));
        createDevSpace(centerCard, DevCardSlot.CENTER, player);

        JPanel rightCard = new JPanel();
        rightCard.setLayout(new OverlayLayout(rightCard));
        createDevSpace(rightCard, DevCardSlot.RIGHT, player);

        this.add(Box.createRigidArea(new Dimension(50,0)));
        this.add(leftCard);
        this.add(Box.createRigidArea(new Dimension(90,0)));
        this.add(centerCard);
        this.add(Box.createRigidArea(new Dimension(80,0)));
        this.add(rightCard);
        this.setOpaque(false);
    }

    /**
     * This method creates the DevCardsSpace panel by adding the cards in the right order
     * @param space is the Panel where the cards will be added
     * @param slot is the DevCardSlot that contains the cards
     * @param player is the nickname of the player that own the DevCardsSlots
     * @throws IOException if there is an I/O problem
     */
    public void createDevSpace(JPanel space, DevCardSlot slot, String player) throws IOException {

        float i = 0;
        for (LiteDevCard card : gui.model.getDevelop(player).get(slot)){
            if (card.getLevel() != LevelDevCard.NOLEVEL && card.getColor() != ColorDevCard.NOCOLOR && !card.getCardID().equals("Empty")) {
                JLabel label = new JLabel();

                InputStream url = this.getClass().getResourceAsStream("/DevCardsImages/" + card.getCardID() + ".png");
                assert url != null;
                Image scaledImage = GUI.getScaledImage(ImageIO.read(url), (462 / 2)-50, (698 / 2)-50);
                ImageIcon icon1 = new ImageIcon(scaledImage);
                label.setIcon(icon1);

                label.setAlignmentY(0.7f - i);
                i = i + 0.2f;

                space.add(label);
            } else {
                JLabel label = new JLabel();
                InputStream url = this.getClass().getResourceAsStream("/WarehouseRes/empty.png");
                assert url != null;
                Image scaledImage = GUI.getScaledImage(ImageIO.read(url), (462 / 2)-50, (698 / 2)-50);
                ImageIcon icon1 = new ImageIcon(scaledImage);
                label.setIcon(icon1);

                label.setAlignmentY(0.7f - i);
                i = i + 0.2f;

                space.add(label);
                space.setOpaque(false);
            }
        }
    }
}
