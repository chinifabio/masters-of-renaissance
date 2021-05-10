package it.polimi.ingsw.communication.client;

public class WaitMyTurnCS extends ClientState {
    public WaitMyTurnCS(Client context) {
        super(context, "it's not your turn, wait");
    }
}
