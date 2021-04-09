package it.polimi.ingsw.model.player.state;

/**
 * abstract class that need to have basics of concrete player states
 */
public abstract class State implements StateChanger{
    /**
     * the context to refer for changing the current state of the player
     */
    protected final Context context;

    /**
     * the constructor take the two final attribute of the state that are the personal board and the context
     * @param context  the context
     */
    protected State(Context context) {
        this.context = context;
    }

    /**
     * can the player do staff?
     * @return true yes, false no
     */
    public abstract boolean doStaff();
}
