package it.polimi.ingsw.model.resource.resourceTypes;

/**
 * permits to call methods to manage resource
 */
public interface Resource {
    /**
     * provide the resource type
     * @return
     */
    public ResourceType type();

    /**
     * tells you if the resource is storable
     * @return
     */
    public boolean isStorable();

    /**
     * what the player should do when he obtains the resource
     */
    public void onObtain();
}
