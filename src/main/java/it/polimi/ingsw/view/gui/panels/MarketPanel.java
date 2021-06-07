package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.PaintMarbleCommand;
import it.polimi.ingsw.communication.packet.commands.UseMarketTrayCommand;
import it.polimi.ingsw.litemodel.litemarkettray.LiteMarble;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.MarbleColor;
import it.polimi.ingsw.model.match.markettray.RowCol;
import it.polimi.ingsw.view.gui.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class MarketPanel extends GuiPanel {

    private Image img = null;

    public MarketPanel(GUI gui) {
        super(gui);
        this.setOpaque(false);

        setLayout(new FlowLayout());

        setPreferredSize(new Dimension(1920 - 380 - 200, 1080 - 270));

        try {
            img = ImageIO.read(getClass().getResourceAsStream("/MarketTrayImages/MarketTrayPNG.png"));
        } catch (IOException e) {
            System.exit(-1);
        }

        InputStream slideUrl = this.getClass().getResourceAsStream("/MarketTrayImages/MarblesPNG/" + gui.model.getTray().getSlideMarble().getColor() + ".png");
        BufferedImage slideImg = null;
        try {
            assert slideUrl != null;
            slideImg = ImageIO.read(slideUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image scaledSlide = GUI.getScaledImage(slideImg, 35, 35);
        ImageIcon slideIcon = new ImageIcon(scaledSlide);

        //--------BACK BUTTON----------
        JButton back = new JButton("Return to PB");

        back.addActionListener(e -> {
            try {
                gui.switchPanels(new PersonalBoardPanel(gui));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });


        //--------BACK PANEL----------
        JPanel backPanel = new JPanel();
        backPanel.setOpaque(false);
        backPanel.add(back);
        add(backPanel);

        //--------FORMATTING----------
        add(Box.createRigidArea(new Dimension(150, 0)));

        //--------MARBLE GRID----------
        JPanel marblePanel = new JPanel();
        marblePanel.setLayout(new GridLayout(6, 6));

        //--------SLIDE MARBLE----------
        marblePanel.add(Box.createRigidArea(new Dimension(0, 0)));
        marblePanel.add(Box.createRigidArea(new Dimension(0, 0)));
        marblePanel.add(Box.createRigidArea(new Dimension(0, 0)));
        marblePanel.add(Box.createRigidArea(new Dimension(0, 0)));

        JLabel slideMarble = new JLabel();
        slideMarble.setIcon(slideIcon);

        marblePanel.add(slideMarble);

        marblePanel.add(Box.createRigidArea(new Dimension(0, 0)));

        //--------ROWS----------
        int index=-1;
        for (LiteMarble[] row : gui.model.getTray().getMarbles()) {
            for (LiteMarble liteMarble : row) {
                index++;
                marblePanel.add(drawMarble(index,liteMarble.getColor()));
            }
            marblePanel.add(Box.createRigidArea(new Dimension(0, 0)));
            JButton button = new JButton();
            final int finalI = index/4;
            button.addActionListener(e -> gui.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new UseMarketTrayCommand(RowCol.ROW, finalI).jsonfy())));
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            marblePanel.add(button);

        }

        //--------FORMATTING - EMPTY LINE----------
        marblePanel.add(Box.createRigidArea(new Dimension(0, 0)));
        marblePanel.add(Box.createRigidArea(new Dimension(0, 0)));
        marblePanel.add(Box.createRigidArea(new Dimension(0, 0)));
        marblePanel.add(Box.createRigidArea(new Dimension(0, 0)));
        marblePanel.add(Box.createRigidArea(new Dimension(0, 0)));
        marblePanel.add(Box.createRigidArea(new Dimension(0, 0)));

        //--------BUTTON ARROWS - LAST LINE----------
        for(int i=0;i<4;i++){
            int finalI = i;
            JButton button = new JButton();
            button.addActionListener(e -> gui.socket.send(new Packet(HeaderTypes.DO_ACTION,ChannelTypes.PLAYER_ACTIONS, new UseMarketTrayCommand(RowCol.COL, finalI).jsonfy())));
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            marblePanel.add(button);
        }

        //--------FORMATTING----------
        marblePanel.setBorder(BorderFactory.createEmptyBorder(165, 0, 0, 160));
        marblePanel.setOpaque(false);
        marblePanel.setVisible(true);
        add(marblePanel);
    }

    @Override
    public void reactToPacket(Packet packet) throws IOException {
        switch (packet.header) {
            case OK -> gui.switchPanels(new MarketPanel(gui));
            case INVALID -> gui.notifyPlayerError(packet.body);
        }
    }

    /**
     * Draw the background
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int x = 475;
        int y = 100;
        int width = 397;
        int height = 506;
        g.drawImage(img, x, y, width, height, null);
    }

    private JPanel drawMarble(int index, MarbleColor color) {

        JPanel panel = new JPanel();

        InputStream marbleUrl = this.getClass().getResourceAsStream("/MarketTrayImages/MarblesPNG/" + color.toString() + ".png");
        BufferedImage marbleImg = null;
        try {
            assert marbleUrl != null;
            marbleImg = ImageIO.read(marbleUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Image scaledMarble = GUI.getScaledImage(marbleImg, 35, 35);
        ImageIcon marbleIcon = new ImageIcon(scaledMarble);
        JButton button = new JButton();

        button.setIcon(marbleIcon);
        button.setOpaque(false);
        panel.setOpaque(false);
        panel.add(button);

        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);

        MarbleColor[] colors = gui.model.getConversionArray(gui.model.getMe());

        //--------PAINT MARBLE----------
        if(color.equals(MarbleColor.WHITE)){
            if(colors.length>0){
                button.addActionListener(e -> {
                    MarbleColor selected = (MarbleColor) JOptionPane.showInputDialog(null,
                            "Select the desired color",
                            "PaintMarble",
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            colors,
                            colors[0]);
                    System.out.println(index);
                    if(selected.equals(colors[0])) gui.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new PaintMarbleCommand(0,index).jsonfy()));
                    else gui.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new PaintMarbleCommand(1,index).jsonfy()));
                });
            }
            else{
                button.addActionListener(e -> JOptionPane.showMessageDialog(this,"You can't paint marbles!"));
            }
        }
        panel.setVisible(true);
        return panel;
    }

}
