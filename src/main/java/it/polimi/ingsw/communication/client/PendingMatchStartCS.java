package it.polimi.ingsw.communication.client;

public class PendingMatchStartCS extends ClientState{

    public PendingMatchStartCS(Client context) {
        super(context, "you have to wait the match start");
    }

}
