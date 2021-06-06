package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.litemodel.litecards.LiteDevCard;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;
import it.polimi.ingsw.view.gui.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class DevSlotPanel extends GuiPanel {
    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */

    public DevSlotPanel(GUI gui) {
        super(gui);

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setPreferredSize(new Dimension(810,400));

        JPanel leftCard = new JPanel();
        leftCard.setLayout(new OverlayLayout(leftCard));
        createDevSpace(leftCard, DevCardSlot.LEFT);

        JPanel centerCard = new JPanel();
        centerCard.setLayout(new OverlayLayout(centerCard));
        createDevSpace(centerCard, DevCardSlot.CENTER);

        JPanel rightCard = new JPanel();
        rightCard.setLayout(new OverlayLayout(rightCard));
        createDevSpace(rightCard, DevCardSlot.RIGHT);

        this.add(Box.createRigidArea(new Dimension(50,0)));
        this.add(leftCard);
        this.add(Box.createRigidArea(new Dimension(90,0)));
        this.add(centerCard);
        this.add(Box.createRigidArea(new Dimension(80,0)));
        this.add(rightCard);
        this.setOpaque(false);
    }

    public void createDevSpace(JPanel space, DevCardSlot slot){

        float i = 0;
        for (LiteDevCard card : gui.model.getDevelop(gui.model.getMe()).get(slot)){
            if (card.getLevel() != LevelDevCard.NOLEVEL && card.getColor() != ColorDevCard.NOCOLOR && !card.getCardID().equals("Empty")) {
                JLabel label = new JLabel();
                InputStream url = this.getClass().getResourceAsStream("/DevCardsImage/" + card.getCardID() + ".png");
                BufferedImage img = null;
                try {
                    assert url != null;
                    img = ImageIO.read(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Image scaledImage = GUI.getScaledImage(img, (462 / 2)-50, (698 / 2)-50);
                ImageIcon icon1 = new ImageIcon(scaledImage);
                label.setIcon(icon1);

                label.setAlignmentY(0.7f - i);
                i = i + 0.2f;

                space.add(label);
            } else {
                JLabel label = new JLabel();
                InputStream url = this.getClass().getResourceAsStream("/WarehouseRes/empty.png");
                BufferedImage img = null;
                try {
                    assert url != null;
                    img = ImageIO.read(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Image scaledImage = GUI.getScaledImage(img, (462 / 2)-50, (698 / 2)-50);
                ImageIcon icon1 = new ImageIcon(scaledImage);
                label.setIcon(icon1);

                label.setAlignmentY(0.7f - i);
                i = i + 0.2f;

                space.add(label);
                space.setOpaque(false);
            }
        }
    }

    @Override
    public void reactToPacket(Packet packet) throws IOException {

    }
}
