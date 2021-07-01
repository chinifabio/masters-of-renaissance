package it.polimi.ingsw.model.player;

/**
 * This class is the State where are counted the VictoryPoints of the Player
 */
public class CountingPointsPlayerState extends PlayerState {
    /**
     * the constructor take the two final attribute of the state that are the personal board and the context.
     * the player holdings are passed to not share it out of the player states and do information hiding
     * @param context the context
     */
    public CountingPointsPlayerState(Player context) {
        super(context, "match ended");
    }

    /**
     * can the player do stuff?
     * @return true yes, false no
     */
    @Override
    public boolean doStuff() {
        return false;
    }

    /**
     * Give the state of the player in case of reconnection
     *
     * @return the reconnection player state
     */
    @Override
    public PlayerState handleDisconnection() {
        return this;
    }

    /**
     * Receive the input to start the turn
     */
    @Override
    public void starTurn() {
        this.context.match.turnDone();
    }
}
