package it.polimi.ingsw.client;

import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.view.View;

import java.util.List;
import java.util.Scanner;

public class SetPlayersNumberCS extends ClientState {

    public SetPlayersNumberCS() {
        super();
        this.nextState.put(HeaderTypes.JOIN_LOBBY, new InGameCS());
        this.nextState.put(HeaderTypes.INVALID, this);
    }

    @Override
    protected Packet doStuff(LiteModel model, View view) throws InterruptedException {
        int number = -1;
        boolean illegal = true;

        while (illegal) {
            String data = view.askUser("How many players?");

            try {
                if(data.equals("")) throw new IndexOutOfBoundsException();
                number = Integer.parseInt(data);
                illegal = false;
            }
            catch (IndexOutOfBoundsException | NullPointerException e) {
                view.notifyPlayerError("You have to insert a number");
            }
            catch (NumberFormatException e) {
                view.notifyPlayerError(data + " is not a number");
            }
        }

        return new Packet(HeaderTypes.SET_PLAYERS_NUMBER, ChannelTypes.PLAYER_ACTIONS, String.valueOf(number));
    }
}

