package it.polimi.ingsw.model.resource.resourceTypes;

/**
 * class that represent the resource SERVANT
 */
public class Servant  implements Resource {

    /**
     * servant can be stored in depots
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
     * return the SERVANT type
     * @return ResourceType.SERVANT
     */
    @Override
    public ResourceType type() {
        return ResourceType.SERVANT;
    }
}
