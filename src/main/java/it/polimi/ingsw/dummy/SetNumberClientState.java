package it.polimi.ingsw.dummy;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.litemodel.LiteModel;

import java.util.Scanner;

public class SetNumberClientState extends ClientState {

    public SetNumberClientState() {
        super();
    }

    /**
     * Do some stuff that generate a packet to send to the server
     *
     * @return packet to send to the server
     */
    @Override
    protected Packet doStuff(LiteModel model) {
        String input = "";
        while (input.equals("")) {
            System.out.println("You have to create the game! Give me the desired size:");
            System.out.print("> ");
            input = new Scanner(System.in).nextLine();
        }

        if (Integer.parseInt(input) == 1){
            this.nextState.put(HeaderTypes.JOIN_LOBBY, new SPInGameClientState());
        } else if (Integer.parseInt(input) > 1 && Integer.parseInt(input) <= 4) {
            this.nextState.put(HeaderTypes.JOIN_LOBBY, new InGameClientState());
        } else {
            this.nextState.put(HeaderTypes.JOIN_LOBBY, new ErrorClientState());
        }
        return new Packet(HeaderTypes.SET_PLAYERS_NUMBER, ChannelTypes.PLAYER_ACTIONS, input);

    }
}
