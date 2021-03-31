package it.polimi.ingsw.model.resource;

/**
 * used to represent the STONE depot
 */
public class Stone  implements Resource {
    /**
     * amount of the resource
     */
    int amount;

    /**
     * the constructor receive the amount of the resource
     * @param i set the amount
     */
    public Stone(int i){
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
     * return the STONE type
     * @return ResourceType.STONE
     */
    @Override
    public ResourceType type() {
        return ResourceType.STONE;
    }
}
