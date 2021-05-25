package it.polimi.ingsw.client;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.cli.printer.NamePrinter;

public class InitialCS extends ClientState{

    public InitialCS() {
        super();
        this.nextState.put(HeaderTypes.INVALID, this);
        this.nextState.put(HeaderTypes.JOIN_LOBBY, new InGameCS());
        this.nextState.put(HeaderTypes.SET_PLAYERS_NUMBER, new SetPlayersNumberCS());
    }

    @Override
    protected Packet doStuff(LiteModel model, View view) throws InterruptedException {
        boolean illegal = true;
        String nick = "";

        while (illegal) {
            try {
                nick = view.pollData("Choose your nickname:").get(0);
            } catch (IndexOutOfBoundsException out) {
                view.notifyPlayerError("You have to insert a nickname");
            } finally {
                illegal = false;
            }
        }

        model.setMyNickname(nick);
        return new Packet(HeaderTypes.HELLO, ChannelTypes.PLAYER_ACTIONS, nick);
    }
}