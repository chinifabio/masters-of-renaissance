package it.polimi.ingsw.client;

import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.view.View;

public class ErrorCS extends ClientState{

    @Override
    protected Packet doStuff(LiteModel model, View view) {
        System.out.println("Error");
        System.exit(-1);
        return null;
    }

}
