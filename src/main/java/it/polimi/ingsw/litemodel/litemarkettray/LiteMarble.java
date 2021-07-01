package it.polimi.ingsw.litemodel.litemarkettray;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.MarbleColor;
import it.polimi.ingsw.model.resource.ResourceType;

/**
 * This class represents the lite version of a Marble
 */
public class LiteMarble {

    /**
     * This attribute sets the color of a marble
     */
    private final MarbleColor color;

    /**
     * This attribute sets what resource
     */
    private final ResourceType toResource;

    /**
     * This attribute serves no purpose apart from letting Jackson do his work
     */
    private String type;

    /**
     * This is the constructor of the class
     * @param color to set
     * @param toResource to set
     */
    @JsonCreator
    public LiteMarble(@JsonProperty("color") MarbleColor color, @JsonProperty("toResource") ResourceType toResource) {
        this.color = color;
        this.toResource = toResource;
    }

    /**
     * This method serves no purpose apart from letting Jackson do his work
     */
    @JsonIgnore
    public String getType() {
        return type;
    }

    /**
     * This method returns the color of the marble
     * @return a MarbleColor
     */
    public MarbleColor getColor() {
        return color;
    }

    /**
     * This method returns the resource of the marble
     * @return a ResourceType
     */
    public ResourceType getToResource() {
        return toResource;
    }

    /**
     * This method returns a string representation of the object.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return this.color.toString();
    }
}
