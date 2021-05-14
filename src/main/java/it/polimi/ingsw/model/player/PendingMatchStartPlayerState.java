package it.polimi.ingsw.model.player;

import it.polimi.ingsw.litemodel.liteplayer.LiteState;
import it.polimi.ingsw.litemodel.liteplayer.PendingStart;

/**
 * This class is the State where the Player is waiting for Match to start
 */
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
     * can the player do stuff?
     *
     * @return true yes, false no
     */
    @Override
    public boolean doStuff() {
        return false;
    }

    /**
     * Receive the input to start the turn
     */
    @Override
    public void starTurn() {
        this.context.setState(new LeaderSelectionPlayerState(this.context));
    }

    // ----------------- PLAYER ACTION IMPLEMENTATIONS ---------------------------------


    /**
     * Returns a string representation of the object.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "PendingStart state";
    }

    /**
     * Create a lite version of the class and serialize it in json
     *
     * @return the json representation of the lite version of the class
     */
    @Override
    public LiteState liteVersion() {
        return new PendingStart();
    }
}
