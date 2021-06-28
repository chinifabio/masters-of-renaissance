package it.polimi.ingsw.communication.packet.clientexecutable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.litecards.LiteSoloActionToken;
import it.polimi.ingsw.view.View;

public class LorenzoPopUp extends ClientExecutable {
    private final LiteSoloActionToken token;

    @JsonCreator
    public LorenzoPopUp(@JsonProperty("token") LiteSoloActionToken token) {
        this.token = token;
    }

    /**
     * Execute a command on the view
     *
     * @param view the view on with execute the command
     */
    @Override
    public void execute(View view) {
        view.popUpLorenzoMoves(token);
    }
}
