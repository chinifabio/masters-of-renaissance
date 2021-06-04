package it.polimi.ingsw.communication.packet.updates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.view.View;

public class ModelUpdater extends Updater {
    private final LiteModel model;

    @JsonCreator
    public ModelUpdater(@JsonProperty("model") LiteModel model) {
        this.model = model;
    }

    /**
     * Take a lite model as input and apply to the implementing function
     *
     * @param liteModel the lite model on the client
     */
    @Override
    public void update(LiteModel liteModel) {
        liteModel.replaceModel(this.model);
    }
}
