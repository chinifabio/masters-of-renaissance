package it.polimi.ingsw.litemodel.litecards.literequirements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.resource.ResourceType;

public class LiteResourceTypeResquisite {

    private final ResourceType resourceType;

    private final int amount;

    @JsonCreator
    public LiteResourceTypeResquisite(@JsonProperty("type") ResourceType resourceType,
                                      @JsonProperty("amount") int amount) {
        this.resourceType = resourceType;
        this.amount = amount;
    }

    @JsonIgnore
    public boolean isStorable() {
        return true;
    }

    @JsonIgnore
    public boolean getStrategy() {
        return false;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public int getAmount() {
        return amount;
    }
}
