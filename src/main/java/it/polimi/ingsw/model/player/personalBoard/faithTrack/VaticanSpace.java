package it.polimi.ingsw.model.player.personalBoard.faithTrack;

/**
 * This enumeration indicates the different types of VaticanSpace which have different VictoryPoint and that are located
 * in different cells
 */
public enum VaticanSpace {
    NONE(0), FIRST(1), SECOND(2), THIRD(3);

    public final int ordinal;

    VaticanSpace(int value) {
        this.ordinal = value;
    }
}
