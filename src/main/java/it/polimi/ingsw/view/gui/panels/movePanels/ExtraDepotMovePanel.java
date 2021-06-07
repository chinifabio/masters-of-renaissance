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
import java.util.Arrays;
import java.util.List;

public class ExtraDepotMovePanel extends GuiPanel {

    private DepotSlot destDepot;

    DepotSlot[] initValueDepot = { DepotSlot.TOP, DepotSlot.MIDDLE, DepotSlot.BOTTOM, DepotSlot.BUFFER};

    public ExtraDepotMovePanel(GUI gui) {
        super(gui);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(200,400));

        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.red);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JPanel firstDepot = new JPanel();
        firstDepot.setPreferredSize(new Dimension(200,50));
        if (gui.model.getDepot(gui.model.getMe(), DepotSlot.SPECIAL1) != null) {
            firstDepot = new it.polimi.ingsw.view.gui.panels.LeaderDepotPanel(gui.model.getDepot(gui.getModel().getMe(), DepotSlot.SPECIAL1).getResourcesInside().get(0).getType());
            insertResourceInDepot(firstDepot, DepotSlot.SPECIAL1, gui.model.getMe());
        }

        firstDepot.setOpaque(false);
        topPanel.add(firstDepot);

        JPanel secondDepot = new JPanel();

        secondDepot.setPreferredSize(new Dimension(200,50));
        if (gui.model.getDepot(gui.model.getMe(), DepotSlot.SPECIAL2) != null) {
            secondDepot = new it.polimi.ingsw.view.gui.panels.LeaderDepotPanel(gui.model.getDepot(gui.getModel().getMe(), DepotSlot.SPECIAL2).getResourcesInside().get(0).getType());
            insertResourceInDepot(secondDepot, DepotSlot.SPECIAL2, gui.model.getMe());

        }
        secondDepot.setOpaque(false);
        topPanel.add(secondDepot);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setPreferredSize(new Dimension(200,200));

        topPanel.setOpaque(false);
        this.add(topPanel);
        this.add(bottomPanel);
        this.setOpaque(false);
    }

    @Override
    public void reactToPacket(Packet packet) throws IOException {

    }

    public void insertResourceInDepot(JPanel depot, DepotSlot slot, String player){
        depot.add(Box.createRigidArea(new Dimension(5,0)));
        LiteResource tempRes = gui.model.getDepot(player, slot).getResourcesInside().get(0);

        for (int i = 0; i <  tempRes.getAmount(); i++){
            JButton label = new JButton();
            createResourceLabel(label, GUI.resourceImages.get(tempRes.getType()));
            depot.add(Box.createRigidArea(new Dimension(20,0)));

            if (slot != DepotSlot.STRONGBOX){

                label.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        destDepot = null;

                        List<DepotSlot> possibleValuesDepots = new ArrayList<>(Arrays.asList(initValueDepot));

                        if (gui.getModel().getDepot(gui.model.getMe(), DepotSlot.SPECIAL1) != null){
                            possibleValuesDepots.add(DepotSlot.SPECIAL1);
                        }
                        if (gui.getModel().getDepot(gui.model.getMe(), DepotSlot.SPECIAL2) != null){
                            possibleValuesDepots.add(DepotSlot.SPECIAL2);
                        }

                        DepotSlot destDepot = (DepotSlot) JOptionPane.showInputDialog(null, "Where do you want to move this resource? ", "Move resources",
                                JOptionPane.QUESTION_MESSAGE, null,
                                possibleValuesDepots.toArray(), possibleValuesDepots.get(0));
                        if (destDepot != null) {
                            gui.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new MoveDepotCommand(slot, destDepot, ResourceBuilder.buildFromType(tempRes.getType(), 1)).jsonfy()));
                        }
                    }
                });
            }

            depot.add(label);
        }

        if (tempRes.getAmount() == 1){
            JButton label = new JButton();
            createResourceLabel(label, GUI.resourceImages.get(ResourceType.EMPTY));
            depot.add(Box.createRigidArea(new Dimension(20,0)));
            label.setEnabled(false);
            depot.add(label);
        }
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

        Image scaledImage = GUI.getScaledImage(img, 45, 45);
        ImageIcon icon1 = new ImageIcon(scaledImage);
        button.setIcon(icon1);
        button.setPreferredSize(new Dimension(47, 47));
        button.setContentAreaFilled(false);
    }
}
