package it.polimi.ingsw.model.resource;

/**
 * used to represent the EMPTY depot
 */
public class Empty implements Resource {
    /**
     * amount of the resource
     */
    int amount;

    /**
     * the constructor receive the amount of the resource
     * @param i set the amount
     */
    public Empty(int i){
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
     * empty can be stored in depots
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
     * return the EMPTY type
     * @return ResourceType.EMPTY
     */
    @Override
    public ResourceType type() {
        return ResourceType.EMPTY;
    }
}
