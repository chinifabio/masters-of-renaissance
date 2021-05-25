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
    }

    @Override
    protected Packet doStuff(LiteModel model, View view) throws InterruptedException {
        int number = -1;
        boolean illegal = true;

        while (illegal) {
            List<String> data = view.pollData("How many players?");

            try {
                number = Integer.parseInt(data.get(0));
            }
            catch (IndexOutOfBoundsException e) {
                view.notifyPlayerError("You have to insert a number");
            }
            catch (NumberFormatException e) {
                view.notifyPlayerError(data.get(0) + " is not a number");
            }
            finally {
                illegal = false;
            }
        }

        return new Packet(HeaderTypes.SET_PLAYERS_NUMBER, ChannelTypes.PLAYER_ACTIONS, String.valueOf(number));
    }
}

