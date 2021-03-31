package it.polimi.ingsw.model.resource;

/**
 * used to represent the UNKNOWN resource
 */
public class Unknown implements Resource {
    /**
     * unknown is always zero
     *
     * @return amount of the resource is always 0
     */
    @Override
    public int amount() {
        return 0;
    }


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
