package it.polimi.ingsw.model.player;

import it.polimi.ingsw.litemodel.liteplayer.LiteState;
import it.polimi.ingsw.litemodel.liteplayer.NotHisTurn;
import it.polimi.ingsw.litemodel.liteplayer.PendingStart;
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
     * Receive the input to start the turn
     */
    @Override
    public void starTurn() {
        this.context.setState(new NoActionDonePlayerState(this.context));
    }

    // ------------------------ PLAYER ACTION IMPLEMENTATIONS --------------------------------------


    /**
     * Create a lite version of the class and serialize it in json
     *
     * @return the json representation of the lite version of the class
     */
    @Override
    public LiteState liteVersion() {
        return new NotHisTurn();
    }
}
