package it.polimi.ingsw.communication.packet.updates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.litemodel.litemarkettray.LiteMarketTray;

import java.util.List;

public class TrayUpdater extends Updater {

    private final LiteMarketTray tray;

    @JsonCreator
    public TrayUpdater(@JsonProperty("tray") LiteMarketTray tray) {
        this.tray = tray;
    }

    /**
     * Take a lite model as input and apply to the implementing function
     *
     * @param liteModel the lite model on the client
     */
    @Override
    public void update(LiteModel liteModel) {
        liteModel.setMarketTray(this.tray);
    }
}
