package it.polimi.ingsw.model.player.personalBoard.warehouse.depot;

import it.polimi.ingsw.litemodel.litewarehouse.LiteDepot;
import it.polimi.ingsw.model.MappableToLiteVersion;
import it.polimi.ingsw.model.exceptions.warehouse.NegativeResourcesDepotException;
import it.polimi.ingsw.model.resource.Resource;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * This interface is the representation of the Warehouse Depots that can be NormalDepot, SpecialDepot and Strongbox
 */
public interface Depot extends MappableToLiteVersion {

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
     * @return true if the Resources are correctly withdrawn
     * @throws NegativeResourcesDepotException if the depot doesn't have enough resources to be withdrawn
     */
    boolean withdraw(Resource output) throws NegativeResourcesDepotException;


    /**
     * This method returns a list of all the resources inside the Depot
     * @return a list of all the resources inside the Depot
     */
    List<Resource> viewResources();

    /**
     * This method checks if this Depot must have a check on the type of the Resources on this Depot
     * @return true if the Depot has a check on the type of the Resources inside
     */
    boolean checkTypeDepot();

    /**
     * return a lite version of the depot
     * @return the lite version
     */
    LiteDepot liteVersion();
}
