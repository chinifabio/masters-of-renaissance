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
        g.drawString("Insert your nickname:", 680, 640);
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
        int height = 1080-230;
        g.drawImage(img, 0, -1,width,height, null);
    }

    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */
    public LogoPanel() {
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
        this.setVisible(true);

    }
}
