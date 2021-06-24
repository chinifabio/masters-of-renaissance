package it.polimi.ingsw.view.gui.panels.graphicComponents;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.FaithPointCheatCommand;
import it.polimi.ingsw.communication.packet.commands.ResourceCheatCommand;
import it.polimi.ingsw.view.gui.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;

public class BgJPanel extends JPanel {

    private final Image background;

    private final int x;
    private final int y;

    public BgJPanel(String path) throws IOException {
        InputStream is = getClass().getResourceAsStream(path);
        assert is != null;
        background = ImageIO.read(is);
        this.x = 0;
        this.y = 0;
    }

    public BgJPanel(String path, int w, int h) throws IOException {
        InputStream is = getClass().getResourceAsStream(path);
        assert is != null;
        background = GUI.getScaledImage(ImageIO.read(is), w, h);
        this.x = 0;
        this.y = 0;
    }

    public BgJPanel(String path, int w, int h, int x, int y) throws IOException {
        InputStream is = getClass().getResourceAsStream(path);
        assert is != null;
        background = GUI.getScaledImage(ImageIO.read(is), w, h);
        this.x = x;
        this.y = y;
    }

    /**
     * Draw the background
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, x, y,null);
    }

}
