package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.view.gui.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class PersonalBoardPanel  extends JPanel {
    public PersonalBoardPanel(GUI gui) {
        this.setForeground(Color.gray);
        repaint();
        revalidate();

        setName("Homepage");

        JButton viewLeader = new JButton("Show Leader Cards");
        viewLeader.addActionListener(e -> gui.renderLeaderCards());

        JButton viewMarket = new JButton("View Market Tray");
        viewMarket.addActionListener(e -> gui.renderMarketTray());

        viewLeader.setBounds(1350, 50, 150, 50);
        viewMarket.setBounds(1350, 150, 150, 50);
        setBounds(0,0,1920-380,1080-230);
        setLayout(null);
        add(viewLeader);
        add(viewMarket);
        setVisible(false);
    }

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
}
