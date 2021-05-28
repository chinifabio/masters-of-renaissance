package it.polimi.ingsw.model.player.personalBoard.warehouse.depot;

/**
 * This enumeration identifies the different warehouse depots
 */
public enum DepotSlot {
    TOP, MIDDLE, BOTTOM, SPECIAL1, SPECIAL2, BUFFER, DEVBUFFER, STRONGBOX;

    public static DepotSlot[] special() {
        return new DepotSlot[]{SPECIAL1, SPECIAL2};
    }
}
