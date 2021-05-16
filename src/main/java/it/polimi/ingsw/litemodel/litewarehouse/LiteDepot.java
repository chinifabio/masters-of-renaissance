package it.polimi.ingsw.litemodel.litewarehouse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.LiteResource;

import java.util.List;

public class LiteDepot {

    private final List<LiteResource> resourcesInside;

    @JsonCreator
    public LiteDepot(@JsonProperty("resourcesInside") List<LiteResource> resourcesInside) {
        this.resourcesInside = resourcesInside;
    }

    public List<LiteResource> getResourcesInside() {
        return resourcesInside;
    }


}
