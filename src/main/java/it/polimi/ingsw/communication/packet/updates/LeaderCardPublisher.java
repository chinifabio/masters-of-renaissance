package it.polimi.ingsw.communication.packet.updates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.view.litemodel.LiteModel;

import java.util.List;

public class LeaderCardPublisher extends Publisher {
    /**
     * the list of card id
     */
    private final List<String> cards;

    /**
     * The nickname of the player who modified leader cards
     */
    private final String nickname;

    /**
     * Build an updater
     * @param cards list of card id
     * @param nickname nickname of the player
     */
    @JsonCreator
    public LeaderCardPublisher(@JsonProperty("cards") List<String> cards, @JsonProperty("player") String nickname) {
        this.cards = cards;
        this.nickname = nickname;
    }

    /**
     * Take a lite model as input and apply to the implementing function
     *
     * @param liteModel the lite model on the client
     */
    @Override
    public void update(LiteModel liteModel) {
        liteModel.setLeader(this.nickname, this.cards);
    }
}
