package it.polimi.ingsw.model.match.markettray.MarkerMarble;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.litemodel.litemarkettray.LiteMarble;
import it.polimi.ingsw.model.MappableToLiteVersion;
import it.polimi.ingsw.model.exceptions.tray.UnpaintableMarbleException;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;


/**
 * Interface that contains the method to handle marbles obtained from the marketTray
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "Normal", value = Marble.class),
        @JsonSubTypes.Type(name = "White", value = PaintableMarble.class)
})
public class Marble implements MappableToLiteVersion {
    /**
     * The color of the marble
     */
    @JsonProperty("color")
    protected MarbleColor color;

    /**
     * The resource associated to the color
     */
    protected ResourceType toResource;


    /**
     * The constructor take the color and the resource mapped
     * @param color color of the marble
     * @param toResource resourceType to build the resource when someone request it
     */
    @JsonCreator
    public Marble(@JsonProperty("color") MarbleColor color, @JsonProperty("toResource") ResourceType toResource) {
        this.color = color;
        this.toResource = toResource;
    }

    /**
     * return the color of the marble
     * @return color of the marble
     */
    public MarbleColor color() {
        return this.color;
    }

    /**
     * return the resource associated to the color
     * @return the mapped resource
     */
    public Resource toResource(){
        return ResourceBuilder.buildFromType(toResource, 1);
    }

    /**
     * @return true if the marble can be painted
     */
    @JsonIgnore
    public boolean isPaintable(){
        return false;
    }

    /**
     * Copy the marble in a new instance
     * @return new instance equals this
     */
    public Marble copy(){
        return new Marble(color, toResource);
    }

    /**
     * Paint the marble
     * @param painted new marble color
     * @throws UnpaintableMarbleException this class is not paintable
     */
    public void paint(Marble painted) throws UnpaintableMarbleException {
        throw new UnpaintableMarbleException();
    }

    /**
     * Two marble are equals if they are of the same type
     * @param obj another marble
     * @return true if the two marbles are of the same type
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
    public void unPaint() {}

    /**
     * Create a lite version of the class and serialize it in json
     *
     * @return the json representation of the lite version of the class
     */
    @Override
    public LiteMarble liteVersion() {
        return new LiteMarble(this.color, this.toResource);
    }
}
