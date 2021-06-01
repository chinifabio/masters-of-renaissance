package it.polimi.ingsw.view.gui.panels;

import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.view.gui.GUI;

import java.io.IOException;

public class OtherPlayersPanel extends GuiPanel{

    public OtherPlayersPanel(GUI gui) {
        super(gui);
    }

    @Override
    public void reactToPacket(Packet packet) throws IOException {

    }
}
