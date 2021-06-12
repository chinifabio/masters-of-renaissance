package it.polimi.ingsw.view.gui.panels.buycardPanels;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.MoveDepotCommand;
import it.polimi.ingsw.litemodel.LiteResource;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.panels.GuiPanel;
import it.polimi.ingsw.view.gui.panels.movePanels.BufferMovePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BufferDevCardPanel extends GuiPanel {

    Image buffer;

    public BufferDevCardPanel(GUI gui) {
        super(gui);

        InputStream is = getClass().getResourceAsStream("/buffer.png");
        if (is == null) try {
            throw new IOException("buffer.png not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            buffer = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }


        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createRigidArea(new Dimension(0,20)));
        this.setPreferredSize(new Dimension(275,100));
        this.setOpaque(false);

        JPanel bigPanel = new JPanel();
        JPanel bufferPanel = new JPanel();

        bigPanel.setPreferredSize(new Dimension(400, 100));

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

        bufferPanel.setOpaque(false);
        bigPanel.add(bufferPanel);

        bigPanel.setOpaque(false);

        this.add(bigPanel);

    }

    public void createResourceLabel(JLabel button, String resource){
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
        button.setIcon(icon1);
        button.setPreferredSize(new Dimension(44, 44));

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = 250;
        int height = 100;
        g.drawImage(buffer, 15, 0,width,height, null);
    }

    @Override
    public void reactToPacket(Packet packet) throws IOException {
        switch (packet.header) {
            case OK -> gui.switchPanels(new BufferDevCardPanel(gui));
            case INVALID -> gui.notifyPlayerError(packet.body);
        }
    }
}
