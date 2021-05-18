package it.polimi.ingsw.model.cards.effects;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.litemodel.litecards.liteeffect.LiteEffect;
import it.polimi.ingsw.litemodel.litecards.liteeffect.LiteMoveTwoEffect;
import it.polimi.ingsw.model.match.SoloTokenReaction;

/**
 * This class is a part of the strategy pattern, it implements the interface Effect.
 */
public class MoveTwoEffect extends Effect{

    /**
     * This method is the constructor of the class
     */
    @JsonCreator
    public MoveTwoEffect() {}

    /**
     * This method is activated by a SoloActionToken, it gives two FaithPoint to Lorenzo.
     * @param p the only player in the game.
     */
    @Override
    public void use(CardReaction p) {
        ((SoloTokenReaction) p).moveLorenzo(2);
    }

    /**
     * Return a lite version of the effect
     *
     * @return a lite version of the effect
     */
    @Override
    public LiteEffect liteVersion() {
        return new LiteMoveTwoEffect();
    }
}
