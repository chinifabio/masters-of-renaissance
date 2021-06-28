package it.polimi.ingsw.view.gui.panels.graphicComponents;

import it.polimi.ingsw.litemodel.LiteResource;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.view.gui.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class is the GUI Panel of the Resource Buffer
 */
public class BufferPanel extends BgJPanel{

    /**
     * This is the constructor of the class
     * @param gui is the GUI that contains the Buffer info
     * @throws IOException if there is a I/O problem
     */
    public BufferPanel(GUI gui, String nickname) throws IOException {
        super("/buffer.png", 275,100);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createRigidArea(new Dimension(0,20)));
        this.setPreferredSize(new Dimension(275,100));
        this.setOpaque(false);

        JPanel bigPanel = new JPanel();
        JPanel bufferPanel = new JPanel();

        bigPanel.setPreferredSize(new Dimension(400, 100));

        for (LiteResource res : gui.model.getDepot(nickname, DepotSlot.BUFFER).getResourcesInside()){
            JPanel resource = new JPanel();
            resource.setLayout(new OverlayLayout(resource));
            resource.setOpaque(false);
            JLabel amount = new JLabel();
            amount.setFont(new Font(amount.getName(), Font.BOLD, 16));
            if (res.getType() == ResourceType.SERVANT){
                amount.setForeground(Color.WHITE);
            } else {
                amount.setForeground(Color.BLACK);
            }

            amount.setBackground(new Color(0, 0, 0, 10));
            amount.setText(String.valueOf(res.getAmount()));

            JLabel image = new JLabel();
            createResourceLabel(image, GUI.resourceImages.get(res.getType()));

            resource.add(amount);
            image.setAlignmentX(0.31f);
            image.setAlignmentY(0.55f);
            resource.add(image);

            resource.add(Box.createRigidArea(new Dimension(50,0)));
            bufferPanel.add(resource);
        }

        bufferPanel.setOpaque(false);
        bigPanel.add(bufferPanel);

        bigPanel.setOpaque(false);

        this.add(bigPanel);
    }

    /**
     * This method changes the passed label by adding the resource image
     * @param label is the button to change
     * @param resource is the path of the resource image
     */
    public void createResourceLabel(JLabel label, String resource){
        InputStream url = this.getClass().getResourceAsStream("/" + resource);
        BufferedImage img = null;
        try {
            assert url != null;
            img = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Image scaledImage = GUI.getScaledImage(img, 42, 42);
        ImageIcon icon1 = new ImageIcon(scaledImage);
        label.setIcon(icon1);
    }
}
