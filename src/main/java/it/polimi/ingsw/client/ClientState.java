package it.polimi.ingsw.client;

import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.communication.ReplyType;
import it.polimi.ingsw.communication.ServerReply;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.view.View;

import java.util.EnumMap;
import java.util.Map;

public abstract class ClientState {

    /**
     * The next state mapped on the server response
     */
    protected final Map<HeaderTypes, ClientState> nextState = new EnumMap<>(HeaderTypes.class);


    /**
     * Do some stuff that generate a packet to send to the server
     * @return packet to send to the server
     */
    protected abstract Packet doStuff(LiteModel model, View view) throws InterruptedException;

    public void start(Client context, View view) throws InterruptedException {
        Packet temp = this.doStuff(context.liteModel, view);

        context.socket.send(temp);
        Packet response = context.socket.pollPacketFrom(ChannelTypes.PLAYER_ACTIONS);
        view.notifyServerReply(new ServerReply(response.body, ReplyType.OK));

        context.setState(this.nextState.containsKey(response.header) ?
                this.nextState.get(response.header):
                new ErrorCS()
        );
    }

}
