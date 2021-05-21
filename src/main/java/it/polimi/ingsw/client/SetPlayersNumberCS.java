package it.polimi.ingsw.client;

import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.litemodel.LiteModel;

import java.util.Scanner;

public class SetPlayersNumberCS extends ClientState {

    public SetPlayersNumberCS() {
        super();
        this.nextState.put(HeaderTypes.JOIN_LOBBY, new InGameCS());
    }

    @Override
    protected Packet doStuff(LiteModel model) {
        System.out.print(TextColors.colorText(TextColors.YELLOW, "Choose the number of players:\n>"));
        Scanner scanner = new Scanner(System.in);
        int number = scanner.nextInt();                  //metodo della cli/gui che ritorna il numero inserito
        while (number > 4 || number < 1) {
            System.out.print("The number is not valid, it has to be included between 1 and 4:\n>");
            number = scanner.nextInt();              //metodo della cli/gui che ritorna il numero inserito
        }
        return new Packet(HeaderTypes.SET_PLAYERS_NUMBER, ChannelTypes.PLAYER_ACTIONS, String.valueOf(number));
    }
}

