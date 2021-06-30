package it.polimi.ingsw.model.resource;

import it.polimi.ingsw.view.cli.Colors;

import java.util.ArrayList;
import java.util.List;

/**
 * This enumeration indicates all the different type of Resources
 */
public enum ResourceType {
    COIN(true), STONE(true), SHIELD(true), SERVANT(true), FAITHPOINT(false), UNKNOWN(false), EMPTY(false);

    public final boolean storable;

    ResourceType(boolean s) {
        storable = s;
    }

    /**
     * Returns the name of this enum constant, as contained in the
     * declaration.
     * @return the name of this enum constant
     */
    @Override
    public String toString() {
        //return Colors.colorResourceType(this);
        return this.name();
    }

    /**
     * @return a List of storable resourceTypes
     */
    public static List<ResourceType> storable() {
        List<ResourceType> result = new ArrayList<>();
        for (ResourceType t : ResourceType.values()) if (t.storable) result.add(t);
        return result;
    }

    public String toColor(){
        return switch (this.toString()) {
            case "SHIELD" -> Colors.BLUE_BRIGHT;
            case "COIN" -> Colors.YELLOW_BRIGHT;
            case "STONE" -> Colors.BLACK_BRIGHT;
            case "SERVANT" -> Colors.PURPLE;
            default -> Colors.RED_BOLD;
        };
    }
}
