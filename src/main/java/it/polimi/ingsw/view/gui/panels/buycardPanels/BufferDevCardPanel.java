package it.polimi.ingsw.view.gui.panels.buycardPanels;

import it.polimi.ingsw.litemodel.LiteResource;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.panels.graphicComponents.BgJPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class BufferDevCardPanel extends BgJPanel {

    public BufferDevCardPanel(GUI gui) throws IOException {
        super("/buffer.png", 250, 100);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createRigidArea(new Dimension(0,20)));
        this.setPreferredSize(new Dimension(275,100));
        this.setOpaque(false);

        JPanel bigPanel = new JPanel();
        JPanel bufferPanel = new JPanel();

        //bigPanel.setPreferredSize(new Dimension(250, 100));

        for (LiteResource res : gui.model.getDepot(gui.model.getMe(), DepotSlot.DEVBUFFER).getResourcesInside()){
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


        bufferPanel.add(Box.createRigidArea(new Dimension(20,0)));
        bufferPanel.setOpaque(false);
        bigPanel.add(bufferPanel);

        bigPanel.setOpaque(false);

        this.add(bigPanel);

    }

    public void createResourceLabel(JLabel button, String resource) throws IOException {
        InputStream url = this.getClass().getResourceAsStream("/" + resource);
        assert url != null;
        Image scaledImage = GUI.getScaledImage(ImageIO.read(url), 42, 42);

        ImageIcon icon1 = new ImageIcon(scaledImage);
        button.setIcon(icon1);
        button.setPreferredSize(new Dimension(44, 44));

    }

    //@Override
    //protected void paintComponent(Graphics g) {
    //    super.paintComponent(g);
    //    int width = 250;
    //    int height = 100;
    //    g.drawImage(buffer, 15, 0,width,height, null);
    //}
}
