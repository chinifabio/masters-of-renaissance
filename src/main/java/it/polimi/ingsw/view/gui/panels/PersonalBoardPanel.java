package it.polimi.ingsw.view.gui.panels;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class PersonalBoardPanel  extends JPanel {

    public void paint(Graphics g){
        myDrawImage("board.png", g);
    }

    public void myDrawImage(String image, Graphics g){
        ClassLoader cl = this.getClass().getClassLoader();
        InputStream url = cl.getResourceAsStream(image);
        BufferedImage img;
        try{
            assert url != null;
            img = ImageIO.read(url);
        } catch (IOException e){
            e.printStackTrace();
            return;
        }

        int width = 1920-580;
        int height = 1080-450;
        g.drawImage(img, 0, -1,width,height, null);
    }

    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */
    public PersonalBoardPanel() {
        this.setForeground(Color.gray);
        repaint();
        revalidate();
    }
}
