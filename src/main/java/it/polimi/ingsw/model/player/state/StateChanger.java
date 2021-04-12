package it.polimi.ingsw.model.player.state;

import it.polimi.ingsw.model.exceptions.IllegalMovesException;

/**
 * this method act as input in the state pattern and allow to change state from the outside of the automata
 */
public interface StateChanger {
    /**
     * player start the turn
     */
    void startTurnInput() throws IllegalMovesException;

    /**
     * player has done a leader action
     */
    void doMainActionInput() throws IllegalMovesException;

    /**
     * player end his turn
     */
    void endTurnInput() throws IllegalMovesException;
}
