package it.polimi.ingsw.model.player.personalBoard;

import it.polimi.ingsw.model.requisite.ResourceRequisite;

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
    private final List<ResourceRequisite> required;
    /**
     * This attribute is the list of the Resources obtained after the Production
     */
    private final List<ResourceRequisite> output;

    /**
     * This method is the constructor of the class
     */
    public Production() {
        //Da aggiornare con i parametri
        this.required = null;
        this.output = null;
    }

    /**
     * This method returns the List of Resources requests
     */
    public List<ResourceRequisite> getRequired(){
        return required;
    }
    /**
     * This method returns the List of Resources obtained after the Production
     */
    public List<ResourceRequisite> getOutput(){
        return output;
    }
}
