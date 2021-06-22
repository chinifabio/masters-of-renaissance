package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.EndTurnCommand;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.panels.buycardPanels.CardsGridPanel;
import it.polimi.ingsw.view.gui.panels.graphicComponents.*;
import it.polimi.ingsw.view.gui.panels.movePanels.MoveResourcesPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        activateProduction.addActionListener(e -> gui.switchPanels(new ProductionsPanel(gui)));

        JButton moveResources = new JButton("Move Resources");
        moveResources.addActionListener(e -> gui.switchPanels(new MoveResourcesPanel(gui)));

        JButton endTurn = new JButton("EndTurn");
        endTurn.addActionListener(e -> {
            //TODO FA FARE CON FABIO CHE LO FARA' MEGLIO
            /*
            if (players.isEmpty()) {
                ImageIcon icon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/SoloActionTokenImages/" + gui.getModel().getSoloToken().getCardID() + ".png")));
                JOptionPane.showMessageDialog(
                        null,
                        "This is the first Action Token",
                        "Solo Action Token", JOptionPane.INFORMATION_MESSAGE,
                        icon);
            }

             */
            gui.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new EndTurnCommand().jsonfy()));
            gui.switchPanels(new PersonalBoardPanel(gui)); // todo try with this instead of creating a new instance
        });

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
        bottomPanel.add(Box.createRigidArea(new Dimension(30,0)));
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
