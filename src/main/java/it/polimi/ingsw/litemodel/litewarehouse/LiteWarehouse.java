package it.polimi.ingsw.litemodel.litewarehouse;

import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;

import java.util.EnumMap;
import java.util.Map;

public class LiteWarehouse {

    private final Map<DepotSlot, LiteDepot> depots = new EnumMap<>(DepotSlot.class);

    public void setDepot(DepotSlot slot, LiteDepot depot) {
        this.depots.put(slot, depot);
    }

    public LiteDepot getDepots(DepotSlot slot) {
        return this.depots.get(slot);
    }

}
