package it.polimi.ingsw.litemodel.litewarehouse;

import it.polimi.ingsw.litemodel.LiteResource;

import java.util.List;

public class LiteDepot {

    private final List<LiteResource> resourcesInside;

    public LiteDepot(List<LiteResource> resourcesInside) {
        this.resourcesInside = resourcesInside;
    }

    public List<LiteResource> getResourcesInside() {
        return resourcesInside;
    }


}
