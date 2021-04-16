package it.polimi.ingsw.model.match.markettray.MarkerMarble;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceType;

import java.util.Optional;

public class PaintableMarble extends Marble{

    private Optional<Marble> marble;

    /**
     * build a new white marble which is paintable
     * @param color
     */
    @JsonCreator
    protected PaintableMarble(@JsonProperty("color") MarbleColor color) {
        super(color, ResourceType.EMPTY);
        this.marble = Optional.empty();
    }

    /**
     * build a new white marble which is paintable
     * @param color
     * @param optional the marble painted
     */
    private PaintableMarble(MarbleColor color, Optional<Marble> optional) {
        super(color, ResourceType.EMPTY);
        this.marble = optional;
    }

    /**
     * paint this marble
     * @return true beacouse white marble is paintable
     */
    @Override
    public boolean isPaintable() {
        return true;
    }

    @Override
    public void paint(Marble painted){
        System.out.println("painted: " + painted);
        this.marble = Optional.of(painted);
    }

    /**
     * return the resource associated to the color
     *
     * @return the mapped resource
     */
    @Override
    public Resource toResource() {
        return this.marble.isPresent() ?
                this.marble.get().toResource() :
                super.toResource();
    }


    /**
     * reset the paint of this marble
     */
    @Override
    public void unPaint() {
        this.marble = Optional.empty();
    }

    /**
     * copy the marble in a new instance
     *
     * @return new instance equals this
     */
    @Override
    public Marble copy() {
        return new PaintableMarble(MarbleColor.WHITE, this.marble);
    }
}
