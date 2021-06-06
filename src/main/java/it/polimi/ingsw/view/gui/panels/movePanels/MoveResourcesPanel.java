package it.polimi.ingsw.view.gui.panels.movePanels;

import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.panels.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MoveResourcesPanel extends GuiPanel {

    public MoveResourcesPanel(GUI gui){
        super(gui);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setOpaque(false);

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));
        middlePanel.setOpaque(false);



        //--------BACK BUTTON----------
        JButton back = new JButton("Return to PB");
        back.addActionListener(e -> {
            try {
                gui.switchPanels(new PersonalBoardPanel(gui));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        //------------BUFFER-------------
        JPanel bufferContainer = new JPanel();
        JPanel buffer = new BufferMovePanel(gui);
        bufferContainer.setLayout(new BoxLayout(bufferContainer, BoxLayout.Y_AXIS));
        buffer.setPreferredSize(new Dimension(275,100));
        bufferContainer.setOpaque(false);
        bufferContainer.add(Box.createRigidArea(new Dimension(0,200)));
        bufferContainer.add(buffer);

        //----------WareHouse------------
        WarehouseMovePanel warehouse = new WarehouseMovePanel(gui);
        warehouse.setPreferredSize(new Dimension(350, 500));
        warehouse.setPainted();



        this.add(back);
        this.add(Box.createRigidArea(new Dimension(0, 100)));
        middlePanel.add(warehouse);
        middlePanel.add(Box.createRigidArea(new Dimension(100, 0)));
        middlePanel.add(bufferContainer);
        this.add(middlePanel);

    }

    @Override
    public void reactToPacket(Packet packet) throws IOException {
        switch (packet.header) {
            case OK -> gui.switchPanels(new MoveResourcesPanel(gui));
            case INVALID -> gui.notifyPlayerError(packet.body);
        }
    }
}
