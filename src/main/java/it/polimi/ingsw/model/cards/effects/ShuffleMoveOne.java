package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.model.player.Player;

/**
 * This class is a part of the strategy pattern, it implements the interface Effect.
 */
public class ShuffleMoveOne implements Effect {

    /**
     * This method is activated by a SoloActionToken, it shuffle every SoloActionToken used previously and gives a FaithPoint to Lorenzo.
     * @param p the only player in the game.
     */
    @Override
    public void use(Player p) {
        p.moveLorenzo();
        p.getPersonalBoard(); // prendi il deck col getter
    }
}