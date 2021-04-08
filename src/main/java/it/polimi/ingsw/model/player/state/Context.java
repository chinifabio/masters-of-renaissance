package it.polimi.ingsw.model.player.state;

/**
 * provide the function set state needed to a context to change its state
 */
public interface Context {
    /**
     * receive a state and set it as current state
     * @param newState the new state of the context
     */
    void setState(State newState);
}
