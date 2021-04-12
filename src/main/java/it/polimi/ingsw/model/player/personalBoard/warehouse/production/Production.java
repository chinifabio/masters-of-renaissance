package it.polimi.ingsw.model.player.personalBoard.warehouse.production;

import it.polimi.ingsw.model.exceptions.NegativeResourcesDepotException;
import it.polimi.ingsw.model.player.personalBoard.warehouse.Bucket;
import it.polimi.ingsw.model.requisite.ResourceRequisite;
import it.polimi.ingsw.model.resource.Resource;

import java.util.List;

/**
 * This class represents the production where some resources are inserted as input to receive other resources in output.
 */
public class Production implements ResourceBucket{

    /**
     * This attribute is the identifier of the Production
     */
    private final ProductionID productionID;

    /**
     * This attribute is the list of the Resources required to activate the Production
     */
    private final List<ResourceRequisite> required;
    /**
     * This attribute is the list of the Resources obtained after the Production
     */
    private final List<ResourceRequisite> output;

    /**
     * This attribute indicates if this Production is selected
     */
    private boolean selected;

    private List<Resource> selectedResources;

    /**
     * This method is the constructor of the class
     */
    public Production(ProductionID productionID, List<ResourceRequisite> required, List<ResourceRequisite> output) {
        this.productionID = productionID;
        this.required = required;
        this.output = output;
        this.selected = false;
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

    /**
     * This method turns the "selected" attribute to true
     * @return true when this Production is selected
     */
    public boolean isSelected(){
        return selected = true;
    }


    /**
     * This method inserts the resources into the Depot
     * @param input is the resource that will be inserted
     */
    @Override
    public boolean insert(Resource input) {
        return false;
    }

    /**
     * This method removes the resources from the Depot
     * @param output is the resource that will be withdrawn
     * @return true if the Resources are correctly withdrawn
     * @throws NegativeResourcesDepotException if the depot doesn't have enough resources to be withdrawn
     */
    @Override
    public boolean withdraw(Resource output) throws NegativeResourcesDepotException {
        return false;
    }

    /**
     * This method returns a list of all the resources inside the Depot if it can stores more types of Resources
     *
     * @return a list of all the resources inside the Depot
     */
    @Override
    public List<Resource> viewAllResources() {
        return null;
    }

    @Override
    public Bucket bucketType() {
        return Bucket.PRODUCTION;
    }

}
