package it.polimi.ingsw.model.player.personalBoard.warehouse.production;

import com.fasterxml.jackson.annotation.*;
import it.polimi.ingsw.litemodel.litewarehouse.LiteProduction;
import it.polimi.ingsw.model.MappableToLiteVersion;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalNormalProduction;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
import it.polimi.ingsw.model.exceptions.warehouse.production.UnknownUnspecifiedException;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;

import java.util.*;

/**
 * This class represents the production where some resources are inserted as input to receive other resources in output.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "NormalProd", value = NormalProduction.class),
        @JsonSubTypes.Type(name = "UnknownProd", value = UnknownProduction.class)
})
public abstract class Production implements MappableToLiteVersion {
    /**
     * this list contains all the input resource not allowed in the production
     */
    @JsonIgnore
    protected final List<ResourceType> illegalType;

    /**
     * This attribute is the list of the Resources required to activate the Production
     */
    protected final List<Resource> required;

    /**
     * This attribute is the list of the Resources obtained after the Production
     */
    protected final List<Resource> output;

    /**
     * This attribute indicates the resources selected by the Player to activate this Production
     */
    protected List<Resource> addedResource;

    /**
     * This attribute indicates when the production is been activated correctly
     */
    @JsonIgnore
    protected boolean activated;

    /**
     * Flag the production as select if player add resource in it
     */
    @JsonIgnore
    protected boolean selected;

    /**
     * This method is the constructor of the class
     * @param newRequired is the Resources required to activate the production
     * @param newOutput is the Resources obtained after that the production is activated
     * @param illegal if the Resource can't be used
     * @throws IllegalTypeInProduction if an IllegalResource is used for the Production
     */
    public Production(List<Resource> newRequired, List<Resource> newOutput, List<ResourceType> illegal) throws IllegalTypeInProduction {

        this.illegalType = illegal;

        for (Resource x : newRequired) {
            if (illegalType.contains(x.type())) {
                throw new IllegalTypeInProduction(x);
            }
        }

        for (Resource x : newOutput) {
            if (illegalType.contains(x.type())) {
                throw new IllegalTypeInProduction(x);
            }
        }

        this.required = ResourceBuilder.buildListOfResource();
        for (Resource res : newRequired)
            required.stream().filter(x -> x.equalsType(res)).findAny().orElse(ResourceBuilder.buildUnknown()).merge(res);

        this.output = ResourceBuilder.buildListOfResource();
        for (Resource res : newOutput)
            output.stream().filter(x -> x.equalsType(res)).findAny().orElse(ResourceBuilder.buildUnknown()).merge(res);

        this.addedResource = ResourceBuilder.buildListOfResource();


        this.activated = false;
        this.selected = false;
    }

    /**
     * This method returns the List of Resources requests
     */
    @JsonGetter("required")
    public List<Resource> getRequired() {
        List<Resource> clone = new ArrayList<>();
        for(Resource item : this.required) if (item.amount() > 0) clone.add(item);
        return clone;
    }

    /**
     * This method returns the List of Resources obtained after the Production
     */
    @JsonGetter("output")
    public List<Resource> getOutput() {
        List<Resource> clone = new ArrayList<>();
        for (Resource item : this.output) if (item.amount() > 0) clone.add(item);
        return clone;
    }

    /**
     * This method returns a list of added resource
     */
    @JsonGetter("addedResource")
    public List<Resource> getAddedResource() {
        List<Resource> clone = new ArrayList<>();
        for(Resource item : this.addedResource) if (item.amount() > 0) clone.add(item);
        return clone;
    }

    /**
     * This method turns the "activated" attribute to true
     * @return true when this Production is activated
     */
    public boolean isActivated() {
        return this.activated;
    }

    /**
     * This method transform the input resource in output resource if given one matches required one
     * @return the succeed of the operation
     */
    public abstract boolean activate();

    /**
     * This method is used to insert resources in the production
     * @param resource the resource to insert
     * @return the succeed of the operation
     */
    public abstract boolean insertResource(Resource resource) throws UnknownUnspecifiedException;

    /**
     * This method allow to set the normal production in case of unknown resource
     * @param normalProduction the production to set as normal production in case of unknown resource
     * @return the succeed of the operation
     */
    public abstract boolean setNormalProduction(NormalProduction normalProduction) throws IllegalNormalProduction;

    /**
     * This method indicates if the player added resources to activate the production
     * @return true if the Player added resources
     */
    public boolean isSelected(){
        return this.selected;
    }

    /**
     * This method reset the production
     * @return the succeed of the operation
     */
    public boolean reset() {
        this.addedResource = ResourceBuilder.buildListOfResource();
        this.selected = false;
        this.activated = false;
        return true;
    }

    /**
     * Create a lite version of the class and serialize it in json
     *
     * @return the json representation of the lite version of the class
     */
    @Override
    public LiteProduction liteVersion() {
        return new LiteProduction (
                ResourceBuilder.mapResource(getRequired()),
                ResourceBuilder.mapResource(getAddedResource()),
                ResourceBuilder.mapResource(getOutput())
        );
    }
}
