package it.polimi.ingsw.litemodel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.resource.ResourceType;

/**
 * This class represents the lite version of a Depot
 */
public class LiteResource {

    /**
     * This attribute is the resource type of the resource
     */
    private final ResourceType type;

    /**
     * This attribute is the amount of resource this LiteResource has
     */
    private final int amount;

    /**
     * This is the construct of the class:
     * @param type to set
     * @param amount to set
     */
    @JsonCreator
    public LiteResource(@JsonProperty("type") ResourceType type,
                        @JsonProperty("amount") int amount) {
        this.type = type;
        this.amount = amount;
    }

    /**
     * This method serves no purpose apart from letting Jackson do his work
     */
    @JsonIgnore
    public boolean isStorable() {
        return true;
    }

    /**
     * This method serves no purpose apart from letting Jackson do his work
     */
    @JsonIgnore
    public boolean getStrategy() {
        return false;
    }

    /**
     * This method returns the resource type of the object
     * @return the ResourceType
     */
    public ResourceType getType(){
        return this.type;
    }

    /**
     * This method returns the amount of resources of the object
     * @return the amount of resources
     */
    public int getAmount() {
        return this.amount;
    }

    /**
     * This method compares two LiteResources
     * @param obj to check
     * @return true if two LiteResources have the same ResourceType
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LiteResource) return type == ((LiteResource) obj).getType();
        else return false;
    }

    /**
     * Returns a string representation of the object containing only his ResourceType
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return type.toString();
    }
}
