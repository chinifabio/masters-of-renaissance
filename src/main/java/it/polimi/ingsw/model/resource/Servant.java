package it.polimi.ingsw.model.resource;

/**
 * used to represent the SERVANT depot
 */
public class Servant  implements Resource {
    /**
     * amount of the resource
     */
    int amount;

    /**
     * the constructor receive the amount of the resource
     * @param i set the amount
     */
    public Servant(int i){
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
