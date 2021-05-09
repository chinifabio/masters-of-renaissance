package it.polimi.ingsw.client;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.Command;
import it.polimi.ingsw.communication.packet.commands.EndTurnCommand;
import it.polimi.ingsw.view.litemodel.LiteModel;

import java.util.Scanner;

public class InGameClientState extends ClientState {

    /**
     * Do some stuff that generate a packet to send to the server
     *
     * @return packet to send to the server
     */
    @Override
    protected Packet doStuff(LiteModel model) {
        System.out.println("give me action: ");
        System.out.print("> ");

        Command command = null;

        switch (new Scanner(System.in).nextLine()) {
            case "discardLeader":
                System.out.println();
            case "endTurn":
                command = new EndTurnCommand();

        }

        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, command.jsonfy());
    }
}
