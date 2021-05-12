package it.polimi.ingsw.communication.packet.updates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.LiteModel;

public class BuildMePublisher extends Publisher {
    /**
     * the nickname of the player
     */
    private final String nickname;

    @JsonCreator
    public BuildMePublisher(@JsonProperty("nick") String nickname) {
        this.nickname = nickname;
    }

    /**
     * Take a lite model as input and apply to the implementing function
     *
     * @param liteModel the lite model on the client
     */
    @Override
    public void update(LiteModel liteModel) {
        liteModel.createPlayer(this.nickname);
    }
}
