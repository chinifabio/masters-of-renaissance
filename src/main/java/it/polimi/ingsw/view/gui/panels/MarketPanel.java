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
import it.polimi.ingsw.view.gui.panels.graphicComponents.BgJPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the GUI Panel of the MarketTray
 */
public class MarketPanel extends GuiPanel {

    /**
     * This is the constructor of the class
     * @param gui is the GUI that contains all the info needed to create the Market Tray Panel
     */
    public MarketPanel(GUI gui) {
        super(gui);
    }

    /**
     * This method update the current panel after a change
     *
     * @return the current Panel updated
     * @throws IOException if there is an I/O problem
     */
    @Override
    public JPanel update() throws IOException {

        JPanel result = new BgJPanel("/brickBackground.png",GUI.width-370, GUI.height-78,35,35);
        result.setPreferredSize(new Dimension(GUI.gameWidth, GUI.gameHeight));
        result.setOpaque(false);

        JPanel bigPanel = new JPanel();
        bigPanel.setOpaque(false);
        bigPanel.setLayout(new BoxLayout(bigPanel, BoxLayout.Y_AXIS));

        InputStream slideUrl = getClass().getResourceAsStream("/MarketTrayImages/MarblesPNG/" + gui.model.getTray().getSlideMarble().getColor() + ".png");
        assert slideUrl != null;
        Image scaledSlide = GUI.getScaledImage(ImageIO.read(slideUrl), 35, 35);
        ImageIcon slideIcon = new ImageIcon(scaledSlide);

        //--------FORMATTING----------
        bigPanel.add(Box.createRigidArea(new Dimension(0, 100)));

        //--------MARBLE GRID----------
        JPanel marblePanel = new JPanel();
        JPanel tray = new BgJPanel("/MarketTrayImages/MarketTrayPNG.png", 397, 506);
        tray.setPreferredSize(new Dimension(397, 506));

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
        marblePanel.setBorder(BorderFactory.createEmptyBorder(63, 180, 80, 80));
        marblePanel.setOpaque(false);
        tray.add(marblePanel);
        tray.setAlignmentX(Component.CENTER_ALIGNMENT);
        tray.setOpaque(false);
        bigPanel.add(tray);

        //-------CONVERSION POWERS------

        List<MarbleColor> temp = new ArrayList<>(gui.getModel().getConversion(gui.getModel().getMe()));
        if(!temp.isEmpty()){
            JPanel preConv = new JPanel();
            JLabel textConv = new JLabel("Available conversion:");
            preConv.setOpaque(false);
            preConv.add(textConv);
            textConv.setForeground(Color.WHITE);
            bigPanel.add(preConv);
            JPanel convMarbles = new JPanel();
            convMarbles.setOpaque(false);
            convMarbles.setLayout(new BoxLayout(convMarbles,BoxLayout.X_AXIS));
            convMarbles.add(Box.createRigidArea(new Dimension(140,0)));
            convMarbles.setPreferredSize(new Dimension(150,50));
            for(MarbleColor marble : temp){
                JPanel conv = new BgJPanel("/MarketTrayImages/MarblesPNG/" + marble.name() + ".png",35,35);
                conv.setPreferredSize(new Dimension(30,30));
                conv.setOpaque(false);
                convMarbles.add(conv);
            }
            if(temp.size()==2){
                convMarbles.add(Box.createRigidArea(new Dimension(100,0)));
            }
            bigPanel.add(convMarbles);
        }


        //--------BACK BUTTON----------
        JButton back = new JButton("Return to PB");

        back.addActionListener(e -> gui.switchPanels(new PersonalBoardPanel(gui)));
        JPanel backB = new JPanel();
        backB.add(back);
        backB.add(Box.createRigidArea(new Dimension(10,0)));
        backB.setOpaque(false);
        bigPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        bigPanel.add(backB);

        result.add(bigPanel);
        return result;
    }


    /**
     * This method create the Panel of each Marble of the Market Tray
     * @param index is the Tray's index where the marble is located
     * @param color is the color of the Marble
     * @return the JPanel of the Marble
     */
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

        MarbleColor[] colors = gui.getModel().getConversionArray(gui.getModel().getMe());

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
                    if(selected != null) {
                        if (selected.equals(colors[0])) gui.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new PaintMarbleCommand(0, index).jsonfy()));
                        else gui.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new PaintMarbleCommand(1, index).jsonfy()));
                    }
                });
            }
            else{
                button.addActionListener(e -> JOptionPane.showMessageDialog(null,"You can't paint marbles!"));
            }
        }
        panel.setVisible(true);
        return panel;
    }

}
