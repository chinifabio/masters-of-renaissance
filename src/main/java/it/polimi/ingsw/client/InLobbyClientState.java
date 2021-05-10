package it.polimi.ingsw.client;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.view.litemodel.LiteModel;

public class InLobbyClientState extends ClientState{

    public InLobbyClientState() {
        super();
        this.nextState.put(HeaderTypes.START_TURN, new InGameClientState());
    }

    /**
     * Do some stuff that generate a packet to send to the server
     *
     * @return packet to send to the server
     */
    @Override
    protected Packet doStuff(LiteModel model) {
        System.out.println("waiting...");
        return new Packet(HeaderTypes.WAIT_FOR_START, ChannelTypes.PLAYER_ACTIONS, "waiting");
    }
}
