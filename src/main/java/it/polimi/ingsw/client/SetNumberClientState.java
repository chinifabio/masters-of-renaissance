package it.polimi.ingsw.client;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.litemodel.LiteModel;

import java.util.Scanner;

public class SetNumberClientState extends ClientState {

    public SetNumberClientState() {
        super();
        this.nextState.put(HeaderTypes.JOIN_LOBBY, new InGameClientState());
    }

    /**
     * Do some stuff that generate a packet to send to the server
     *
     * @return packet to send to the server
     */
    @Override
    protected Packet doStuff(LiteModel model) {
        System.out.println("You have to create the game! Give me the desired size:");
        System.out.print("> ");
        return new Packet(HeaderTypes.SET_PLAYERS_NUMBER, ChannelTypes.PLAYER_ACTIONS, new Scanner(System.in).nextLine());
    }
}
