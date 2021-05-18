package it.polimi.ingsw.communication.packet.updates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.litemodel.litewarehouse.LiteDepot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;

public class DepotUpdater extends Updater{

    private final String nickname;

    private final DepotSlot slot;

    private final LiteDepot depot;

    @JsonCreator
    public DepotUpdater(@JsonProperty("nickname") String nickname, @JsonProperty("depot") LiteDepot depot, @JsonProperty("slot") DepotSlot slot) {
        this.nickname = nickname;
        this.slot = slot;
        this.depot = depot;
    }

    /**
     * Take a lite model as input and apply to the implementing function
     *
     * @param liteModel the lite model on the client
     */
    @Override
    public void update(LiteModel liteModel) {
        liteModel.setDepot(this.nickname, slot, depot);
    }
}
