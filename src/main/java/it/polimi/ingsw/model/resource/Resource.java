package it.polimi.ingsw.model.resource;

import it.polimi.ingsw.model.exceptions.UnobtainableResourceException;
import it.polimi.ingsw.model.player.PlayerModifier;
import it.polimi.ingsw.model.resource.strategy.ObtainStrategy;

/**
 * the resource class that represent a generic resource. its type is defined in the builder
 */
public class Resource{
    /**
     * flag to check before store a resource
     */
    private final boolean storable;
    /**
     * the type of the resource
     */
    private final ResourceType type;
    /**
     * the strategy for differentiate behaviours when the player obtain the resource
     */
    private final ObtainStrategy os;
    /**
     * the amount of the resource
     */
    private int amount;

    /**
     * constructor for a default amount = 1
     * @param type the type of the resource
     * @param os strategy
     */
    protected Resource(boolean storable, ResourceType type, ObtainStrategy os) {
        this.os = os;
        this.amount = 1;
        this.storable = storable;
        this.type = type;
    }

    /**
     * constructor for a default amount = 1
     * @param type the type of the resource
     * @param os strategy
     */
    protected Resource(boolean storable, ResourceType type, ObtainStrategy os, int amount){
        this.type = type;
        this.os = os;
        this.amount = amount;
        this.storable = storable;
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
        if(equalsType(toMerge)) {
            this.amount += toMerge.amount;
            return true;
        } else return false;
    }

    /**
     * reduces the amount of this resource with the amount of the passed resource only if they are of the same type
     * @param toReduce is the Resource that will be removed
     * @return true if the operation worked fine, otherwise return false
     */
    public boolean reduce(Resource toReduce){
        if(equalsType(toReduce)) {
            this.amount -= toReduce.amount;
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
        if (obj instanceof Resource) return (((Resource) obj).type() == this.type && ((Resource) obj).amount() == this.amount);
        else return false;
    }

    /**
     * Indicates whether some other object is "equal type to" this one.
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     */
    public boolean equalsType(Object obj) {
        if (obj instanceof Resource) return (((Resource) obj).type() == this.type);
        else return false;
    }

    /**
     * create and return a new resource built with the same attributes of this class, except for the amount passed as parameter
     * @param amount amount of the new resource
     * @return the new resource
     */
    public Resource buildNewOne(int amount) {
        return new Resource(storable, type, os, amount);
    }

    /**
     * create and return a new resource built with the same attributes of this class
     * @return the new resource
     */
    public Resource buildNewOne() {
        return new Resource(storable, type, os, amount);
    }

    /**
     * Returns a string representation of the object.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "resource: type -> "+type.toString()+" amount -> " + amount;
    }

    /**
     * return the value of storable flag
     * @return true if storable
     */
    public boolean isStorable() {
        return storable;
    }
}
