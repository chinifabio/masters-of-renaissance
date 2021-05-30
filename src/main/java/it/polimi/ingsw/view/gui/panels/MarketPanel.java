package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.view.gui.GUI;

import javax.swing.*;
import java.io.IOException;

public class MarketPanel extends GuiPanel {
    public MarketPanel(GUI gui) {
        super(gui);

        //questa roba è orribile.
        JLabel labelTemp0 = new JLabel();
        JLabel labelTemp1 = new JLabel();
        JLabel labelTemp2 = new JLabel();
        String test0 = gui.model.getTray().toString(0);
        String test1 = gui.model.getTray().toString(1);
        String test2 = gui.model.getTray().toString(2);
        labelTemp0.setText(test0);
        labelTemp1.setText(test1);
        labelTemp2.setText(test2);
        labelTemp0.setBounds(300,100,600,50);
        labelTemp1.setBounds(300,125,600,50);
        labelTemp2.setBounds(300,150,600,50);
        add(labelTemp0);
        add(labelTemp1);
        add(labelTemp2);
        
        JButton back = new JButton("Return to PB");
        back.setBounds(20,20,150,40);
        back.addActionListener(e -> {
            try {
                gui.switchPanels(new PersonalBoardPanel(gui));
            } catch (IOException ioException) {
                System.out.println("cant come back quitting");
                System.exit(-1);
            }
        });

        add(back);
        setLayout(null);
    }

    @Override
    public void reactToPacket(Packet packet) throws IOException {

    }
}
