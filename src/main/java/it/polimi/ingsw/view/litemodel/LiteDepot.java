package it.polimi.ingsw.view.litemodel;

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
