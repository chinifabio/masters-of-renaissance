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

public class MarketPanel extends GuiPanel {

    public MarketPanel(GUI gui) {
        super(gui);
    }

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

        //--------BACK BUTTON----------
        JButton back = new JButton("Return to PB");

        back.addActionListener(e -> gui.switchPanels(new PersonalBoardPanel(gui)));
        bigPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        bigPanel.add(back);
        result.add(bigPanel);

        return result;
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
                button.addActionListener(e -> JOptionPane.showMessageDialog(null,"You can't paint marbles!"));
            }
        }
        panel.setVisible(true);
        return panel;
    }

}
