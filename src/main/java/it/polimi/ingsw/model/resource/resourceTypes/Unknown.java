package it.polimi.ingsw.model.resource.resourceTypes;

/**
 * used to represent the unknown resource
 */
public class Unknown implements Resource {

    /**
     * unknown can't be stored in depots
     * @return false
     */
    @Override
    public boolean isStorable() {
        return false;
    }

    /**
     * do nothing
     */
    @Override
    public void onObtain() {

    }

    /**
     * return the UNKNOWN type
     * @return ResourceType.UNKNOWN
     */
    @Override
    public ResourceType type() {
        return ResourceType.UNKNOWN;
    }


}
