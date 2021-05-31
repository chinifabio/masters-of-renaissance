package it.polimi.ingsw.view.gui.panels;


import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.view.gui.GUI;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class LogoPanel extends GuiPanel{
    private final Image img;

    public LogoPanel(GUI gui) throws IOException {
        super (gui);
        setPreferredSize(new Dimension(1920-380-200, 1080-270));
        img = ImageIO.read(getClass().getResourceAsStream("/LogoMasters.png"));
    }

    @Override
    public void reactToPacket(Packet packet) {
        switch (packet.header) {
            case OK -> gui.switchPanels(new LeaderPanel(gui));
            case INVALID -> gui.notifyPlayerError(packet.body);
        }
    }

    /**
     * Draw the background
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = 1920-380-200;
        int height = 1080-270;
        g.drawImage(img, 0, 0,width,height, null);
    }

}
