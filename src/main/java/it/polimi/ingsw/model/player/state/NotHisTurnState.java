package it.polimi.ingsw.model.player.state;

import it.polimi.ingsw.model.exceptions.IllegalMovesException;

public class NotHisTurnState extends State {
    /**
     * the constructor take the two final attribute of the state that are the personal board and the context
     * @param context the context
     */
    public NotHisTurnState(Context context) {
        super(context);
    }

    /**
     * player start the turn
     */
    @Override
    public void startTurnInput() {
        context.setState(new NoActionDoneState(context));
    }

    /**
     * player has done a leader action
     */
    @Override
    public void doMainActionInput() throws IllegalMovesException {
        throw new IllegalMovesException("main action not in the player turn");
    }

    /**
     * player end his turn
     */
    @Override
    public void endTurnInput() throws IllegalMovesException {
        throw new IllegalMovesException("end turn not in the player turn");
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
}
