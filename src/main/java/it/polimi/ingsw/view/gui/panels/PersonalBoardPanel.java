package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.EndTurnCommand;
import it.polimi.ingsw.communication.packet.commands.FaithPointCheatCommand;
import it.polimi.ingsw.communication.packet.commands.ProductionCommand;
import it.polimi.ingsw.communication.packet.commands.ResourceCheatCommand;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.panels.buycardPanels.CardsGridPanel;
import it.polimi.ingsw.view.gui.panels.graphicComponents.*;
import it.polimi.ingsw.view.gui.panels.movePanels.MoveResourcesPanel;
import it.polimi.ingsw.view.gui.panels.movePanels.ProductionsPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PersonalBoardPanel  extends GuiPanel {

    public PersonalBoardPanel(GUI gui) {
        super(gui);
    }

    @Override
    public JPanel update() throws IOException {
        JPanel result = new BgJPanel("/board.png", GUI.width-NotifyPanel.width+50, GUI.height-120);

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


        List<String> players = new ArrayList<>(gui.model.getPlayers().keySet());
        players.remove(gui.model.getMe());
        players.remove("Lorenzo il Magnifico");

        result.setName("Homepage");
        result.setLayout(new BorderLayout());

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
        activateProduction.addActionListener(e -> {
            gui.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new ProductionCommand().jsonfy()));
            gui.switchPanels(new ProductionsPanel(gui));
        });

        JButton moveResources = new JButton("Move Resources");
        moveResources.addActionListener(e -> gui.switchPanels(new MoveResourcesPanel(gui)));

        JButton endTurn = new JButton("EndTurn");
        endTurn.addActionListener(e -> gui.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new EndTurnCommand().jsonfy())));

        bottomPanel.setBackground(GUI.borderColor);
        buttons.setOpaque(false);


        result.setPreferredSize(new Dimension(GUI.gameWidth,  GUI.gameHeight));
        buttons.add(Box.createRigidArea(new Dimension(0,60)));
        buttons.add(viewLeader);
        buttons.add(viewMarket);
        buttons.add(viewGrid);
        buttons.add(moveResources);
        buttons.add(activateProduction);


        if (!players.isEmpty()) {
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
        }

        buttons.add(endTurn);
        result.setVisible(true);


        buttons.setPreferredSize(new Dimension(1720, 100));

        result.add(boardPanel);

        JPanel buffer = new BufferPanel(gui);
        buffer.setPreferredSize(new Dimension(400,100));
        bottomPanel.add(Box.createRigidArea(new Dimension(35,0)));
        bottomPanel.add(buffer);
        bottomPanel.add(buttons);

        if (!players.isEmpty()) {
            if (gui.model.getPlayerOrder().get(0).equals(gui.getModel().getMe())) {
                JLabel inkWell = new JLabel();
                Image ink;
                InputStream is = getClass().getResourceAsStream("/inkwell.png");
                assert is != null;
                ink = GUI.getScaledImage(ImageIO.read(is), 60, 70);
                ImageIcon imgIcon = new ImageIcon(ink);
                inkWell.setIcon(imgIcon);
                bottomPanel.add(inkWell);
            }
        }

        result.add(bottomPanel, BorderLayout.SOUTH);
        result.setBackground(GUI.borderColor);

        result.repaint();
        result.revalidate();

        return result;
    }
}

class KeyCheats implements KeyListener {

    private final GUI gui;

    public KeyCheats(GUI gui) {
        this.gui=gui;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        System.out.println("pulsante");
        if(key == KeyEvent.VK_F4) {
            gui.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new FaithPointCheatCommand(1).jsonfy()));
        }
        if (key == KeyEvent.VK_F5) {
            gui.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new FaithPointCheatCommand(5).jsonfy()));
        }
        if(key == KeyEvent.VK_F6) {
            gui.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new ResourceCheatCommand().jsonfy()));
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }
}