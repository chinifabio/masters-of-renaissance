package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.EndTurnCommand;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.panels.movePanels.MoveResourcesPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class OtherPlayersPanel extends GuiPanel{

    private Image personalBoard;

    public OtherPlayersPanel(GUI gui){
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
        this.setOpaque(false);

        InputStream is = getClass().getResourceAsStream("/board.png");
        if (is == null) try {
            throw new IOException("board.png not found");
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
        //Mettere il player
        WarehousePanel warehousePanel = new WarehousePanel(gui, gui.model.getMe());

        JPanel devSlot = new DevSlotPanel(gui);
        JPanel trackPanel = null;
        try {
            trackPanel = new FaithTrackPanel(gui);
        } catch (IOException e) {
            e.printStackTrace();
        }

        GridBagConstraints c = new GridBagConstraints();



        c.gridy = 1;
        boardPanel.add(warehousePanel, c);

        c.gridx = 1;
        boardPanel.add(Box.createRigidArea(new Dimension(200,400)), c);

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

        JButton viewLeader = new JButton("View Leader Cards");
        viewLeader.addActionListener(e -> gui.switchPanels(new LeaderPanel(gui)));

        JButton viewMarket = new JButton("View Market Tray");
        viewMarket.addActionListener(e -> gui.switchPanels(new MarketPanel(gui)));

        JButton viewGrid = new JButton("View DevCard's grid");
        viewGrid.addActionListener(e -> gui.switchPanels(new CardsGridPanel(gui)));

        JButton viewPlayer = new JButton("View Other Player");
        viewPlayer.addActionListener(e -> gui.switchPanels(new OtherPlayersPanel(gui)));

        JButton activateProduction = new JButton("Productions");
        activateProduction.addActionListener(e -> gui.switchPanels(new ProductionsPanel(gui)));

        JButton moveResources = new JButton("Move Resources");
        moveResources.addActionListener(e -> gui.switchPanels(new MoveResourcesPanel(gui)));

        JButton endTurn = new JButton("EndTurn");
        endTurn.addActionListener(e -> {
            gui.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new EndTurnCommand().jsonfy()));
            try {
                gui.switchPanels(new PersonalBoardPanel(gui));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        bottomPanel.setBackground(GUI.borderColor);
        buttons.setOpaque(false);



        setPreferredSize(new Dimension(gui.width-300,  gui.height));
        buttons.add(Box.createRigidArea(new Dimension(0,70)));
        buttons.add(viewLeader);
        buttons.add(viewMarket);
        buttons.add(viewGrid);
        buttons.add(moveResources);
        buttons.add(activateProduction);
        buttons.add(viewPlayer);
        buttons.add(endTurn);
        setVisible(true);


        buttons.setPreferredSize(new Dimension(1720, 100));

        this.add(boardPanel);

        JPanel buffer = new BufferPanel(gui);
        buffer.setPreferredSize(new Dimension(400,100));
        bottomPanel.add(Box.createRigidArea(new Dimension(30,0)));
        bottomPanel.add(buffer);
        bottomPanel.add(buttons);

        this.add(bottomPanel, BorderLayout.SOUTH);

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
