package it.polimi.ingsw.model.resource;

/**
 * used to represent the FAITHPOINT depot
 */
public class FaithPoint  implements Resource {
    /**
     * amount of the resource
     */
    int amount;

    /**
     * the constructor receive the amount of the resource
     * @param i set the amount
     */
    public FaithPoint(int i){
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
     * faith points can't be stored
     * @return false
     */
    @Override
    public boolean isStorable() {
        return false;
    }

    /**
     * riceve il player e gli aggiunge 1 punto fede
     */
    @Override
    public void onObtain() {
        // da implementare
    }

    /**
     * return the FAITHPOINT type
     * @return ResourceType.FAITHPOINT
     */
    @Override
    public ResourceType type() {
        return ResourceType.FAITHPOINT;
    }
}
