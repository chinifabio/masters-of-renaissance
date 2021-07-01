package it.polimi.ingsw.communication.packet.clientexecutable;

import it.polimi.ingsw.view.View;

/**
 * Render the game phase where the player has to wait in the client
 */
public class FireLobbyWait extends ClientExecutable {
    /**
     * Execute a command on the view
     * @param view the view on with execute the command
     */
    @Override
    public void execute(View view) {
        view.fireLobbyWait();
    }
}
