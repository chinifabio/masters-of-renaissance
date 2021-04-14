package it.polimi.ingsw.model.player.personalBoard.warehouse.production;

import it.polimi.ingsw.model.exceptions.productionException.IllegalNormalProduction;
import it.polimi.ingsw.model.exceptions.productionException.IllegalTypeInProduction;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;

import java.util.*;

/**
 * This class represents the production where some resources are inserted as input to receive other resources in output.
 */
public abstract class Production{

    /**
     * this list contains all the input resource not allowed in the production
     */
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
    protected final List<Resource> addedResource;

    /**
     * This attribute indicates when the production is been activated correctly
     */
    protected boolean activated;

    /**
     * This method is the constructor of the class
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
            required.stream().filter(x->x.equalsType(res)).findAny().orElse(null).merge(res);

        this.output = ResourceBuilder.buildListOfResource();
        for (Resource res : newOutput)
            output.stream().filter(x->x.equalsType(res)).findAny().orElse(null).merge(res);

        this.addedResource = ResourceBuilder.buildListOfResource();
    }

    /**
     * This method returns the List of Resources requests
     */
    public List<Resource> getRequired() {
        List<Resource> clone = new ArrayList<>(this.required.size());
        for(Resource item : this.required) if (item.amount() > 0) clone.add(item);
        return clone;
    }

    /**
     * This method returns the List of Resources obtained after the Production
     */
    public List<Resource> getOutput() {
        if(this.activated) {
            List<Resource> clone = new ArrayList<>(this.output.size());
            for (Resource item : this.output) if (item.amount() > 0) clone.add(item);
            return clone;
        } else return null;
    }

    /**
     * This method turns the "selected" attribute to true
     * @return true when this Production is selected
     */
    public boolean isActivated() {
        return this.activated;
    }

    /**
     * This method transform the input resource in output resource if given one matches required one
     * @return the succeed of the operation
     */
    //todo mettere le eccezioni perchè ho due possibilità di fallimento
    public abstract boolean activate();

    /**
     * This method is used to insert resources in the production
     * @param resource the resource to insert
     * @return the succeed of the operation
     */
    public abstract boolean insertResource(Resource resource);

    /**
     * This method allow to set the normal production in case of unknown resource
     * @param normalProduction the production to set as normal production in case of unknown resource
     * @return the succeed of the operation
     */
    public abstract boolean setNormalProduction(NormalProduction normalProduction) throws IllegalNormalProduction;

    /**
     * This method reset the production
     * @return the succeed of the operation
     */
    public boolean reset() {
        this.addedResource.forEach(x -> x = x.buildNewOne(0));
        this.activated = false;
        return true;
    }

    /**
     * Returns a string representation of the object.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "PROD: req > " + this.required + "; out > " + this.output + "; illegal types: {" + this.illegalType + "}";
    }
}
