package it.polimi.ingsw.model.resource.resourceTypes;

/**
 * class that represent the resource SHIELD
 */
public class Shield  implements Resource {

    /**
     * shields can be stored in depots
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
