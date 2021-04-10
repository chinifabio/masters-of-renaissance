package it.polimi.ingsw.model.player.personalBoard.warehouse;

import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;

/**
 * This class represents the movement of resources between depots
 */
public class MoveResource {
    /**
     * This attribute is the DepotSlot where the resources will be taken from
     */
    private DepotSlot from;

    /**
     * This attribute is the DepotSlot where the resources will be deposited
     */
    private DepotSlot dest;


    /**
     * This method is the constructor of the class
     * @param from is the DepotSlot where the resources will be taken from
     * @param dest is the DepotSlot where the resources will be deposited

     */
    public MoveResource(DepotSlot from, DepotSlot dest) {
        this.from = from;
        this.dest = dest;
    }

    /**
     * This method returns the DepotSlot from which the resources will be taken
     */
    public DepotSlot getFrom(){
        return from;
    }

    /**
     * This method returns the DepotSlot where the resources will be deposited
     */
    public DepotSlot getDest(){
        return dest;
    }
}
