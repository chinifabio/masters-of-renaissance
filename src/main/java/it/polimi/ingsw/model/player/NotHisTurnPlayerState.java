package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.exceptions.PlayerStateException;

/**
 * This class is the State where the Player waits for his turn
 */
public class NotHisTurnPlayerState extends PlayerState {

    /**
     * the constructor take the two final attribute of the state that are the personal board and the context.
     * the player holdings are passed to not share it out of the player states and do information hiding
     *
     * @param context        the context
     */
    protected NotHisTurnPlayerState(Player context) {
        super(context, "it is not your turn");
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
     * This method starts the turn of the player
     * @throws PlayerStateException if the Player can't do this action
     */
    @Override
    public void startTurn() throws PlayerStateException {
        this.context.setState(new NoActionDonePlayerState(this.context));
    }

    // ------------------------ PLAYER ACTION IMPLEMENTATIONS --------------------------------------

}
