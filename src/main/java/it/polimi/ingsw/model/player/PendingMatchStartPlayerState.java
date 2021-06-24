package it.polimi.ingsw.model.player;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;

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
        super(context, "Wait that all the players complete the initial selection phase!");
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
        this.context.setState(new NoActionDonePlayerState(this.context));
    }

    /**
     * Give the state of the player in case of reconnection
     *
     * @return the reconnection player state
     */
    @Override
    public PlayerState reconnectionState() {
        return new NotHisTurnPlayerState(this.context);
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
}
