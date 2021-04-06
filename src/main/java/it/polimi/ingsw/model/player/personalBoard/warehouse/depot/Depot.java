package it.polimi.ingsw.model.player.personalBoard.warehouse.depot;

import it.polimi.ingsw.model.resource.Resource;
import java.util.function.BiPredicate;

public interface Depot {
    /**
     * This method accept a lambda function predicate with two parameters: first one is always referred to the input resource,
     * second one is the resource already contained in the depot
     * @param constraint is the constraint that the depot checks before adding resources
     */
    void addConstraint(BiPredicate<Resource,Resource> constraint);
    /**
     * This method inserts the resources into the Depot
     * @param input is the resource that will be inserted
     */
    boolean insert(Resource input);

    /**
     * This method removes the resources from the Depot
     * @param output is the resource that will be withdrawn
     */
    Resource withdraw(Resource output);

    /**
     * This method returns the resources that are into the depot
     */
    Resource viewResources();
}
