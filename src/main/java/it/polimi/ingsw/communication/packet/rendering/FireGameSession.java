package it.polimi.ingsw.communication.packet.rendering;

import it.polimi.ingsw.view.View;

public class FireGameSession extends Lighter {
    @Override
    public void fire(View view) {
        view.fireGameSession();
    }
}
