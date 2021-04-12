package it.polimi.ingsw.model.player.personalBoard.warehouse.production;

import it.polimi.ingsw.model.exceptions.NegativeResourcesDepotException;
import it.polimi.ingsw.model.player.personalBoard.warehouse.Bucket;
import it.polimi.ingsw.model.resource.Resource;

import java.util.List;

public interface ResourceBucket {

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
     * This method returns a list of all the resources inside the Depot if it can stores more types of Resources
     * @return a list of all the resources inside the Depot
     */
    List<Resource> viewAllResources();

    /**
     * This method returns the type of Bucket that it can be Depot or Production
     * @return Bucket type
     */
    Bucket bucketType();

}
