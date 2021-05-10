package it.polimi.ingsw.communication.client;

public class EndGameCS extends ClientState {

    public EndGameCS(Client context) {
        super(context, "the game has finished, you can't do nothing");
    }

}
