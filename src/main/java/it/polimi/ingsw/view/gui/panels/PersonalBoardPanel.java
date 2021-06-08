package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.EndTurnCommand;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.panels.movePanels.MoveResourcesPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PersonalBoardPanel  extends GuiPanel {

    private  Image personalBoard;

    public PersonalBoardPanel(GUI gui) throws IOException {
        super(gui);

        InputStream is = getClass().getResourceAsStream("/board.png");
        if (is == null) throw new IOException("board.png not found");
        personalBoard = ImageIO.read(is);

        JPanel boardPanel = new JPanel();
        boardPanel.setOpaque(false);
        boardPanel.setLayout(new GridBagLayout());
        WarehousePanel warehousePanel = new WarehousePanel(gui, gui.model.getMe());

        JPanel devSlot = new DevSlotPanel(gui, gui.model.getMe());
        JPanel trackPanel = new FaithTrackPanel(gui, gui.model.getMe());
        JPanel extraDepot = new ExtraDepotPanel(gui, gui.model.getMe());

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

        JButton viewLeader = new JButton("View Leader Cards");
        viewLeader.addActionListener(e -> gui.switchPanels(new LeaderPanel(gui)));

        JButton viewMarket = new JButton("View Market Tray");
        viewMarket.addActionListener(e -> gui.switchPanels(new MarketPanel(gui)));

        JButton viewGrid = new JButton("View DevCard's grid");
        viewGrid.addActionListener(e -> gui.switchPanels(new CardsGridPanel(gui)));



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

        List<String> players = new ArrayList<>(gui.model.getPlayers().keySet());
        players.remove(gui.model.getMe());
        players.remove("Lorenzo il Magnifico");

        if (!players.isEmpty()){
            JButton viewPlayer = new JButton("View other Player");
            viewPlayer.addActionListener(e -> {
                String toSee = (String) JOptionPane.showInputDialog(null, "Who do you want to see?", "View other player",
                        JOptionPane.QUESTION_MESSAGE, null,
                        players.toArray(), players.get(0));
                if (toSee != null) {
                    gui.switchPanels(new OtherPlayersPanel(gui, toSee));
                }
            });
            buttons.add(viewPlayer);
        } else {
            buttons.add(Box.createRigidArea(new Dimension(100,0)));
        }

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
