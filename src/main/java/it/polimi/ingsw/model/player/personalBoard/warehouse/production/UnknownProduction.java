package it.polimi.ingsw.model.player.personalBoard.warehouse.production;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.litewarehouse.LiteProduction;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalNormalProduction;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
import it.polimi.ingsw.model.exceptions.warehouse.production.UnknownUnspecifiedException;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;

import java.util.*;

/**
 * This method represents the UnknownProduction that are the Productions with one or more unknown resources,
 * chosen by the player
 */
public class UnknownProduction extends Production{

    /**
     * this normal production allow to an unknown production to work as a normal production
     */
    private Optional<NormalProduction> normal = Optional.empty();

    /**
     * contains the number of unknown resources in the required list
     */
    private final int unknownInRequired;

    /**
     * contains the number of unknown resources in the output list
     */
    private final int unknownInOutput;

    /**
     * The number of faith point in the output
     */
    private final int fpInOutput;

    /**
     * This method is the constructor of the class
     * @param newRequired list of required
     * @param newOutput list of output
     * @throws IllegalTypeInProduction if there are IllegalResources in the Production
     */
    @JsonCreator
    public UnknownProduction(@JsonProperty("required") List<Resource> newRequired, @JsonProperty("output") List<Resource> newOutput) throws IllegalTypeInProduction {
        super(newRequired, newOutput, Collections.singletonList(ResourceType.EMPTY));
        unknownInRequired = required.stream().filter(x -> x.type() == ResourceType.UNKNOWN).findAny().orElse(ResourceBuilder.buildUnknown()).amount();
        unknownInOutput = output.stream().filter(x -> x.type() == ResourceType.UNKNOWN).findAny().orElse(ResourceBuilder.buildUnknown()).amount();
        fpInOutput = output.stream().filter(x -> x.type() == ResourceType.FAITHPOINT).findAny().orElse(ResourceBuilder.buildFaithPoint(0)).amount();
    }

    /**
     * This method returns the List of Resources requests
     * @return the list of resources
     */
    @Override
    public List<Resource> getRequired() {
        return normal.isPresent() ?
            normal.get().getRequired() :
            super.getRequired();
    }

    /**
     * This method returns the List of Resources obtained after the Production
     * @return the list of resources
     */
    @Override
    public List<Resource> getOutput() {
        return normal.isPresent() ?
                normal.get().getOutput() :
                super.getOutput();
    }

    /**
     * This method return the list of the Resources added by the Player
     * @return the list of resources
     */
    @Override
    public List<Resource> getAddedResource() {
        return normal.isPresent() ?
                normal.get().getAddedResource() :
                super.getAddedResource();
    }

    /**
     * This method transform the input resource in output resource if given one matches required one
     * @return the succeed of the operation
     */
    @Override
    public boolean activate() {
        return normal.isPresent() && normal.get().activate();
    }

    /**
     * This method turns the "activated" attribute to true
     *
     * @return true when this Production is activated
     */
    @Override
    public boolean isActivated() {
        return normal.isPresent() && normal.get().isActivated();
    }

    /**
     * This method indicates if the player added resources to activate the production
     *
     * @return true if the Player added resources
     */
    @Override
    public boolean isSelected() {
        return normal.isPresent() && normal.get().isSelected();
    }

    /**
     * This method is used to insert resources in the production
     * @param resource the resource to insert
     * @return the succeed of the operation
     */
    @Override
    public boolean insertResource(Resource resource) {
        return normal.isPresent() && normal.get().insertResource(resource);
    }

    /**
     * This method allow to set the normal production in case of unknown resource
     * @param normalProduction the production to set as normal production in case of unknown resource
     * @return the succeed of the operation
     */
    @Override
    public boolean setNormalProduction(NormalProduction normalProduction) throws IllegalNormalProduction {
        int counter = 0;
        int tempSum;
        List<Resource> lnorm;
        List<Resource> lthis;

        lnorm = ResourceBuilder.rearrangeResourceList(normalProduction.required);
        lthis = ResourceBuilder.rearrangeResourceList(required);
        for(int i = 0; i < required.size(); i++) {
            tempSum = lnorm.get(i).amount() - lthis.get(i).amount();
            if(lthis.get(i).type() == ResourceType.UNKNOWN) tempSum = 0;
            if(tempSum < 0) throw new IllegalNormalProduction("too few " + lnorm.get(i).type() + " in the required resources");
            counter += tempSum;
        }
        if(counter != unknownInRequired) throw new IllegalNormalProduction("Incompatible required list of resource");

        counter = 0;
        lnorm = ResourceBuilder.rearrangeResourceList(normalProduction.output);
        lthis = ResourceBuilder.rearrangeResourceList(output);
        for(int i = 0; i < this.output.size(); i++) {
            tempSum = lnorm.get(i).amount() - lthis.get(i).amount(); // because resources can't have a new lower number
            if(lthis.get(i).type() == ResourceType.UNKNOWN) tempSum = 0; // because unknown must decrease their number
            if(tempSum < 0) throw new IllegalNormalProduction( "too few " + lnorm.get(i).type() + " in the required resources");
            counter += tempSum;
        }
        if(counter != unknownInOutput) throw new IllegalNormalProduction("Incompatible output list of resource");

        if (normalProduction.output.stream().filter(x -> x.type() == ResourceType.FAITHPOINT).findAny().orElse(ResourceBuilder.buildFaithPoint(fpInOutput)).amount() != fpInOutput)
            throw new IllegalNormalProduction("Faith points in output does not match the original number (" + fpInOutput + ")");
        if (normalProduction.required.stream().filter(x -> x.type() == ResourceType.FAITHPOINT).findAny().orElse(ResourceBuilder.buildFaithPoint(0)).amount() != 0)
            throw new IllegalNormalProduction("You can't use faith points in required");

        normal = Optional.of(normalProduction);
        return true;
    }

    /**
     * This method reset the production
     * @return the succeed of the operation
     */
    @Override
    public boolean reset() {
        normal = Optional.empty();
        return super.reset();
    }
}
