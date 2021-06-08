package it.polimi.ingsw.view.gui.panels;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.view.gui.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;

public class OtherPlayersPanel extends GuiPanel{

    private Image personalBoard;

    public OtherPlayersPanel(GUI gui, String player){
        super(gui);


        //--------BACK BUTTON----------
        JPanel backPanel = new JPanel();
        JButton back = new JButton("Return to PB");
        back.addActionListener(e -> {
            try {
                gui.switchPanels(new PersonalBoardPanel(gui));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        backPanel.add(back);
        backPanel.setOpaque(false);

        this.add(backPanel);

        InputStream is = getClass().getResourceAsStream("/otherBoard.png");
        if (is == null) try {
            throw new IOException("otherBoard.png not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            personalBoard = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel boardPanel = new JPanel();
        boardPanel.setOpaque(false);
        boardPanel.setLayout(new GridBagLayout());

        JPanel extraDepot = new ExtraDepotPanel(gui, player);
        WarehousePanel warehousePanel = new WarehousePanel(gui, player);

        JPanel devSlot = new DevSlotPanel(gui, player);
        JPanel trackPanel = null;
        try {
            trackPanel = new FaithTrackPanel(gui, player);
        } catch (IOException e) {
            e.printStackTrace();
        }

        GridBagConstraints c = new GridBagConstraints();



        c.gridy = 1;
        boardPanel.add(warehousePanel, c);

        c.gridx = 1;
        boardPanel.add(extraDepot, c);

        c.gridx = 2;
        boardPanel.add(devSlot, c);


        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        boardPanel.add(trackPanel, c);

        c.gridy = 2;
        boardPanel.add(Box.createRigidArea(new Dimension(1920-380-200,25)), c);


        setName("Homepage");
        this.setLayout(new BorderLayout());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        JPanel buttons = new JPanel();

        JButton otherLeader = new JButton("View " + player + "'s Leaders");
        otherLeader.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.switchPanels(new OtherPlayerLeaderPanel(gui, player));
            }
        });
        buttons.add(otherLeader);
        buttons.add(backPanel);

        bottomPanel.setBackground(GUI.borderColor);
        buttons.setOpaque(false);



        setPreferredSize(new Dimension(gui.width-300,  gui.height));
        buttons.add(Box.createRigidArea(new Dimension(0,70)));

        setVisible(true);


        buttons.setPreferredSize(new Dimension(1720, 100));

        this.add(boardPanel);

        JPanel panelText = new JPanel();
        panelText.setOpaque(false);
        panelText.setLayout(new BoxLayout(panelText,BoxLayout.Y_AXIS));
        panelText.add(Box.createRigidArea(new Dimension(0,40)));
        JTextArea text = new JTextArea();
        text.setText("This is the Personal Board of " + player);
        text.setOpaque(false);
        text.setFont(new Font("Times New Roman",Font.ITALIC,18));
        text.setEditable(false);
        panelText.add(text);

        JPanel buffer = new BufferPanel(gui);
        buffer.setPreferredSize(new Dimension(400,100));
        bottomPanel.add(Box.createRigidArea(new Dimension(30,0)));
        bottomPanel.add(buffer);
        bottomPanel.add(Box.createRigidArea(new Dimension(300,0)));
        bottomPanel.add(panelText);
        bottomPanel.add(buttons);

        this.add(bottomPanel, BorderLayout.SOUTH);

        this.setBackground(GUI.borderColor);

        repaint();
        revalidate();
    }

    /**
     * Draw the background
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = gui.width-250;
        int height = gui.height-100;
        g.drawImage(personalBoard, 0, -10,width,height, null);
    }

    @Override
    public void reactToPacket(Packet packet) {
        switch (packet.header){
            case INVALID -> gui.notifyPlayerError(packet.body);
        }
    }
}
