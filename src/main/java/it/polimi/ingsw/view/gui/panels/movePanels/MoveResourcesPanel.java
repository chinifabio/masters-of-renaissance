package it.polimi.ingsw.view.gui.panels.movePanels;

import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.panels.*;
import it.polimi.ingsw.view.gui.panels.graphicComponents.BgJPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MoveResourcesPanel extends GuiPanel {

    public MoveResourcesPanel(GUI gui){
        super(gui);
    }

    @Override
    public JPanel update() throws IOException {
        JPanel background = new BgJPanel("/brickBackground.png",GUI.width-370, GUI.height-78,35,35);
        JPanel result = new JPanel();

        background.setPreferredSize(new Dimension(GUI.gameWidth, GUI.gameHeight));

        result.setLayout(new BoxLayout(result, BoxLayout.Y_AXIS));
        result.setOpaque(false);

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));
        middlePanel.setOpaque(false);



        //--------BACK BUTTON----------
        JPanel backPanel = new JPanel();
        JButton back = new JButton("Return to PB");
        back.addActionListener(e -> gui.switchPanels(new PersonalBoardPanel(gui)));
        backPanel.add(back);
        backPanel.add(Box.createRigidArea(new Dimension(900,0)));
        backPanel.setOpaque(false);


        //------------BUFFER-------------
        JPanel bufferContainer = new JPanel();
        JPanel buffer = new BufferMovePanel(gui);
        bufferContainer.setLayout(new BoxLayout(bufferContainer, BoxLayout.Y_AXIS));
        buffer.setPreferredSize(new Dimension(275,100));
        bufferContainer.setOpaque(false);
        bufferContainer.add(Box.createRigidArea(new Dimension(0,200)));
        bufferContainer.add(buffer);

        JPanel bufferPanel = new JPanel();
        bufferPanel.add(bufferContainer);
        bufferPanel.setOpaque(false);

        //----------WareHouse------------
        JPanel warehousPanel = new JPanel();
        WarehouseMovePanel warehouse = new WarehouseMovePanel(gui);
        warehouse.setPreferredSize(new Dimension(350, 500));
        warehousPanel.add(warehouse);
        warehousPanel.setOpaque(false);

        //--------ExtraDepot------------
        JPanel extraPanel = new JPanel();
        ExtraDepotMovePanel extraDepotPanel = new ExtraDepotMovePanel(gui);
        extraPanel.setPreferredSize(new Dimension(350,100));
        extraDepotPanel.setOpaque(false);
        extraPanel.add(extraDepotPanel);
        extraPanel.setOpaque(false);

        JPanel depotAndBufferPanel = new JPanel();
        depotAndBufferPanel.setLayout(new BoxLayout(depotAndBufferPanel, BoxLayout.Y_AXIS));
        depotAndBufferPanel.add(extraPanel);
        depotAndBufferPanel.add(bufferPanel);
        depotAndBufferPanel.setOpaque(false);


        result.add(Box.createRigidArea(new Dimension(0, 100)));
        result.add(backPanel);
        result.add(Box.createRigidArea(new Dimension(0, 100)));
        middlePanel.add(warehousPanel);
        middlePanel.add(Box.createRigidArea(new Dimension(100, 0)));
        middlePanel.add(depotAndBufferPanel);
        result.add(middlePanel);

        background.setOpaque(false);
        result.setOpaque(false);
        background.add(result);
        return background;
    }
}
