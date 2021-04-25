package it.polimi.ingsw.model.player;

public class PendingMatchStartPlayerState extends PlayerState {

    /**
     * the constructor take the two final attribute of the state that are the personal board and the context.
     * the player holdings are passed to not share it out of the player states and do information hiding
     *
     * @param context        the context
     */
    public PendingMatchStartPlayerState(Player context) {
        super(context, "match not started yet");
    }

    /**
     * can the player do staff?
     *
     * @return true yes, false no
     */
    @Override
    public boolean doStaff() {
        return false;
    }

    /**
     * this method start the turn of the player
     */
    @Override
    public void startTurn() {
        this.context.setState(new LeaderSelectionPlayerState(this.context));
    }

    // ----------------- PLAYER ACTION IMPLEMENTATIONS ---------------------------------


    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "PendingStart state";
    }
}
