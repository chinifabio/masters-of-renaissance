package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.model.exceptions.faithtrack.IllegalMovesException;
import it.polimi.ingsw.model.exceptions.warehouse.WrongPointsException;
import it.polimi.ingsw.model.player.PlayerReactEffect;

/**
 * This class is a part of the strategy pattern, it implements the interface Effect.
 */
public class ShuffleMoveOneEffect extends Effect {
    public ShuffleMoveOneEffect() {
    }

    /**
     * This method is activated by a SoloActionToken, it shuffle every SoloActionToken used previously and gives a FaithPoint to Lorenzo.
     * @param p the only player in the game.
     */
    @Override
    public void use(PlayerReactEffect p) throws WrongPointsException, IllegalMovesException {
        System.out.println("Move one space");
        p.moveFaithMarker(1);
        System.out.println("Shuffling");
        p.shuffleToken();
    }
}