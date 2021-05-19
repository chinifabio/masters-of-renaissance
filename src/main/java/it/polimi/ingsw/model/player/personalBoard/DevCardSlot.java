package it.polimi.ingsw.model.player.personalBoard;

/**
 * This enumeration indicates the slots for the DevCards in the PersonalBoard
 */
public enum DevCardSlot {
    LEFT(0), CENTER(1), RIGHT(2);

    private final int index;

    DevCardSlot(int index) {
        this.index = index;
    }

}
