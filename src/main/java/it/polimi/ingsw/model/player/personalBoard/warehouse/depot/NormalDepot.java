package it.polimi.ingsw.model.player.personalBoard.warehouse.depot;


import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiPredicate;


/**
 * This class is the Depot into the warehouse where the resources will be stored
 */
public class NormalDepot implements Depot {
    /**
     * This attribute is the type and the number of resources into the depot
     */
    private Resource resources;

    /**
     * This attribute is a constraint that verifies that the resources within the depot are of the same type
     */
    private List<BiPredicate<Resource, Resource>> constraints;

    /**
     * This method is the constructor of the class
     */
    public NormalDepot() {
        resources = ResourceBuilder.buildEmpty();
        this.constraints = new ArrayList<>();

    }

    /**
     * This method accept a lambda function predicate with two parameters: first one is always referred to the input resource,
     * second one is the resource already contained in the depot
     * @param constraint is the constraint that the depot checks before adding resources
     */
    public void addConstraint(BiPredicate<Resource,Resource> constraint){
        constraints.add(constraint);
    }
    /**
     * This method inserts the resources into the Depot
     * @param input is the resource that will be inserted
     */
    public boolean insert(Resource input) {
        AtomicBoolean testResult = new AtomicBoolean(true);
        this.constraints.forEach(x->{
            if (!x.test(input,this.resources)) testResult.set(false);
        });

        if (testResult.get()){
            if (resources.type() != ResourceType.EMPTY){
                this.resources.merge(input);
            }
            else { this.resources = input;}
            return true;
        }
        return  false;
    }

    /**
     * This method removes the resources from the Depot
     * @param output is the resource that will be withdrawn
     */
    public Resource withdraw(Resource output) {
        return null;
    }

    /**
     * This method returns the resources that are into the depot
     */
    public Resource viewResources() {
        return resources;
    }

}
