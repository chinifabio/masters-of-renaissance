package it.polimi.ingsw.model.resource.strategy;

import it.polimi.ingsw.model.player.PlayableCardReaction;

/**
 * strategy for ObtainStrategy that do nothing when player obtain the resource
 */
public class DoNothingBehavior implements ObtainStrategy{
    public DoNothingBehavior() {}

    /**
     * do nothing
     * @param player player that has obtained the resource
     */
    @Override
    public void obtain(PlayableCardReaction player) {

    }
}
