package it.polimi.ingsw.communication.packet.clientexecutable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.view.View;

/**
 * This executable notify a message from the server to the player
 */
public class PlayerMessage extends ClientExecutable {

    /**
     * The error message to notify
     */
    private final String message;

    /**
     * This is the constructor of the class
     * @param message is the notify message
     */
    @JsonCreator
    public PlayerMessage (@JsonProperty("message") String message) {
        this.message = message;
    }

    /**
     * Execute a command on the view
     *
     * @param view the view on with execute the command
     */
    @Override
    public void execute(View view) {
        view.notifyPlayer(message);
    }
}
