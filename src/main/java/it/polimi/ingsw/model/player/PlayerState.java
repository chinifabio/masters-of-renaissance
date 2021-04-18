package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.exceptions.PlayerStateException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerAction;

/**
 * abstract class that need to have basics of concrete player states
 */
public abstract class PlayerState implements PlayerAction {
    /**
     * the context to refer for changing the current state of the player
     */
    protected final Player context;


    /**
     * the constructor take the two final attribute of the state that are the personal board and the context.
     * the player holdings are passed to not share it out of the player states and do information hiding
     * @param context the context
     */
    protected PlayerState(Player context) {
        this.context = context;
    }

    /**
     * can the player do staff?
     * @return true yes, false no
     */
    public abstract boolean doStaff();

    /**
     * this method start the turn of the player
     */
    public abstract void startTurn() throws PlayerStateException;
}
