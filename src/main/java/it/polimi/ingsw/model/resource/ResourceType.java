package it.polimi.ingsw.model.resource;

import it.polimi.ingsw.TextColors;

/**
 * types for the resources
 */
public enum ResourceType {
    COIN, STONE, SHIELD, SERVANT, FAITHPOINT, UNKNOWN, EMPTY;

    /**
     * Returns the name of this enum constant, as contained in the
     * declaration.
     * @return the name of this enum constant
     */
    @Override
    public String toString() {
        return TextColors.colorResourceType(this);
    }
}
