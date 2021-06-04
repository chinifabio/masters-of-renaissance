package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.view.gui.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class FaithTrackPanel extends GuiPanel {

    HashMap<Integer, Integer> playerPos = new HashMap<>(){{
        put(0,2);
        put(1,2);
        put(2,2);
        put(3,1);
        put(4,0);
        put(5,0);
        put(6,0);
        put(7,0);
        put(8,0);
        put(9,0);
        put(10,1);
        put(11,2);
        put(12,2);
        put(13,2);
        put(14,2);
        put(15,2);
        put(16,2);
        put(17,1);
        put(18,0);
        put(19,0);
        put(20,0);
        put(21,0);
        put(22,0);
        put(23,0);
        put(24,0);
        put(25,0);
    }};

    HashMap<Integer, Integer> topRow = new HashMap<>(){{
        put(4,3);
        put(5,4);
        put(6,5);
        put(7,6);
        put(8,7);
        put(9,8);
        put(18,13);
        put(19,14);
        put(20,15);
        put(21,16);
        put(22,17);
        put(23,18);
        put(24,19);
        put(25,20);
    }};

    HashMap<Integer, Integer> centerRow = new HashMap<>(){{
        put(3,3);
        put(10,8);
        put(17,13);
    }};

    HashMap<Integer, Integer> bottomRow = new HashMap<>(){{
        put(0,1);
        put(1,2);
        put(2,3);
        put(11,8);
        put(12,9);
        put(13,10);
        put(14,11);
        put(15,12);
        put(16,13);
    }};

    int player = gui.model.getPlayerPosition().get(gui.model.getMe());
    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */
    public FaithTrackPanel(GUI gui) throws IOException {
        super(gui);

        InputStream url = this.getClass().getResourceAsStream("/WarehouseRes/faithpoint.png");
        BufferedImage img = null;
        try {
            assert url != null;
            img = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image scaledImage = GUI.getScaledImage(img, 50, 50);
        ImageIcon icon1 = new ImageIcon(scaledImage);
        JLabel PlayerMarker = new JLabel();
        PlayerMarker.setIcon(icon1);

        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(1920-380-200,285));


        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        for (int i = 0; i<20; i++){

            JPanel panel = new JPanel();

            if (i == 0){
                panel.setPreferredSize(new Dimension(32, 52));
            }else if (i == 1){
                panel.setPreferredSize(new Dimension(70, 52));
            } else {
                panel.setPreferredSize(new Dimension(65, 52));
            }

            if (i == 7){
                c.insets = new Insets(0,10,0,10);
            } else if (i == 2){
                c.insets = new Insets(0,0,0,10);
            } else if (i == 12){
                c.insets = new Insets(0,0,0,20);
            }
            panel.setOpaque(false);

            if (gui.model.getPlayerPosition().get("Lorenzo il Magnifico") != null){
                int lorenzo = gui.model.getPlayerPosition().get("Lorenzo il Magnifico");
                if (player == lorenzo){
                    url = this.getClass().getResourceAsStream("/FaithTrackImages/blackAndPlayer.png");
                    img = null;
                    try {
                        assert url != null;
                        img = ImageIO.read(url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    scaledImage = GUI.getScaledImage(img, 50, 50);
                    icon1 = new ImageIcon(scaledImage);
                    PlayerMarker.setIcon(icon1);
                } else {
                    if (playerPos.get(lorenzo) == 0) {
                        if (i == topRow.get(lorenzo)) {
                            InputStream urlLor = this.getClass().getResourceAsStream("/FaithTrackImages/blackCross.png");
                            BufferedImage imgLor = null;
                            try {
                                assert urlLor != null;
                                imgLor = ImageIO.read(urlLor);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Image scaledImageLor = GUI.getScaledImage(imgLor, 50, 50);
                            ImageIcon iconLor = new ImageIcon(scaledImageLor);
                            JLabel LoreMarker = new JLabel();
                            LoreMarker.setIcon(iconLor);
                            panel.add(LoreMarker);
                        }
                    }
                }
            }

            if (playerPos.get(player) == 0){
                if (i == topRow.get(player)){
                    panel.add(PlayerMarker);
                }
            }


            this.add(panel, c);
            c.gridx++;
            c.insets = new Insets(0,0,0,0);
        }

        c.gridy = 1;
        c.gridx = 0;
        for (int i = 0; i<20; i++){
            JPanel panel = new JPanel();
            if (i == 0){
                panel.setPreferredSize(new Dimension(32, 52));
            }else if (i == 1){
                panel.setPreferredSize(new Dimension(70, 52));
            } else {
                panel.setPreferredSize(new Dimension(65, 52));
            }

            if (i == 7){
                c.insets = new Insets(0,10,0,10);
            } else if (i == 2){
                c.insets = new Insets(0,0,0,10);
            } else if (i == 12){
                c.insets = new Insets(0,0,0,20);
            }

            panel.setOpaque(false);

            if (gui.model.getPlayerPosition().get("Lorenzo il Magnifico") != null){
                int lorenzo = gui.model.getPlayerPosition().get("Lorenzo il Magnifico");
                if (player == lorenzo){
                    url = this.getClass().getResourceAsStream("/FaithTrackImages/blackAndPlayer.png");
                    img = null;
                    try {
                        assert url != null;
                        img = ImageIO.read(url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    scaledImage = GUI.getScaledImage(img, 50, 50);
                    icon1 = new ImageIcon(scaledImage);
                    PlayerMarker.setIcon(icon1);
                } else {
                    if (playerPos.get(lorenzo) == 1) {
                        if (i == centerRow.get(lorenzo)) {
                            InputStream urlLor = this.getClass().getResourceAsStream("/FaithTrackImages/blackCross.png");
                            BufferedImage imgLor = null;
                            try {
                                assert urlLor != null;
                                imgLor = ImageIO.read(urlLor);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Image scaledImageLor = GUI.getScaledImage(imgLor, 50, 50);
                            ImageIcon iconLor = new ImageIcon(scaledImageLor);
                            JLabel LoreMarker = new JLabel();
                            LoreMarker.setIcon(iconLor);
                            panel.add(LoreMarker);
                        }
                    }
                }
            }

            if (playerPos.get(player) == 1){
                if (i == centerRow.get(player)){
                    panel.add(PlayerMarker);
                }
            }
            this.add(panel, c);
            c.gridx++;
            c.insets = new Insets(0,0,0,0);

        }

        c.gridy = 2;
        c.gridx = 0;

        for (int i = 0; i<20; i++){
            JPanel panel = new JPanel();
            if (i == 0){
                panel.setPreferredSize(new Dimension(32, 52));
            }else if (i == 1){
                panel.setPreferredSize(new Dimension(70, 52));
            } else {
                panel.setPreferredSize(new Dimension(65, 52));
            }

            if (i == 7){
                c.insets = new Insets(0,10,0,10);
            } else if (i == 2){
                c.insets = new Insets(0,0,0,10);
            } else if (i == 12){
                c.insets = new Insets(0,0,0,20);
            }
            panel.setOpaque(false);

            if (gui.model.getPlayerPosition().get("Lorenzo il Magnifico") != null){
                int lorenzo = gui.model.getPlayerPosition().get("Lorenzo il Magnifico");
                if (player == lorenzo){
                    url = this.getClass().getResourceAsStream("/FaithTrackImages/blackAndPlayer.png");
                    img = null;
                    try {
                        assert url != null;
                        img = ImageIO.read(url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    scaledImage = GUI.getScaledImage(img, 50, 50);
                    icon1 = new ImageIcon(scaledImage);
                    PlayerMarker.setIcon(icon1);
                } else {
                    if (playerPos.get(lorenzo) == 2) {
                        if (i == bottomRow.get(lorenzo)) {
                            InputStream urlLor = this.getClass().getResourceAsStream("/FaithTrackImages/blackCross.png");
                            BufferedImage imgLor = null;
                            try {
                                assert urlLor != null;
                                imgLor = ImageIO.read(urlLor);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Image scaledImageLor = GUI.getScaledImage(imgLor, 50, 50);
                            ImageIcon iconLor = new ImageIcon(scaledImageLor);
                            JLabel LoreMarker = new JLabel();
                            LoreMarker.setIcon(iconLor);
                            panel.add(LoreMarker);
                        }
                    }
                }
            }

            if (playerPos.get(player) == 2){
                if (i == bottomRow.get(player)){
                    panel.add(PlayerMarker);
                }
            }
            this.add(panel, c);
            c.gridx++;
            c.insets = new Insets(0,0,0,0);
        }


        c.gridy = 3;
        c.gridwidth = 24;
        this.add(Box.createRigidArea(new Dimension(0, 95)), c);



        this.setOpaque(false);

        repaint();
        revalidate();

    }

    @Override
    public void reactToPacket(Packet packet) throws IOException {

    }
}
