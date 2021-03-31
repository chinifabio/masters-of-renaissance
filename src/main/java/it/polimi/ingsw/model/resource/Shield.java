package it.polimi.ingsw.model.resource;

/**
 * used to represent the SHIELD depot
 */
public class Shield  implements Resource {
    /**
     * amount of the resource
     */
    int amount;

    /**
     * the constructor receive the amount of the resource
     * @param i set the amount
     */
    public Shield(int i){
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
