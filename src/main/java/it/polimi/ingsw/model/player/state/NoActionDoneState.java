package it.polimi.ingsw.model.player.state;

import it.polimi.ingsw.model.exceptions.IllegalMovesException;

public class NoActionDoneState extends State {
    /**
     * the constructor take the two final attribute of the state that are the personal board and the context
     * @param context the context
     */
    public NoActionDoneState(Context context) {
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
    public void mainActionDone() {
        context.setState(new MainActionDoneState(context));
    }

    /**
     * player end his turn
     */
    @Override
    public void endTurn() {
        context.setState(new NotHisTurnState(context));
    }
}
