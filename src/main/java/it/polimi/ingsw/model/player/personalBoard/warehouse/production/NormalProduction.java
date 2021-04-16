package it.polimi.ingsw.model.player.personalBoard.warehouse.production;

import it.polimi.ingsw.model.exceptions.productionException.IllegalTypeInProduction;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NormalProduction extends Production{

    /**
     * This method is the constructor of the class
     *
     * @param required
     * @param output
     */
    public NormalProduction(List<Resource> required, List<Resource> output) throws IllegalTypeInProduction {
        super(required, output, Arrays.asList(ResourceType.EMPTY, ResourceType.UNKNOWN));
    }

    /**
     * This method transform the input resource in output resource if given one matches required one
     *
     * @return the succeed of the operation
     */
    @Override
    public boolean activate() {
        if (this.activated) return false;

        boolean result = true;
        for(int i = 0; i < required.size(); i++) {
            if(!required.get(i).equals(addedResource.get(i))) result = false;
        }
        this.activated = result;
        return result;
    }

    /**
     * This method is used to insert resources in the production
     *
     * @param input the resource to insert
     * @return the succeed of the operation
     */
    // todo se la risorsa sborda ritorna falso
    @Override
    public boolean insertResource(Resource input) {
        if (illegalType.contains(input.type())) return false; // todo lanciare eccezione (?)

        // copy the stored resource of input resource type
        Resource stored = addedResource.stream().filter(x->x.equalsType(input)).reduce(null, (x, y) -> y);
        Resource copy = stored.buildNewOne();
        boolean result;

        // check if input can be stored
        result = copy.merge(input);
        result &= (copy.amount() <= required.stream().filter(x->x.equalsType(copy)).reduce(null, (x,y) -> y).amount());

        // store the input if it is legal
        if (result) stored.merge(input);

       return result;
    }

    /**
     * This method allow to set the normal production in case of unknown resource
     *
     * @param normalProduction the production to set as normal production in case of unknown resource
     * @return the succeed of the operation
     */
    @Override
    public boolean setNormalProduction(NormalProduction normalProduction) {
        return false;
    }

    /**
     * return a copy of the output without checking if added resource.
     * It is legal because in the warehouse we store only Production and this is a NormalProduction method
     * @return
     */
    public List<Resource> viewOutput() {
        List<Resource> clone = new ArrayList<>(this.output.size());
        for (Resource item : this.output) if (item.amount() > 0) clone.add(item);
        return clone;
    }
}