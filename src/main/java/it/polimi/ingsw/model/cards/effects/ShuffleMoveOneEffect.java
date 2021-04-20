package it.polimi.ingsw.model.cards.effects;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.match.SoloTokenReaction;

/**
 * This class is a part of the strategy pattern, it implements the interface Effect.
 */
public class ShuffleMoveOneEffect extends Effect {

    @JsonCreator
    public ShuffleMoveOneEffect() {}

    /**
     * This method is activated by a SoloActionToken, it shuffle every SoloActionToken used previously and gives a FaithPoint to Lorenzo.
     * @param p the only player in the game.
     */
    @Override
    public void use(CardReaction p) {
        ((SoloTokenReaction) p).moveLorenzo(1);
        ((SoloTokenReaction) p).shuffleToken();
    }
}