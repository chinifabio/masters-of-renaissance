package it.polimi.ingsw.communication.packet.updates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.litemodel.litecards.LiteLeaderCard;
import it.polimi.ingsw.model.cards.DevCard;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.List;

public class LeaderUpdater extends Updater{

    private final String nickname;

    private final List<LiteLeaderCard> deck = new ArrayList<>();

    @JsonCreator
    public LeaderUpdater(@JsonProperty("nickname") String nickname, @JsonProperty("deck") List<LiteLeaderCard> deck) {
        this.deck.addAll(deck);
        this.nickname = nickname;

    }

    /**
     * Take a lite model as input and apply to the implementing function
     *
     * @param liteModel the lite model on the client
     */
    @Override
    public void update(LiteModel liteModel, View view) {
        liteModel.setLeader(nickname, deck);
    }
}
