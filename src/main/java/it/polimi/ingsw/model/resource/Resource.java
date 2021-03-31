package it.polimi.ingsw.model.resource;

/**
 * permits to call methods to manage resource
 */
public interface Resource {
    /**
     * provide the resource type, it need to be override in new resources classes
     * @return the type of the resource
     */
    ResourceType type();

    /**
     * tells you if the resource is storable
     * @return true means it is storable, false not
     */
    boolean isStorable();

    /**
     * what the player should do when he obtains the resource
     */
    void onObtain();

    /**
     * return the amount of the resource
     * @return the amount of resource loot
     */
    int amount();
}
