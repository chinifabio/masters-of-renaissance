package it.polimi.ingsw.communication.packet.updates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.litemodel.litecards.LiteSoloActionToken;

import java.util.List;

public class TokenUpdater extends Updater {

    private final List<LiteSoloActionToken> deck;

    @JsonCreator
    public TokenUpdater(@JsonProperty("deck") List<LiteSoloActionToken> deck) {
        this.deck = deck;
    }
    /**
     * Take a lite model as input and apply to the implementing function
     *
     * @param liteModel the lite model on the client
     */
    @Override
    public void update(LiteModel liteModel) {
        liteModel.setSoloToken(this.deck);
    }
}
