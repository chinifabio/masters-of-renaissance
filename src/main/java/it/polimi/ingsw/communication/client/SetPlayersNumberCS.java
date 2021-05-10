package it.polimi.ingsw.communication.client;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.Command;
import it.polimi.ingsw.communication.packet.commands.SetNumberCommand;

import java.util.Scanner;

public class SetPlayersNumberCS extends ClientState{

    public SetPlayersNumberCS(Client context) {
        super(context, "inserted a wrong number of players");
    }

    @Override
    public Packet setNumberOfPlayers() {
        int number = 4;

        //metodo cli/gui che ritorna numero inserito

        //Scanner scanner = new Scanner(System.in);
        //int number = scanner.nextInt();

        while(number > 4 || number < 1){
            System.out.println("Numero non legale, rifai: ");
            //metodo della cli/gui che ritorna il numero inserito
        }

        return new Packet(HeaderTypes.SET_PLAYERS_NUMBER, ChannelTypes.PLAYER_ACTIONS, new SetNumberCommand(number).jsonfy());
    }
}
