package it.polimi.ingsw.client;

import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.litemodel.LiteModel;

import java.util.Scanner;

public class EndGameCS extends ClientState {

    public EndGameCS() {
        super();
    }

    @Override
    protected Packet doStuff(LiteModel model) {

        System.out.println("The game has finished! The final leaderboard:");
        //printa la leaderboard
        System.out.println("The winner is . . ." + "winner" + "!");
        System.out.println("Press enter to exit...");
        while(true){
            new Scanner(System.in).nextLine();
            System.exit(-1);
            return null;
        }
    }
}
