package it.polimi.ingsw.communication.packet.rendering;

import it.polimi.ingsw.view.View;

public class FireGameCreator extends Lighter {

    @Override
    public void fire(View view) {
        view.fireGameCreator();
    }
}
