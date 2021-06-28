package it.polimi.ingsw.communication.packet.clientexecutable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.view.View;

public class ModelUpdater extends ClientExecutable {
    private final LiteModel model;

    @JsonCreator
    public ModelUpdater(@JsonProperty("model") LiteModel model) {
        this.model = model;
    }

    /**
     * Take a lite model as input and apply to the implementing function
     *
     * @param view the lite model on the client
     */
    @Override
    public void execute(View view) {
        view.getModel().replaceModel(model);
        view.refresh();
    }
}
