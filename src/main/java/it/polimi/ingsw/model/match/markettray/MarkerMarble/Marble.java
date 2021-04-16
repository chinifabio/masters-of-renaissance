package it.polimi.ingsw.model.match.markettray.MarkerMarble;

import com.fasterxml.jackson.annotation.*;
import it.polimi.ingsw.model.exceptions.UnpaintableMarbleException;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "Normal", value = Marble.class),
        @JsonSubTypes.Type(name = "White", value = PaintableMarble.class)
})
/**
 * interface that contains the method to handle marbles obtained from the marketTray
 */
public class Marble {
    /**
     * the color of the marble
     */
    @JsonProperty("color")
    protected MarbleColor color;
    /**
     * the resource associated to the color
     */
    protected ResourceType toResource;

    /**
     * for jackson
     */
    public Marble(){}

    /**
     * the constructor take the color and the resource mapped
     * @param color color of the marble
     * @param toResource resourceType to build the resource when someone request it
     */
    @JsonCreator
    protected Marble(@JsonProperty("color") MarbleColor color, @JsonProperty("toResource") ResourceType toResource) {
        this.color = color;
        this.toResource = toResource;
    }

    /**
     * return the color of the marble
     * @return color of the marble
     */
    public MarbleColor type() {
        return this.color;
    }

    /**
     * return the resource associated to the color
     * @return the mapped resource
     */
    public Resource toResource(){
        return ResourceBuilder.buildFromType(toResource, 1);
    }

    public boolean isPaintable(){
        return false;
    }

    /**
     * copy the marble in a new instance
     * @return new instance equals this
     */
    public Marble copy(){
        return new Marble(color, toResource);
    }

    /**
     *
     * @param painted
     * @throws UnpaintableMarbleException
     */
    public void paint(Marble painted) throws UnpaintableMarbleException {
        throw new UnpaintableMarbleException();
    }

    /**
     * two marble are equals if they are the same type
     * @param obj another marble
     * @return true if the two marbles are the same type
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Marble) {
            return (this.color == ((Marble) obj).color);
        } else return false;
    }

    /**
     * Returns a string representation of the object
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return this.color.toString();
    }

    /**
     * reset the paint of this marble
     */
    public void unPaint() {

    }
}
