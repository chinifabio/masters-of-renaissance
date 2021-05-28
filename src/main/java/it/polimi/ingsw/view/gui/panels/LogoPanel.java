package it.polimi.ingsw.view.gui.panels;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class LogoPanel extends JPanel{


    public void paint(Graphics g){
        myDrawImage("LogoMasters.png", g);
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

        int width = 1920-380;
        int height = 1080-270;
        g.drawImage(img, 0, -1,width,height, null);
    }

}
