package it.polimi.ingsw.communication.packet.clientexecutable;

import it.polimi.ingsw.view.View;

public class FireGameEnded extends ClientExecutable {
    @Override
    public void execute(View view) {
        view.fireGameEnded();
    }
}
