package it.polimi.ingsw.view.gui.panels.graphicComponents;

import it.polimi.ingsw.view.gui.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class BgJPanel extends JPanel {

    private final Image background;

    public BgJPanel(String path) throws IOException {
        InputStream is = getClass().getResourceAsStream(path);
        assert is != null;
        background = ImageIO.read(is);
    }

    public BgJPanel(String path, int w, int h) throws IOException {
        InputStream is = getClass().getResourceAsStream(path);
        assert is != null;
        background = GUI.getScaledImage(ImageIO.read(is), w, h);
    }

    /**
     * Draw the background
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0,null);
    }
}
