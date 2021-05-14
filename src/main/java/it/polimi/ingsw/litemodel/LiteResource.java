package it.polimi.ingsw.litemodel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.resource.ResourceType;


public class LiteResource {

    private ResourceType type;

    private int amount;

    @JsonCreator
    public LiteResource(@JsonProperty("type") ResourceType type,
                        @JsonProperty("amount") int amount) {
        this.type = type;
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

    public ResourceType getType(){
        return this.type;
    }

    public int getAmount() {
        return this.amount;
    }
}
