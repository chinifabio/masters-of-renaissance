package it.polimi.ingsw.model.player.personalBoard.warehouse.depot;

/**
 * This enumeration identifies the different warehouse depots
 */
public enum DepotSlot {
    TOP, MIDDLE, BOTTOM, SPECIAL1, SPECIAL2, BUFFER, STRONGBOX;

    public static DepotSlot[] special() {
        return new DepotSlot[]{SPECIAL1, SPECIAL2};
    }

    // another idea can be passing a boolean value indicating if the depot is special or not
    // than when i need special depot i can for on .value() and if it is special .isSpecial() do stuff
}
