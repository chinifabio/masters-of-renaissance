package it.polimi.ingsw.litemodel.litewarehouse;

import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;

import java.util.EnumMap;
import java.util.Map;

public class LiteWarehouse {

    private final Map<DepotSlot, LiteDepot> depots = new EnumMap<>(DepotSlot.class);

    private final Map<ProductionID, LiteProduction> productions = new EnumMap<>(ProductionID.class);

    public void setDepot(DepotSlot slot, LiteDepot depot) {
        this.depots.put(slot, depot);
    }

    public LiteDepot getDepots(DepotSlot slot) {
        return this.depots.get(slot);
    }

    public void setProduction(ProductionID id, LiteProduction prod) {
        this.productions.put(id, prod);
    }

    public LiteProduction getProduction(ProductionID id) {
        return this.productions.get(id);
    }
}
