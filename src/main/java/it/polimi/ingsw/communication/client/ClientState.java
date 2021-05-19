package it.polimi.ingsw.communication.client;

import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.litemodel.LiteModel;

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
    protected abstract Packet doStuff(LiteModel model);

    public void start(Client context){
        Packet temp = this.doStuff(context.liteModel);
        if(!temp.header.equals(HeaderTypes.INVALID)){
            context.socket.send(temp);
            Packet response = context.socket.pollPacketFrom(ChannelTypes.PLAYER_ACTIONS);
            printServerResponse(response.body);

            context.setState(this.nextState.containsKey(response.header) ?
                    this.nextState.get(response.header):
                    new ErrorCS()
            );
        }
        else if(temp.body.equals("Invalid")){
            System.out.println(TextColors.colorText(TextColors.RED_BRIGHT,"You can't do that!"));
        }
    }

    /**
     * Print the server response in a preformatted mode
     * @param r the server response
     */
    protected void printServerResponse(String r) {
        System.out.println(TextColors.colorText(TextColors.BLUE, "[server] ") + r);
    }
}
