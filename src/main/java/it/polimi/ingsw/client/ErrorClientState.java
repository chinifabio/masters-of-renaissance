package it.polimi.ingsw.client;

import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.litemodel.LiteModel;

public class ErrorClientState extends ClientState {
    /**
     * Do some stuff that generate a packet to send to the server
     *
     * @return packet to send to the server
     */
    @Override
    protected Packet doStuff(LiteModel model) {
        System.out.println("error");
        System.exit(-1);
        return null;
    }
}
