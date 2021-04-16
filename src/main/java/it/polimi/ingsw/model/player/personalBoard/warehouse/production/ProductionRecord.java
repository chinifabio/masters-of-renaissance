package it.polimi.ingsw.model.player.personalBoard.warehouse.production;

import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.Resource;

/**
 * This class records all the movements done by the Resources to activate Productions
 */
public class ProductionRecord {

    /**
     * This attribute is the DepotSlot where the resources will be taken from
     */
    private final DepotSlot from;

    /**
     * This attribute is the Production where the resources will be stored
     */
    private final ProductionID dest;

    /**
     * This attribute is the Resource to move between Depots
     */
    private final Resource resources;


    /**
     * This method is the constructor of the class
     * @param from is the DepotSlot where the resources will be taken from
     * @param dest is the ProductionID where the resources will be deposited
     * @param resource is the Resource to move
     */
    public ProductionRecord(DepotSlot from, ProductionID dest, Resource resource) {
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
     * This method returns the ProductionID where the resources will be deposited
     * @return dest
     */
    public ProductionID getDest(){
        return dest;
    }

    /**
     * This method returns the resources to move
     * @return resources
     */
    public Resource getResources(){
        return resources;
    }

    @Override
    public String toString() {
        return "From: " + this.from.toString() + ", Dest: " + this.dest.toString() + ", Resource: " + this.resources;
    }
}
