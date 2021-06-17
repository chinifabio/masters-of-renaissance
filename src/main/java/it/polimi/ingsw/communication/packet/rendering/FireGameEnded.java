package it.polimi.ingsw.communication.packet.rendering;

import it.polimi.ingsw.view.View;

public class FireGameEnded extends Lighter {
    @Override
    public void fire(View view) {
        view.fireGameEnded();
    }
}
