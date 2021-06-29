package it.polimi.ingsw.litemodel.litewarehouse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.LiteResource;

import java.util.List;

/**
 * This class represents the lite version of a Depot
 */
public class LiteDepot {

    /**
     * This attribute is the list of resources that the depot contains
     */
    private final List<LiteResource> resourcesInside;

    /**
     * This is the constructor of the class:
     * @param resourcesInside list of LiteResource to insert
     */
    @JsonCreator
    public LiteDepot(@JsonProperty("resourcesInside") List<LiteResource> resourcesInside) {
        this.resourcesInside = resourcesInside;
    }

    /**
     * This method returns the resources inside the depot
     * @return a list of LiteResources
     */
    public List<LiteResource> getResourcesInside() {
        return resourcesInside;
    }


}
