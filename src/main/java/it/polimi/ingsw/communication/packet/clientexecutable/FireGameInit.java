package it.polimi.ingsw.communication.packet.clientexecutable;

import it.polimi.ingsw.view.View;

public class FireGameInit extends ClientExecutable {

    @Override
    public void execute(View view) {
        view.fireGameInit();
    }
}
