package it.polimi.ingsw.communication.client;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;

import java.util.Scanner;

public class InitialCS extends ClientState{

    public InitialCS(Client context) {
        super(context, "insert a valid nickname");
    }

    @Override
    public Packet setNickname() {
        String nickname = "pino";
        //metodo della gui/cli per settare il nick

        return new Packet(HeaderTypes.HELLO, ChannelTypes.PLAYER_ACTIONS, nickname);
    }
}
