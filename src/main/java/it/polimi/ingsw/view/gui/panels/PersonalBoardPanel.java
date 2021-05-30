package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.view.gui.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class PersonalBoardPanel  extends GuiPanel {

    private final Image personalBoard;

    public PersonalBoardPanel(GUI gui) throws IOException {
        super(gui);

        InputStream is = getClass().getResourceAsStream("/board.png");
        if (is == null) throw new IOException("board.png not found");
        personalBoard = ImageIO.read(is);

        this.setForeground(Color.gray);
        repaint();
        revalidate();

        setName("Homepage");

        JButton viewLeader = new JButton("Show Leader Cards");
        viewLeader.addActionListener(e -> gui.switchPanels(new LeaderPanel(gui)));

        JButton viewMarket = new JButton("View Market Tray");
        viewMarket.addActionListener(e -> gui.switchPanels(new MarketPanel(gui)));

        setPreferredSize(new Dimension(500, 500));

        add(viewLeader);
        add(viewMarket);
        setVisible(true);
    }

    /**
     * Draw the background
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = 1920-380;
        int height = 1080-270;
        g.drawImage(personalBoard, 0, 0,width,height, null);
    }

    @Override
    public void reactToPacket(Packet packet) {
        System.out.println(packet);
    }
}
