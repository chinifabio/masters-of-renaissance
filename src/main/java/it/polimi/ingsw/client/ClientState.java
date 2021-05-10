package it.polimi.ingsw.client;

import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.litemodel.LiteModel;

import java.util.EnumMap;
import java.util.Map;

/**
 * This class represent the state of the fsm that manage the client possible action
 */
public abstract class ClientState {

    /**
     * The next state mapped on the server response
     */
    protected final Map<HeaderTypes, ClientState> nextState = new EnumMap<>(HeaderTypes.class);

    /**
     * Do some stuff that generate a packet to send to the server
     * @return packet to send to the server
     */
    protected abstract Packet doStuff(LiteModel model);

    /**
     * From the context generate a string and than send it to the player. than once
     * the server responses set the context state passed by the sever
     * @param context the context of operation
     */
    public void start(DummyClient context) {
        context.socket.send(this.doStuff(context.model));
        Packet response = context.socket.pollPacketFrom(ChannelTypes.PLAYER_ACTIONS);
        printServerResponse(response.body);

        context.setState(this.nextState.containsKey(response.header) ?
                this.nextState.get(response.header):
                new ErrorClientState()
        );
    }

    /**
     * Print the server response in a preformatted mode
     * @param r the server response
     */
    protected void printServerResponse(String r) {
        System.out.println(TextColors.colorText(TextColors.BLUE, "[server] ") + r);
    }
}

