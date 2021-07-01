package it.polimi.ingsw.litemodel.litewarehouse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.LiteResource;
import it.polimi.ingsw.model.resource.ResourceType;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the lite version of a Production
 */
public class LiteProduction {

    /**
     * This attribute is the list of the required resources of the production
     */
    private final List<LiteResource> required = new ArrayList<>();

    /**
     * This attribute is the list of the result resources of the production
     */
    private final List<LiteResource> output = new ArrayList<>();

    /**
     * This attribute is the list of the added resources to the production
     */
    private final List<LiteResource> added = new ArrayList<>();

    /**
     * This is the constructor of the class:
     * @param required LiteResource needed to activate the production
     * @param added LiteResource added to the production, null for empty list
     * @param output LiteResource that will be the result of the production
     */
    @JsonCreator
    public LiteProduction(@JsonProperty("required") List<LiteResource> required, @JsonProperty("added") List<LiteResource> added, @JsonProperty("output") List<LiteResource> output) {
        this.required.addAll(required);
        this.output.addAll(output);
        this.added.addAll(added == null ? new ArrayList<>() : added);
    }

    /**
     * This method returns the list of required resources
     * @return a list of LiteResources
     */
    public List<LiteResource> getRequired() {
        return required;
    }

    /**
     * This method returns the list of result resources
     * @return a list of LiteResources
     */
    public List<LiteResource> getOutput() {
        return output;
    }

    /**
     * This method returns the list of added resources
     * @return a list of LiteResources
     */
    public List<LiteResource> getAdded() {
        return added;
    }

    /**
     * This method returns true if a production has unknown resources inside, else false
     * @return true if the production isn't normalized yet, else return false
     */
    @JsonIgnore
    public boolean isUnknown() {
        return required.contains(new LiteResource(ResourceType.UNKNOWN, 1)) || output.contains(new LiteResource(ResourceType.UNKNOWN, 1));
    }
}
