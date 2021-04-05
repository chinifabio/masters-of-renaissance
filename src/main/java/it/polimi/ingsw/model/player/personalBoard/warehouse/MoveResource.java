package it.polimi.ingsw.model.player.personalBoard.warehouse;


import it.polimi.ingsw.model.resource.builder.Resource;

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
     * This attribute is the resources that will be moved
     */
    private Resource howMany;

    /**
     * This method returns the DepotSlot from which the resources will be taken
     */
    public DepotSlot getFrom;

    /**
     * This method returns the DepotSlot where the resources will be deposited
     */
    public DepotSlot getDest;

    /**
     * This method returns the resources that will be moved
     */
    public Resource getHowMany;
}
