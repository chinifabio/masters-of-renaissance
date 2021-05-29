package it.polimi.ingsw.litemodel.litewarehouse;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;

import java.util.*;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class LiteWarehouse {

    private final HashMap<String, LiteDepot> depots = new HashMap<>();
    private final HashMap<String, LiteProduction> productions = new HashMap<>();

    @JsonCreator
    public LiteWarehouse() {}

    public void setDepot(DepotSlot slot, LiteDepot depot) {
        this.depots.put(slot.name(), depot);
    }

    @JsonIgnore
    public LiteDepot getDepots(DepotSlot slot) {
        return this.depots.get(slot.name());
    }

    public void setProductions(ProductionID id, LiteProduction prod) {
        this.productions.put(id.name(), prod);
    }

    @JsonIgnore
    public LiteProduction getProductions(ProductionID id) {
        return this.productions.get(id.name());
    }

    @JsonIgnore
    public Map<ProductionID, LiteProduction> getAllProductions(){
        Map<ProductionID, LiteProduction> result = new HashMap<>();
        this.productions.forEach((key, value) -> result.put(ProductionID.valueOf(key), value));
        return result;
    }
}
