package it.polimi.ingsw.view.litemodel;

import java.util.List;

public class LiteWarehouse {

    private final List<LiteDepot> depots;

    public LiteWarehouse(List<LiteDepot> depots) {
        this.depots = depots;
    }

    public List<LiteDepot> getDepots() {
        return depots;
    }
}
