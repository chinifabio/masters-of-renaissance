package it.polimi.ingsw.view.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class LogoFrame extends JFrame {


    public void paint(Graphics g){
        myDrawImage("LogoMasters.png", g);
        g.drawString("Insert your nickname:", 660, 670);
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
        g.drawImage(img, 5, 29, 1920-380, 1080-265, null);
    }

    public void printIntro(){

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); //Make Fullscreen
        //f.setSize(1007,720);

        JTextField name = new JTextField(20);
        name.setHorizontalAlignment(SwingConstants.CENTER);
        name.setBounds(650,650,200,20);
        Color yellowBackground = new Color(193,173,98);
        name.setBackground(yellowBackground);

        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Nickname detected " + name.getText());
                name.setText(null);
            }
        };

        name.addActionListener(action);
        this.add(name, BorderLayout.CENTER);

        this.setLayout(null);
        this.pack();
        this.setVisible(true);
    }

}
