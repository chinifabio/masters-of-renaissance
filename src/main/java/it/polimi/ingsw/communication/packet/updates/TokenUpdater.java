package it.polimi.ingsw.communication.packet.updates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.litemodel.litecards.LiteSoloActionToken;
import it.polimi.ingsw.view.View;

import java.util.List;

public class TokenUpdater extends Updater {

    private final LiteSoloActionToken token;

    @JsonCreator
    public TokenUpdater(@JsonProperty("token") LiteSoloActionToken token) {
        this.token = token;
    }
    /**
     * Take a lite model as input and apply to the implementing function
     *
     * @param liteModel the lite model on the client
     */
    @Override
    public void update(LiteModel liteModel) {
        liteModel.setSoloToken(this.token);
    }
}
