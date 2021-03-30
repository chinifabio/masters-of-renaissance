package it.polimi.ingsw.model.resource.resourceTypes;

/**
 * used to represent the COIN depot
 */
public class Coin implements Resource {
    /**
     * coin can be stored in depots
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
     * return the COIN type
     * @return ResourceType.COIN
     */
    @Override
    public ResourceType type() {
        return ResourceType.COIN;
    }
}
