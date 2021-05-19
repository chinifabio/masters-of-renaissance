package it.polimi.ingsw.communication.client;

import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.litemodel.LiteModel;

public class ErrorCS extends ClientState{

    @Override
    protected Packet doStuff(LiteModel model) {
        System.out.println("Error");
        System.exit(-1);
        return null;
    }

}
