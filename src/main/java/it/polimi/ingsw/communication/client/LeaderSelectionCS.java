package it.polimi.ingsw.communication.client;

import it.polimi.ingsw.communication.packet.Packet;

public class LeaderSelectionCS extends ClientState{

    public LeaderSelectionCS(Client context) {
        super(context,"you must discard two leader card in total");
    }

    @Override
    public Packet discardLeader() {

        return super.discardLeader();
    }
}
