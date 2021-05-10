package it.polimi.ingsw.litemodel.litemarkettray;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.MarbleColor;
import it.polimi.ingsw.model.resource.ResourceType;

public class LiteMarble {

    private final MarbleColor color;

    private final ResourceType toResource;

    private String type;

    @JsonCreator
    public LiteMarble(@JsonProperty("color") MarbleColor color,@JsonProperty("toResource") ResourceType toResource) {
        this.color = color;
        this.toResource = toResource;
    }

    @JsonIgnore
    public String getType() {
        return type;
    }

    public MarbleColor getColor() {
        return color;
    }

    public ResourceType getToResource() {
        return toResource;
    }
}
