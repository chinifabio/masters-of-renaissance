package it.polimi.ingsw.model.resource.resourceTypes;

/**
 * used to represent the FAITHPOINT depot
 */
public class FaithPoint  implements Resource {

    /**
     * faith points can't be stored
     * @return false
     */
    @Override
    public boolean isStorable() {
        return false;
    }

    /**
     * riceve il player e gli aggiunge 1 punto fede
     */
    @Override
    public void onObtain() {
        // da implementare
    }

    /**
     * return the FAITHPOINT type
     * @return ResourceType.FAITHPOINT
     */
    @Override
    public ResourceType type() {
        return ResourceType.FAITHPOINT;
    }
}
