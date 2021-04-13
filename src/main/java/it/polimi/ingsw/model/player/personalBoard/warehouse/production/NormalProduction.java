package it.polimi.ingsw.model.player.personalBoard.warehouse.production;

import it.polimi.ingsw.model.requisite.ResourceRequisite;
import it.polimi.ingsw.model.resource.Resource;

import java.util.List;

public class NormalProduction extends Production {

    /**
     * This method is the constructor of the class
     *
     * @param productionID
     * @param required
     * @param output
     */
    public NormalProduction(ProductionID productionID, List<Resource> required, List<Resource> output) {
        super(productionID, required, output);
    }

    /**
     * This method returns the List of Resources requests
     */
    @Override
    public List<Resource> getRequired() {
        return null;
    }

    /**
     * This method returns the List of Resources obtained after the Production
     */
    @Override
    public List<Resource> getOutput() {
        return null;
    }

    /**
     * This method turns the "selected" attribute to true
     *
     * @return true when this Production is selected
     */
    @Override
    public boolean isSelected() {
        return false;
    }

    @Override
    public boolean activate() {
        return false;
    }

    @Override
    public boolean insertResource() {
        return false;
    }

    @Override
    public boolean setNormalProduction() {
        return false;
    }

    @Override
    public boolean reset() {
        return false;
    }
}
