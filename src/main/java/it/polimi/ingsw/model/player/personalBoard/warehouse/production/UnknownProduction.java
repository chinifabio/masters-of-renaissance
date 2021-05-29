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

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * This method represents the UnknownProduction that are the Productions with one or more unknown resources,
 * chosen by the player
 */
public class UnknownProduction extends Production{

    /**
     * this normal production allow to an unknown production to work as a normal production
     */
    private Optional<NormalProduction> normal;

    /**
     * contains the number of unknown resources in the required list
     */
    private final int unknownInRequired;


    /**
     * contains the number of unknown resources in the output list
     */
    private final int unknownInOutput;

    /**
     * This method is the constructor of the class
     * @param newRequired list of required
     * @param newOutput list of output
     * @throws IllegalTypeInProduction if there are IllegalResources in the Production
     */
    @JsonCreator
    public UnknownProduction(@JsonProperty("required") List<Resource> newRequired, @JsonProperty("output") List<Resource> newOutput) throws IllegalTypeInProduction {
        super(newRequired, newOutput, Collections.singletonList(ResourceType.EMPTY));
        this.normal = Optional.empty();
        this.unknownInRequired = Objects.requireNonNull(this.required.stream().filter(x -> x.type() == ResourceType.UNKNOWN).findAny().orElse(null)).amount();
        this.unknownInOutput = Objects.requireNonNull(this.output.stream().filter(x -> x.type() == ResourceType.UNKNOWN).findAny().orElse(null)).amount();
    }

    /**
     * This method returns the List of Resources requests
     * @return the list of resources
     */
    @Override
    public List<Resource> getRequired() {
        return this.normal.isPresent() ?
            this.normal.get().getRequired() :
            super.getRequired();
    }

    /**
     * This method returns the List of Resources obtained after the Production
     * @return the list of resources
     */
    @Override
    public List<Resource> getOutput() {
        return this.normal.isPresent() ?
                this.normal.get().getOutput() :
                super.getOutput();
    }

    /**
     * This method transform the input resource in output resource if given one matches required one
     * @return the succeed of the operation
     */
    @Override
    public boolean activate() {
        return this.normal.isPresent() && this.normal.get().activate();
    }

    /**
     * This method is used to insert resources in the production
     * @param resource the resource to insert
     * @return the succeed of the operation
     */
    @Override
    public boolean insertResource(Resource resource) throws UnknownUnspecifiedException {
        if (!this.normal.isPresent()){
            throw new UnknownUnspecifiedException();
        }
        boolean result = this.normal.get().insertResource(resource);
        if (result) this.selected = true;
        return result;
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

        lnorm = ResourceBuilder.rearrangeResourceList(normalProduction.getRequired());
        lthis = ResourceBuilder.rearrangeResourceList(this.required);
        for(int i = 0; i < this.required.size(); i++) {
            tempSum = lnorm.get(i).amount() - lthis.get(i).amount();
            if(lthis.get(i).type() == ResourceType.UNKNOWN) tempSum = 0;
            if(tempSum < 0) throw new IllegalNormalProduction("too few required resources");
            counter += tempSum;
        }
        if(counter > this.unknownInRequired) throw new IllegalNormalProduction("too many required resources");

        counter = 0;
        lnorm = ResourceBuilder.rearrangeResourceList(normalProduction.viewOutput());
        lthis = ResourceBuilder.rearrangeResourceList(this.output);
        for(int i = 0; i < this.required.size(); i++) {
            tempSum = lnorm.get(i).amount() - lthis.get(i).amount();
            if(lthis.get(i).type() == ResourceType.UNKNOWN) tempSum = 0;
            if(tempSum < 0 && lthis.get(i).type() != ResourceType.UNKNOWN) throw new IllegalNormalProduction( "too few out resources");
            counter += tempSum;
        }
        if(counter > this.unknownInOutput) throw new IllegalNormalProduction("too many output resources");

        this.normal = Optional.of(normalProduction);
        return true;
    }

    /**
     * This method reset the production
     * @return the succeed of the operation
     */
    @Override
    public boolean reset() {
        this.normal = Optional.empty();
        return super.reset();
    }

    /**
     * This method return the list of the Resources added by the Player
     * @return the list of resources
     */
    @Override
    public List<Resource> viewResourcesAdded() {
        return this.normal.isPresent() ?
                this.normal.get().viewResourcesAdded() :
                super.viewResourcesAdded();
    }

    /**
     * Create a lite version of the class and serialize it in json
     *
     * @return the json representation of the lite version of the class
     */
    @Override
    public LiteProduction liteVersion() {
        return new LiteProduction (
                ResourceBuilder.mapResource(this.required),
                ResourceBuilder.mapResource(this.addedResource),
                ResourceBuilder.mapResource(this.output)
        );
    }
}
