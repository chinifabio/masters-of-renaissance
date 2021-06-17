package it.polimi.ingsw.communication.packet.rendering;

import it.polimi.ingsw.view.View;

public class FireLobbyWait extends Lighter {
    @Override
    public void fire(View view) {
        view.fireLobbyWait();
    }
}
