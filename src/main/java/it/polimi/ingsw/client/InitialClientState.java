package it.polimi.ingsw.client;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.litemodel.LiteModel;

import java.util.Scanner;

public class InitialClientState extends ClientState {

    public InitialClientState() {
        super();
        this.nextState.put(HeaderTypes.JOIN_LOBBY, new InLobbyClientState());
        this.nextState.put(HeaderTypes.SET_PLAYERS_NUMBER, new SetNumberClientState());
    }

    /**
     * Do some stuff that generate a packet to send to the server
     *
     * @return packet to send to the server
     */
    @Override
    protected Packet doStuff(LiteModel model) {
        System.out.println("Insert your username");
        System.out.print("> ");
        return new Packet(HeaderTypes.HELLO, ChannelTypes.PLAYER_ACTIONS, new Scanner(System.in).nextLine());
    }
}
