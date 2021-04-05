package it.polimi.ingsw.model.resource.builder;

import it.polimi.ingsw.model.exceptions.UnobtainableResourceException;
import it.polimi.ingsw.model.player.PlayerModifier;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.model.resource.strategy.ObtainStrategy;

/**
 * the resource class that represent a generic resource. its type is defined in the builder
 */
public class Resource{
    /**
     * the type of the resource
     */
    ResourceType type;
    /**
     * the strategy for differentiate behaviours when the player obtain the resource
     */
    ObtainStrategy os;
    /**
     * the amount of the resource
     */
    int amount;

    /**
     * constructor for a default amount = 1
     * @param type the type of the resource
     * @param os strategy
     */
    protected Resource(ResourceType type, ObtainStrategy os) {
        this.type = type;
        this.os = os;
        this.amount = 1;
    }

    /**
     * constructor for a default amount = 1
     * @param type the type of the resource
     * @param os strategy
     */
    protected Resource(ResourceType type, ObtainStrategy os, int amount) {
        this.type = type;
        this.os = os;
        this.amount = amount;
    }

    /**
     * return the type of the resource
     * @return resource type
     */
    public ResourceType type() {
        return type;
    }

    /**
     * return the amoutn of the resource
     * @return amount of the resource
     */
    public int amount() {
        return amount;
    }

    /**
     * when the player obtain the resource it is called the obtain method of the strategy passed in the constructor
     * @param player the player that obtain the resource
     * @throws UnobtainableResourceException if the resource is not obtainable
     */
    public void onObtain(PlayerModifier player) throws UnobtainableResourceException {
        os.obtain(player);
    }
}
