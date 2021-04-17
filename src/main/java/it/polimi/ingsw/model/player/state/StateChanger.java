package it.polimi.ingsw.model.player.state;

import it.polimi.ingsw.model.exceptions.game.movesexception.MainActionDoneException;
import it.polimi.ingsw.model.exceptions.game.movesexception.NotHisTurnException;
import it.polimi.ingsw.model.exceptions.game.movesexception.TurnStartedException;

/**
 * this method act as input in the state pattern and allow to change state from the outside of the automata
 */
public interface StateChanger {
    /**
     * player start the turn
     */
    void startTurnInput() throws TurnStartedException;

    /**
     * player has done a leader action
     */
    void doMainActionInput() throws NotHisTurnException, MainActionDoneException;

    /**
     * player end his turn
     */
    void endTurnInput() throws NotHisTurnException;
}
