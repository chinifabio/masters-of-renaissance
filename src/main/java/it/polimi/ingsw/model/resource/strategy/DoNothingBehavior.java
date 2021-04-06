package it.polimi.ingsw.model.resource.strategy;

import it.polimi.ingsw.model.player.PlayerModifier;

/**
 * strategy for ObtainStategy that do nothing when player obtaint the resource
 */
public class DoNothingBehavior implements ObtainStrategy{
    /**
     * do nothing
     * @param player player that has obtained the resource
     */
    @Override
    public void obtain(PlayerModifier player) {

    }
}
