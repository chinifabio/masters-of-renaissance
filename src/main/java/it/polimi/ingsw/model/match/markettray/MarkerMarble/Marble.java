package it.polimi.ingsw.model.match.markettray.MarkerMarble;

import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;

/**
 * interface that contains the method to handle marbles obtained from the marketTray
 */
public class Marble {
    /**
     * the color of the marble
     */
    private MarbleColor color;
    /**
     * the resource associated to the color
     */
    private ResourceType toResource;

    /**
     * the constructor take the color and the resource mapped
     * @param color color of the marble
     * @param toResource resourceType to build the resource when someone request it
     */
    protected Marble(MarbleColor color, ResourceType toResource) {
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
    public Resource toResource(ResourceType whiteCase) {
        return this.color == MarbleColor.WHITE ?
                ResourceBuilder.buildFromType(whiteCase, 1):
                ResourceBuilder.buildFromType(toResource, 1);

    }

    /**
     * copy the marble in a new instance
     * @return new instance equals this
     */
    public Marble copy(){
        return new Marble(color, toResource);
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
}
