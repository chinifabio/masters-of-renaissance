package it.polimi.ingsw.model.resource;

import it.polimi.ingsw.model.exceptions.NegativeResourceException;
import it.polimi.ingsw.model.exceptions.UnobtainableResourceException;
import it.polimi.ingsw.model.player.PlayerModifier;
import it.polimi.ingsw.model.resource.strategy.LaunchExceptionBehavior;
import it.polimi.ingsw.model.resource.strategy.ObtainStrategy;

/**
 * the resource class that represent a generic resource. its type is defined in the builder
 */
public class Resource{
    /**
     * the type of the resource
     */
    private ResourceType type;
    /**
     * the strategy for differentiate behaviours when the player obtain the resource
     */
    private ObtainStrategy os;
    /**
     * the amount of the resource
     */
    private int amount;

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
    protected Resource(ResourceType type, ObtainStrategy os, int amount){
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
     * return the amount of the resource
     * @return amount of the resource
     */
    public int amount() {
        return amount;
    }

    /**
     * merge a passed resource to this one only if they are the same type
     * @param toMerge resource to merge with this one
     * @return true if the operation worked fine, otherwise return false
     */
    public boolean merge(Resource toMerge) {
        if(equals(toMerge)) {
            this.amount += toMerge.amount;
            return true;
        } else return false;
    }

    /**
     * when the player obtain the resource it is called the obtain method of the strategy passed in the constructor
     * @param player the player that obtain the resource
     * @throws UnobtainableResourceException if the resource is not obtainable
     */
    public void onObtain(PlayerModifier player) throws UnobtainableResourceException {
        os.obtain(player);
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Resource) return (((Resource) obj).type() == this.type);
        else return false;
    }

    /**
     * Returns a string representation of the object.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "resource: type -> "+type.toString()+" amount -> " + amount;
    }
}
