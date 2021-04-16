package it.polimi.ingsw.model.player.personalBoard.warehouse.production;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.exceptions.productionException.IllegalNormalProduction;
import it.polimi.ingsw.model.exceptions.productionException.IllegalTypeInProduction;
import it.polimi.ingsw.model.exceptions.productionException.UnknownUnspecifiedException;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UnknownProduction extends Production{

    /**
     * this normal production allow to an unknown production to work as a normal production
     */
    private Optional<NormalProduction> normal;

    /**
     * contains the number of unknown resources in the required list
     */
    private int unknownInRequired;


    /**
     * contains the number of unknown resources in the output list
     */
    private int unknownInOutput;

    /**
     * This method is the constructor of the class
     * @param newRequired
     * @param newOutput
     */
    @JsonCreator
    public UnknownProduction(@JsonProperty("required") List<Resource> newRequired, @JsonProperty("output") List<Resource> newOutput) throws IllegalTypeInProduction {
        super(newRequired, newOutput, Arrays.asList(ResourceType.EMPTY));
        this.normal = Optional.empty();
        this.unknownInRequired = this.required.stream().filter(x->x.type() == ResourceType.UNKNOWN).findAny().orElse(null).amount();
        this.unknownInOutput = this.output.stream().filter(x->x.type() == ResourceType.UNKNOWN).findAny().orElse(null).amount();
    }

    /**
     * This method returns the List of Resources requests
     */
    @Override
    public List<Resource> getRequired() {
        return this.normal.isPresent() ?
            this.normal.get().getRequired() :
            super.getRequired();
    }

    /**
     * This method returns the List of Resources obtained after the Production
     */
    @Override
    public List<Resource> getOutput() {
        return this.normal.isPresent() ?
                this.normal.get().getOutput() :
                super.getOutput();
    }

    /**
     * This method transform the input resource in output resource if given one matches required one
     *
     * @return the succeed of the operation
     */
    @Override
    public boolean activate() {
        return this.normal.isPresent() && this.normal.get().activate();
    }

    /**
     * This method is used to insert resources in the production
     *
     * @param resource the resource to insert
     * @return the succeed of the operation
     */
    @Override
    public boolean insertResource(Resource resource) throws UnknownUnspecifiedException {
        if (!this.normal.isPresent()){
            throw new UnknownUnspecifiedException("This production is unspecified");
        }
        boolean result = this.normal.get().insertResource(resource);
        if (result) this.selected = true;
        return result;
    }

    /**
     * This method allow to set the normal production in case of unknown resource
     *
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
            if(tempSum < 0) throw new IllegalNormalProduction(normalProduction, "too low req");
            counter += tempSum;
        }
        if(counter > this.unknownInRequired) throw new IllegalNormalProduction(normalProduction, "too many req");

        counter = 0;
        lnorm = ResourceBuilder.rearrangeResourceList(normalProduction.viewOutput());
        lthis = ResourceBuilder.rearrangeResourceList(this.output);
        for(int i = 0; i < this.required.size(); i++) {
            tempSum = lnorm.get(i).amount() - lthis.get(i).amount();
            if(lthis.get(i).type() == ResourceType.UNKNOWN) tempSum = 0;
            if(tempSum < 0 && lthis.get(i).type() != ResourceType.UNKNOWN) throw new IllegalNormalProduction(normalProduction, "too low out");
            counter += tempSum;
        }
        if(counter > this.unknownInOutput) throw new IllegalNormalProduction(normalProduction, "too many req");

        this.normal = Optional.of(normalProduction);
        return true;
    }

    /**
     * This method reset the production
     *
     * @return the succeed of the operation
     */
    @Override
    public boolean reset() {
        this.normal = Optional.empty();
        return super.reset();
    }

    @Override
    public List<Resource> viewResourcesAdded() {
        return this.normal.isPresent() ?
                this.normal.get().viewResourcesAdded() :
                super.viewResourcesAdded();
    }
}
