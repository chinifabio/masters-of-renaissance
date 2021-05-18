package it.polimi.ingsw.communication.packet.updates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.litemodel.litecards.LiteDevCard;

import java.util.List;

public class DevelopUpdater extends Updater {

    private final String nickname;

    private final List<LiteDevCard> deck;

    @JsonCreator
    public DevelopUpdater(@JsonProperty("nickname") String nickname, @JsonProperty("deck") List<LiteDevCard> deck) {
        this.deck = deck;
        this.nickname = nickname;
    }

    /**
     * Take a lite model as input and apply to the implementing function
     *
     * @param liteModel the lite model on the client
     */
    @Override
    public void update(LiteModel liteModel) {
        liteModel.setDevelop(nickname, deck);
    }
}
