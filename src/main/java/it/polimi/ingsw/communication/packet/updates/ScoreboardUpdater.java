package it.polimi.ingsw.communication.packet.updates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.litemodel.Scoreboard;

public class ScoreboardUpdater extends Updater {
    private final Scoreboard scoreboard;

    @JsonCreator
    public ScoreboardUpdater(@JsonProperty("scoreboard") Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    /**
     * Take a lite model as input and apply to the implementing function
     *
     * @param liteModel the lite model on the client
     */
    @Override
    public void update(LiteModel liteModel) {
        liteModel.setScoreboard(scoreboard);
    }
}
