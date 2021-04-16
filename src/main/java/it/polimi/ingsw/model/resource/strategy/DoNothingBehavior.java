package it.polimi.ingsw.model.resource.strategy;

import it.polimi.ingsw.model.player.PlayerReactEffect;

/**
 * strategy for ObtainStategy that do nothing when player obtaint the resource
 */
public class DoNothingBehavior implements ObtainStrategy{
    public DoNothingBehavior() {}

    /**
     * do nothing
     * @param player player that has obtained the resource
     */
    @Override
    public void obtain(PlayerReactEffect player) {

    }
}
