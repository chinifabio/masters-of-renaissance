package it.polimi.ingsw.model.resource;

/**
 * used to represent the COIN depot.
 * it must be visible only in the package to be used by ResourceLoot
 */
public class Coin implements Resource {
    /**
     * amount of the resource
     */
    int amount;

    /**
     * the constructor receive the amount of the resource
     * @param i set the amount
     */
    public Coin(int i){
        this.amount = i;
    }

    /**
     * return the amount of the resource
     *
     * @return amount of the resource
     */
    @Override
    public int amount() {
        return amount;
    }

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
