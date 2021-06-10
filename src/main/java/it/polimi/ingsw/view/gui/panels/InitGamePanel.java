package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.ChooseResourceCommand;
import it.polimi.ingsw.communication.packet.commands.DiscardLeaderCommand;
import it.polimi.ingsw.communication.packet.commands.EndTurnCommand;
import it.polimi.ingsw.litemodel.litecards.LiteLeaderCard;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.view.gui.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class InitGamePanel extends GuiPanel {

    public InitGamePanel(GUI gui) throws IOException {
        super(gui);
        setOpaque(false);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(Box.createRigidArea(new Dimension(0, 50)));

        JPanel leader = new JPanel();
        leader.setLayout(new BoxLayout(leader, BoxLayout.X_AXIS));
        for (LiteLeaderCard card : gui.model.getLeader(gui.model.getMe())){
            leader.add(new LeaderCardButton(card.getCardID(), gui));
        }
        add(leader);

        add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel res = new JPanel();
        res.setLayout(new BoxLayout(res, BoxLayout.X_AXIS));
        res.setOpaque(false);

        res.add(new ResourceButton(ResourceType.COIN, gui));
        res.add(new ResourceButton(ResourceType.SHIELD, gui));
        res.add(new ResourceButton(ResourceType.SERVANT, gui));
        res.add(new ResourceButton(ResourceType.STONE, gui));

        add(res);

        add(Box.createRigidArea(new Dimension(0, 30)));

        JButton done = new JButton("DONE");
        done.addActionListener(e -> gui.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new EndTurnCommand().jsonfy())));
        done.setAlignmentX(CENTER_ALIGNMENT);

        add(done);
    }

    @Override
    public void reactToPacket(Packet packet) throws IOException {
        switch (packet.header) {
            case OK -> {
                gui.switchPanels(new InitGamePanel(gui));
                gui.notifyPlayer(packet.body);
            }
            case INVALID -> gui.notifyPlayerError(packet.body);
            case GAME_START -> gui.switchPanels(new PersonalBoardPanel(gui));
        }
    }
}

class LeaderCardButton extends JButton {

    public LeaderCardButton(String id, GUI gui) throws IOException {
        InputStream url = this.getClass().getResourceAsStream("/LeaderCardsImages/" + id + ".png");
        BufferedImage img;
        assert url != null;
        img = ImageIO.read(url);

        addActionListener(e -> gui.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new DiscardLeaderCommand(id).jsonfy())));

        Image scaledImage = GUI.getScaledImage(img, 462/2, 698/2);
        ImageIcon icon1 = new ImageIcon(scaledImage);
        setIcon(icon1);

        setAlignmentY(Component.CENTER_ALIGNMENT);

        setPreferredSize(new Dimension(462/2, 698/2));

        setVisible(true);
        setContentAreaFilled(false);
        setOpaque(false);
    }
}

class ResourceButton extends JButton {
    private final ResourceType res;

    ResourceButton(ResourceType res, GUI gui) {
        this.res = res;

        InputStream url = this.getClass().getResourceAsStream("/WarehouseRes/" + res.name().toLowerCase() + ".png");
        BufferedImage img = null;
        try {
            assert url != null;
            img = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        addActionListener(e ->  {
            DepotSlot depot = (DepotSlot) JOptionPane.showInputDialog(null, "Select the depot \n where store the resource", "Place Resource",
                    JOptionPane.QUESTION_MESSAGE, null,
                    new DepotSlot[]{DepotSlot.TOP, DepotSlot.MIDDLE, DepotSlot.BOTTOM} , DepotSlot.TOP);
            if (depot != null) {
                gui.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new ChooseResourceCommand(depot, res).jsonfy()));
            }
        });

        Image scaledImage = GUI.getScaledImage(img, 50, 50);
        ImageIcon icon1 = new ImageIcon(scaledImage);
        setIcon(icon1);

        setAlignmentY(Component.CENTER_ALIGNMENT);

        setVisible(true);
        setContentAreaFilled(false);
        setOpaque(false);
    }
}
