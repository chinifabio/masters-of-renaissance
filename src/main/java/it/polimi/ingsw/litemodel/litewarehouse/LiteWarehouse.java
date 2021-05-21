package it.polimi.ingsw.litemodel.litewarehouse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.util.Tuple;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class LiteWarehouse {

    private final Map<DepotSlot, LiteDepot> depots = new EnumMap<>(DepotSlot.class);

    private final Map<ProductionID, LiteProduction> productions = new EnumMap<>(ProductionID.class);

    public void setDepot(DepotSlot slot, LiteDepot depot) {
        this.depots.put(slot, depot);
    }

    @JsonIgnore
    public LiteDepot getDepots(DepotSlot slot) {
        return this.depots.get(slot);
    }

    public void setProductions(ProductionID id, LiteProduction prod) {
        this.productions.put(id, prod);
    }

    @JsonIgnore
    public LiteProduction getProductions(ProductionID id) {
        return this.productions.get(id);
    }

    @JsonGetter("depots")
    public List<Tuple<DepotSlot,LiteDepot>> getDepots(){
        List<Tuple<DepotSlot,LiteDepot>> res = new ArrayList<>();
        depots.forEach((K,V)->res.add(new Tuple<>(K,V)));
        return res;
    }

    @JsonGetter("productions")
    public List<Tuple<DepotSlot,LiteDepot>> getProductions(){
        List<Tuple<DepotSlot,LiteDepot>> res = new ArrayList<>();
        depots.forEach((K,V)->res.add(new Tuple<>(K,V)));
        return res;
    }

    public LiteWarehouse() {

    }

    @JsonCreator
    public LiteWarehouse(@JsonProperty("depots") List<Tuple<DepotSlot,LiteDepot>> depots, @JsonProperty("productions") List<Tuple<DepotSlot,LiteDepot>> productions) {
        depots.forEach(tuple->this.depots.put(tuple.a,tuple.b));
        productions.forEach(tuple->this.depots.put(tuple.a,tuple.b));
    }

}
