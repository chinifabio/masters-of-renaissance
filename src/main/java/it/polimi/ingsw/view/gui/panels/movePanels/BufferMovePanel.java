package it.polimi.ingsw.view.gui.panels.movePanels;


import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.MoveDepotCommand;
import it.polimi.ingsw.litemodel.LiteResource;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.panels.GuiPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class BufferMovePanel extends GuiPanel {

    private DepotSlot slot;

    Image buffer;

    public BufferMovePanel(GUI gui){
        super(gui);

        InputStream is = getClass().getResourceAsStream("/buffer.png");
        if (is == null) try {
            throw new IOException("buffer.png not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            buffer = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        DepotSlot[] possibleValues = { DepotSlot.TOP, DepotSlot.MIDDLE, DepotSlot.BOTTOM, DepotSlot.BUFFER, DepotSlot.STRONGBOX, DepotSlot.SPECIAL1, DepotSlot.SPECIAL2};


        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createRigidArea(new Dimension(0,20)));
        this.setPreferredSize(new Dimension(275,100));
        this.setOpaque(false);

        JPanel bigPanel = new JPanel();
        JPanel bufferPanel = new JPanel();

        bigPanel.setPreferredSize(new Dimension(400, 100));

        for (LiteResource res : gui.model.getDepot(gui.model.getMe(), DepotSlot.BUFFER).getResourcesInside()){
            JPanel resource = new JPanel();
            resource.setLayout(new OverlayLayout(resource));
            resource.setOpaque(false);
            JLabel amount = new JLabel();
            amount.setFont(new Font(amount.getName(), Font.BOLD, 16));
            if (res.getType() == ResourceType.SERVANT){
                amount.setForeground(Color.WHITE);
            } else {
                amount.setForeground(Color.BLACK);
            }

            amount.setBackground(new Color(0, 0, 0, 10));
            amount.setText(String.valueOf(res.getAmount()));

            JButton image = new JButton();
            createResourceLabel(image, GUI.resourceImages.get(res.getType()));

            image.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    slot = null;
                    int value = 0;
                    boolean valid = true;

                    DepotSlot slot = (DepotSlot) JOptionPane.showInputDialog(null, "Where do you wanto to move this resource? ", "Move resources",
                            JOptionPane.QUESTION_MESSAGE, null,
                            possibleValues, possibleValues[0]);

                    if (slot != null) {
                        String str = JOptionPane.showInputDialog(null, "How many resources do you want to move?");
                        try {
                            value = Integer.parseInt(str);
                        } catch (Exception notParsable) {
                            JOptionPane.showMessageDialog(null, "Please use a valid number!");
                            valid = false;
                        }

                        if (valid) {
                            gui.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new MoveDepotCommand(DepotSlot.BUFFER, slot, ResourceBuilder.buildFromType(res.getType(), value)).jsonfy()));
                        }
                    }
                }
            });

            resource.add(amount);
            image.setAlignmentX(0.31f);
            image.setAlignmentY(0.55f);
            resource.add(image);

            resource.add(Box.createRigidArea(new Dimension(50,0)));
            bufferPanel.add(resource);
        }

        bufferPanel.setOpaque(false);
        bigPanel.add(bufferPanel);

        bigPanel.setOpaque(false);

        this.add(bigPanel);

    }

    public void createResourceLabel(JButton button, String resource){
        InputStream url = this.getClass().getResourceAsStream("/" + resource);
        BufferedImage img = null;
        try {
            assert url != null;
            img = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Image scaledImage = GUI.getScaledImage(img, 42, 42);
        ImageIcon icon1 = new ImageIcon(scaledImage);
        button.setIcon(icon1);
        button.setPreferredSize(new Dimension(44, 44));
        button.setContentAreaFilled(false);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = 250;
        int height = 100;
        g.drawImage(buffer, 15, 0,width,height, null);
    }

    @Override
    public void reactToPacket(Packet packet) throws IOException {
        switch (packet.header) {
            case OK -> gui.switchPanels(new BufferMovePanel(gui));
            case INVALID -> gui.notifyPlayerError(packet.body);
        }
    }
}
