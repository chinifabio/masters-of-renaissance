package it.polimi.ingsw.view.gui.panels;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class FaithTrackPanel extends JPanel {

    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */
    public FaithTrackPanel() {
        this.setPreferredSize(new Dimension(1920-380-200,285));
        this.setBackground(Color.red);
    }
}
