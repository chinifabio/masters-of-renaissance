package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.communication.packet.Packet;
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

public class WarehousePanel  extends GuiPanel {


    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */
    public WarehousePanel(GUI gui) {
        super(gui);

        this.setPreferredSize(new Dimension(320,400));
        this.setOpaque(false);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));


        JPanel topDepot = new JPanel();
        topDepot.setPreferredSize(new Dimension(75, 10));

        insertResourceInDepot(topDepot, DepotSlot.TOP);

        topDepot.setOpaque(false);



        JPanel middleDepot = new JPanel();

        insertResourceInDepot(middleDepot, DepotSlot.MIDDLE);

        middleDepot.setOpaque(false);
        middleDepot.setPreferredSize(new Dimension(75, 10));



        JPanel bottomDepot = new JPanel();

        insertResourceInDepot(bottomDepot, DepotSlot.BOTTOM);

        bottomDepot.setOpaque(false);
        bottomDepot.setPreferredSize(new Dimension(75, 10));




        JPanel bigStrong = new JPanel();


        JPanel strongbox = new JPanel();
        strongbox.setLayout(new GridLayout(2,2));

        for (LiteResource res : gui.model.getDepot(gui.model.getMe(), DepotSlot.STRONGBOX).getResourcesInside()){
            JPanel resource = new JPanel();
            resource.setLayout(new OverlayLayout(resource));
            resource.setOpaque(false);
            JLabel amount = new JLabel();
            amount.setFont(new Font(amount.getName(), Font.BOLD, 20));
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


            strongbox.add(resource);
        }

        strongbox.setOpaque(false);

        strongbox.setPreferredSize(new Dimension(270, 120));

        bigStrong.add(strongbox);
        bigStrong.setOpaque(false);

        this.add(topDepot);
        this.add(middleDepot);
        this.add(bottomDepot);
        this.add(Box.createRigidArea(new Dimension(320, 40)));
        this.add(bigStrong);

    }

    public void createResourceLabel(JLabel label, String resource){
        InputStream url = this.getClass().getResourceAsStream("/" + resource);
        BufferedImage img = null;
        try {
            assert url != null;
            img = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Image scaledImage = GUI.getScaledImage(img, 55, 55);
        ImageIcon icon1 = new ImageIcon(scaledImage);
        label.setIcon(icon1);
    }

    public void insertResourceInDepot(JPanel depot, DepotSlot slot){
        depot.add(Box.createRigidArea(new Dimension(35,0)));
        LiteResource tempRes = gui.model.getDepot(gui.getModel().getMe(), slot).getResourcesInside().get(0);

        for (int i = 0; i <  tempRes.getAmount(); i++){
            JLabel label = new JLabel();
            createResourceLabel(label, GUI.resourceImages.get(tempRes.getType()));
            depot.add(label);
        }
    }

    @Override
    public void reactToPacket(Packet packet) throws IOException {}
}
