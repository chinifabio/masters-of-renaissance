package it.polimi.ingsw.model.player.state;

import it.polimi.ingsw.model.exceptions.IllegalMovesException;

public class MainActionDoneState extends State {
    /**
     * the constructor take the two final attribute of the state that are the personal board and the context
     * @param context the context
     */
    public MainActionDoneState(Context context) {
        super(context);
    }

    /**
     * player start the turn
     */
    @Override
    public void startTurn() throws IllegalMovesException {
        throw new IllegalMovesException("turn already started");
    }

    /**
     * player has done a leader action
     */
    @Override
    public void doMainAction() throws IllegalMovesException {
        throw new IllegalMovesException("no more action can be done");
    }

    /**
     * player end his turn
     */
    @Override
    public void endTurn() {
        context.setState(new NotHisTurnState(context));
    }

    /**
     * can the player do staff?
     *
     * @return true yes, false no
     */
    @Override
    public boolean doStaff() {
        return true;
    }
}
