package it.polimi.ingsw.model.player.state;

import it.polimi.ingsw.model.exceptions.game.movesexception.TurnStartedException;

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
    public void startTurnInput() throws TurnStartedException {
        throw new TurnStartedException();
    }

    /**
     * player has done a leader action
     */
    @Override
    public void doMainActionInput() {
        context.setState(new MainActionDoneState(context));
    }

    /**
     * player end his turn
     */
    @Override
    public void endTurnInput() {
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
