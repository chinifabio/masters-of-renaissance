package it.polimi.ingsw.model.player.personalBoard.warehouse.production;

import it.polimi.ingsw.model.resource.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the production where some resources are inserted as input to receive other resources in output.
 */
public abstract class Production{

    /**
     * This attribute is the identifier of the Production
     */
    private final ProductionID productionID;

    /**
     * This attribute is the list of the Resources required to activate the Production
     */
    private final List<Resource> required;
    /**
     * This attribute is the list of the Resources obtained after the Production
     */
    private final List<Resource> output;

    /**
     * This attribute indicates if this Production is selected
     */
    private boolean selected;

    /**
     * This attribute indicates the resources selected by the Player to activate this Production
     */
    private final List<Resource> addedResource;

    /**
     * This method is the constructor of the class
     */
    //TODO Togliere il paramtero ProductioID
    public Production(ProductionID productionID, List<Resource> required, List<Resource> output) {
        this.productionID = productionID;
        this.required = required;
        this.output = output;
        this.selected = false;
        this.addedResource = new ArrayList<>();
    }

    /**
     * This method returns the List of Resources requests
     */
    public abstract List<Resource> getRequired();

    /**
     * This method returns the List of Resources obtained after the Production
     */
    public abstract List<Resource> getOutput();

    /**
     * This method turns the "selected" attribute to true
     * @return true when this Production is selected
     */
    public abstract boolean isSelected();

    public abstract boolean activate();

    public abstract boolean insertResource();

    public abstract boolean setNormalProduction();

    public abstract boolean reset();



}
