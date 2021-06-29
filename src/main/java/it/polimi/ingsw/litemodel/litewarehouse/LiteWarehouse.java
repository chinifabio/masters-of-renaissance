package it.polimi.ingsw.litemodel.litewarehouse;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;

import java.util.*;

/**
 * This class represents the lite version of the Warehouse
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class LiteWarehouse {

    /**
     * This attribute is the map of depot names and the actual LiteDepot
     */
    private final HashMap<String, LiteDepot> depots = new HashMap<>();

    /**
     * This attribute is the map of production names and the actual LiteProduction
     */
    private final HashMap<String, LiteProduction> productions = new HashMap<>();

    /**
     * This is the constructor of the class
     */
    @JsonCreator
    public LiteWarehouse() {}

    /**
     * This method sets an element in the depot map
     * @param slot to insert
     * @param depot to insert
     */
    public void setDepot(DepotSlot slot, LiteDepot depot) {
        this.depots.put(slot.name(), depot);
    }

    /**
     * This method returns the asked depot
     * @param slot to return
     * @return the asked depot
     */
    @JsonIgnore
    public LiteDepot getDepots(DepotSlot slot) {
        return this.depots.get(slot.name());
    }

    /**
     * This method sets an element in the production map
     * @param id to insert
     * @param prod to insert
     */
    public void setProductions(ProductionID id, LiteProduction prod) {
        this.productions.put(id.name(), prod);
    }

    //todo not used
    /**
     * This method returns the asked production
     * @param id to return
     * @return the asked depot
     */
    @JsonIgnore
    public LiteProduction getProductions(ProductionID id) {
        return this.productions.get(id.name());
    }

    /**
     * This method returns the map of productions
     * @return the map of ProductionIDs and LiteProductions
     */
    @JsonIgnore
    public Map<ProductionID, LiteProduction> getAllProductions(){
        Map<ProductionID, LiteProduction> result = new HashMap<>();
        this.productions.forEach((key, value) -> result.put(ProductionID.valueOf(key), value));
        return result;
    }
}
