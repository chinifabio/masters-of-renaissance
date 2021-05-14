package it.polimi.ingsw.communication.packet.updates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.LiteModel;

public class LorenzoUpdater extends Updater {
    private final int move;

    @JsonCreator
    public LorenzoUpdater(@JsonProperty("move") int move) {
        this.move = move;
    }

    /**
     * Take a lite model as input and apply to the implementing function
     *
     * @param liteModel the lite model on the client
     */
    @Override
    public void update(LiteModel liteModel) {
        liteModel.movePlayer("lorenzo", this.move);
    }
}
