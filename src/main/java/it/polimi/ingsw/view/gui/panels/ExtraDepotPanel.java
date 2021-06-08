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

public class ExtraDepotPanel extends GuiPanel{

    public ExtraDepotPanel(GUI gui, String nickname) {
        super(gui);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(200,400));

        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.red);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JPanel firstDepot = new JPanel();
        firstDepot.setPreferredSize(new Dimension(200,50));
        if (gui.model.getDepot(nickname, DepotSlot.SPECIAL1) != null) {
            firstDepot = new LeaderDepotPanel(gui.model.getDepot(nickname, DepotSlot.SPECIAL1).getResourcesInside().get(0).getType());
            insertResourceInDepot(firstDepot, DepotSlot.SPECIAL1, nickname);
        }

        firstDepot.setOpaque(false);
        topPanel.add(firstDepot);

        JPanel secondDepot = new JPanel();

        secondDepot.setPreferredSize(new Dimension(200,50));
        if (gui.model.getDepot(nickname, DepotSlot.SPECIAL2) != null) {
            secondDepot = new LeaderDepotPanel(gui.model.getDepot(nickname, DepotSlot.SPECIAL2).getResourcesInside().get(0).getType());
            insertResourceInDepot(secondDepot, DepotSlot.SPECIAL2, nickname);

        }
        secondDepot.setOpaque(false);
        topPanel.add(secondDepot);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setPreferredSize(new Dimension(200,200));

        topPanel.setOpaque(false);
        this.add(topPanel);
        this.add(bottomPanel);
        this.setOpaque(false);
    }

    @Override
    public void reactToPacket(Packet packet) throws IOException {

    }

    public void insertResourceInDepot(JPanel depot, DepotSlot slot, String player){
        depot.add(Box.createRigidArea(new Dimension(5,0)));
        LiteResource tempRes = gui.model.getDepot(player, slot).getResourcesInside().get(0);

        for (int i = 0; i <  tempRes.getAmount(); i++){
            JLabel label = new JLabel();
            createResourceLabel(label, GUI.resourceImages.get(tempRes.getType()));
            depot.add(Box.createRigidArea(new Dimension(20,0)));
            depot.add(label);
        }
        if (tempRes.getAmount() == 1){
            JLabel label = new JLabel();
            createResourceLabel(label, GUI.resourceImages.get(ResourceType.EMPTY));
            depot.add(Box.createRigidArea(new Dimension(20,0)));
            depot.add(label);
        }
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

        Image scaledImage = GUI.getScaledImage(img, 45, 45);
        ImageIcon icon1 = new ImageIcon(scaledImage);
        label.setIcon(icon1);
    }
}
