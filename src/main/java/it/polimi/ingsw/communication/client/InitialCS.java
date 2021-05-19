package it.polimi.ingsw.communication.client;

import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.view.cli.printer.NamePrinter;

import java.util.Scanner;

public class InitialCS extends ClientState{

    public InitialCS() {
        super();
        this.nextState.put(HeaderTypes.INVALID, this);
        this.nextState.put(HeaderTypes.JOIN_LOBBY, new InGameCS());
        this.nextState.put(HeaderTypes.SET_PLAYERS_NUMBER, new SetPlayersNumberCS());
    }

    @Override
    protected Packet doStuff(LiteModel model) {
        //metodo della gui/cli per settare il nick
        NamePrinter.titleName();
        System.out.print(TextColors.colorText(TextColors.YELLOW, "Choose your nickname:\n>"));
        Scanner scanner = new Scanner(System.in);
        String nick = scanner.nextLine();
        model.setMyNickname(nick);
        return new Packet(HeaderTypes.HELLO, ChannelTypes.PLAYER_ACTIONS, nick);
    }
}
