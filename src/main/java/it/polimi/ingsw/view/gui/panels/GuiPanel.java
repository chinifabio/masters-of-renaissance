package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.view.gui.GUI;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public abstract class GuiPanel {

    protected final GUI gui;

    public GuiPanel(GUI gui) {
        this.gui = gui;
    }

    public abstract JPanel update() throws IOException;
}
