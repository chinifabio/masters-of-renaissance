package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.view.gui.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
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
        setLayout(new FlowLayout());

        setName("Homepage");

        JButton viewLeader = new JButton("Show Leader Cards");
        viewLeader.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        viewLeader.addActionListener(e -> gui.switchPanels(new LeaderPanel(gui)));

        JButton viewMarket = new JButton("View Market Tray");
        viewLeader.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        viewMarket.addActionListener(e -> gui.switchPanels(new MarketPanel(gui)));

        setPreferredSize(new Dimension(1920, 1080));

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

        int width = 1920-380-100;
        int height = 1080-270-100;
        g.drawImage(personalBoard, 250, -10,width,height, null);
    }

    @Override
    public void reactToPacket(Packet packet) {
        System.out.println(packet);
    }
}
