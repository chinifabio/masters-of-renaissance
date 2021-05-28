package it.polimi.ingsw.model.resource;

import it.polimi.ingsw.view.cli.Colors;

/**
 * This enumeration indicates all the different type of Resources
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
        return Colors.colorResourceType(this);
    }
}
