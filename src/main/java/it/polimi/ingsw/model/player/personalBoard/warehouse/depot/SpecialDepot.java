package it.polimi.ingsw.model.player.personalBoard.warehouse.depot;

import it.polimi.ingsw.model.resource.Resource;

import java.util.function.BiPredicate;

public class SpecialDepot implements Depot {
    /**
     * This method accept a lambda function predicate with two parameters: first one is always referred to the input resource,
     * second one is the resource already contained in the depot
     *
     * @param constraint is the constraint that the depot checks before adding resources
     */
    @Override
    public void addConstraint(BiPredicate<Resource, Resource> constraint) {

    }

    /**
     * This method inserts the resources into the Depot
     *
     * @param input is the resource that will be inserted
     */
    @Override
    public boolean insert(Resource input) {
        return false;
    }

    /**
     * This method removes the resources from the Depot
     *
     * @param output is the resource that will be withdrawn
     */
    @Override
    public Resource withdraw(Resource output) {
        return null;
    }

    /**
     * This method returns the resources that are into the depot
     */
    @Override
    public Resource viewResources() {
        return null;
    }
}
