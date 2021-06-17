package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.view.gui.GUI;

import javax.swing.*;
import java.io.IOException;

public class LoadingPanel extends GuiPanel {
    public LoadingPanel(GUI gui) {
        super(gui);
    }

    @Override
    public JPanel update() throws IOException {
        JPanel result = new JPanel();

        result.add(new JLabel("aspetta trimone"));

        return result;
    }
}
