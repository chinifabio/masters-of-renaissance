package it.polimi.ingsw.model.resource.resourceTypes;

/**
 * used to represent the STONE depot
 */
public class Stone  implements Resource {

    /**
     * stones can be stored in depots
     * @return true
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
     * return the SHIELD type
     * @return ResourceType.SHIELD
     */
    @Override
    public ResourceType type() {
        return ResourceType.SHIELD;
    }
}
