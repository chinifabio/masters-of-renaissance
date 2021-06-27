package it.polimi.ingsw.view.gui.panels.graphicComponents;
import it.polimi.ingsw.view.gui.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class is a generic GUI Panel that has a background image
 */
public class BgJPanel extends JPanel {

    /**
     * This attribute is the background image
     */
    private final Image background;

    /**
     * This attribute is the horizontal coordinate
     */
    private final int x;

    /**
     * This attribute is the vertical coordinate
     */
    private final int y;

    /**
     * This is the constructor of the class
     * @param path is the path of the background image
     * @throws IOException if there is an I/O problem
     */
    public BgJPanel(String path) throws IOException {
        InputStream is = getClass().getResourceAsStream(path);
        assert is != null;
        background = ImageIO.read(is);
        this.x = 0;
        this.y = 0;
    }

    /**
     * This is the constructor of the class where the dimension of the image can be changed
     * @param path is the path of the background image
     * @param w is the width of the image
     * @param h is the height of the image
     * @throws IOException if there is an I/O problem
     */
    public BgJPanel(String path, int w, int h) throws IOException {
        InputStream is = getClass().getResourceAsStream(path);
        assert is != null;
        background = GUI.getScaledImage(ImageIO.read(is), w, h);
        this.x = 0;
        this.y = 0;
    }

    /**
     * This is the constructor of the class where the dimension and the initial drawing coordinates of the image can be changed
     * @param path is the path of the background image
     * @param w is the width of the image
     * @param h is the height of the image
     * @param x is the horizontal coordinate where the image will be painted
     * @param y is the vertical coordinate where the image will be painted
     * @throws IOException if there is an I/O problem
     */
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
