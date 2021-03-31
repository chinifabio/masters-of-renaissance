package it.polimi.ingsw.model.player.personalBoard;

import it.polimi.ingsw.model.resource.ResourceLoot;

import java.util.List;

/**
 * This class represents the production where some resources are inserted as input to receive other resources in output.
 */
public class Production {
    /**
     * This attribute is the identifier of the Production
     */
    private ProductionID productionID;
    /**
     * This attribute is the list of the Resources required to activate the Production
     */
    private List<ResourceLoot> required;
    /**
     * This attribute is the list of the Resources obtained after the Production
     */
    private List<ResourceLoot> output;

    /**
     * This method is the constructor of the class
     */
    public Production production;
    /**
     * This method returns the List of Resources requests
     */
    public List<ResourceLoot> getRequired;
    /**
     * This method returns the List of Resources obtained after the Production
     */
    public List<ResourceLoot> getOutput;
}
