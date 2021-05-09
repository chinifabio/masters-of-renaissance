package it.polimi.ingsw.communication.packet.updates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.view.litemodel.LiteModel;

import java.util.List;

public class TrayPublisher extends Publisher{

    private final List<String> tray;

    @JsonCreator
    public TrayPublisher(@JsonProperty("tray") List<String> tray) {
        this.tray = tray;
    }

    /**
     * Take a lite model as input and apply to the implementing function
     *
     * @param liteModel the lite model on the client
     */
    @Override
    public void update(LiteModel liteModel) {
        liteModel.setMarbleMarket(this.tray);
    }
}
