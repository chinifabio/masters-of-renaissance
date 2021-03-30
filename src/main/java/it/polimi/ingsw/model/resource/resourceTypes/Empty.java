package it.polimi.ingsw.model.resource.resourceTypes;

/**
 * used to represent the empity depot
 */
public class Empty implements Resource {

    /**
     * empty can be stored in depots
     * @return
     */
    @Override
    public boolean isStorable() {
        return true;
    }

    /**
     * do nothing
     */
    @Override
    public void onObtain() {

    }

    /**
     * return the EMPTY type
     * @return ResourceType.EMPTY
     */
    @Override
    public ResourceType type() {
        return ResourceType.EMPTY;
    }
}
