package it.polimi.ingsw.litemodel.litecards.liteeffect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.resource.ResourceType;

public class LiteAddDiscountEffect extends LiteEffect{

    private final ResourceType resourceType;

    @JsonCreator
    public LiteAddDiscountEffect(@JsonProperty("resource") ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }
}
