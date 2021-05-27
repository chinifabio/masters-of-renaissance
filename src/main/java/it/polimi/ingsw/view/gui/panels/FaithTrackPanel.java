package it.polimi.ingsw.view.gui.panels;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class FaithTrackPanel extends JPanel {

    public void paint(Graphics g){
        myDrawImage("FaithTrackImages/FaithTrack.png", g);
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
        g.drawImage(img, 5, 29, 1350, 600, null);
    }

    public void printTrack(){

        this.setLayout(null);
        this.setVisible(true);
    }

}
