package it.polimi.ingsw.communication.packet.updates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.litemodel.litefaithtrack.LiteFaithTrack;
import it.polimi.ingsw.view.View;

public class FaithTrackUpdater extends Updater{

    private final String nickname;

    private final LiteFaithTrack track;

    @JsonCreator
    public FaithTrackUpdater(@JsonProperty("nickname") String nickname, @JsonProperty("track") LiteFaithTrack track) {
        this.track = track;
        this.nickname = nickname;
    }

    /**
     * Take a lite model as input and apply to the implementing function
     *
     * @param liteModel the lite model on the client
     */
    @Override
    public void update(LiteModel liteModel, View view) {
        view.notifyPlayer(nickname + " moved on faith track");
        liteModel.updateFaithTrack(this.nickname, this.track);
    }
}
