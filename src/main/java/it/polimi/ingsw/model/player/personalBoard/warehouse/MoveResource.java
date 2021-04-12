package it.polimi.ingsw.model.player.personalBoard.warehouse;

import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.Resource;

/**
 * This class represents the movement of resources between depots
 */
public class MoveResource {
    /**
     * This attribute is the DepotSlot where the resources will be taken from
     */
    private final DepotSlot from;

    /**
     * This attribute is the DepotSlot where the resources will be deposited
     */
    private final DepotSlot dest;

    /**
     * This attribute is the Resource to move between Depots
     */
    private final Resource resources;


    /**
     * This method is the constructor of the class
     * @param from is the DepotSlot where the resources will be taken from
     * @param dest is the DepotSlot where the resources will be deposited
     * @param resource is the Resource to move
     */
    public MoveResource(DepotSlot from, DepotSlot dest, Resource resource) {
        this.from = from;
        this.dest = dest;
        this.resources = resource;
    }

    /**
     * This method returns the DepotSlot from which the resources will be taken
     * @return from
     */
    public DepotSlot getFrom(){
        return from;
    }

    /**
     * This method returns the DepotSlot where the resources will be deposited
     * @return dest
     */
    public DepotSlot getDest(){
        return dest;
    }

    /**
     * This method returns the resources to move
     * @return resources
     */
    public Resource getResources(){
        return resources;
    }
}
