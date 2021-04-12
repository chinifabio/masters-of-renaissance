package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.model.player.PlayerReactEffect;

/**
 * This class is a part of the strategy pattern, it implements the interface Effect.
 */
public class MoveTwo implements Effect{

    /**
     * This method is activated by a SoloActionToken, it gives two FaithPoint to Lorenzo.
     * @param p the only player in the game.
     */
    @Override
    public void use(PlayerReactEffect p) {
        p.moveFaithMarker(2); // aggiungere movimento 2
    }
}
