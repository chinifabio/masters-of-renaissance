package it.polimi.ingsw.model.resource.strategy;

import it.polimi.ingsw.model.player.PlayerModifier;

/**
 * strategy for obtaining faithpoint from resources
 */
public class GiveFaithPointBehavior implements ObtainStrategy{
    /**
     * amount of faith point to give to the player
     */
    int amount;

    /**
     * the constructor receive the amount of faithpoint
     * @param amount amount of the resource
     */
    public GiveFaithPointBehavior(int amount) {
        this.amount = amount;
    }

    /**
     * when someone obtain a resource with this strategy receive an amount of faithpoint
     * @param player player
     */
    @Override
    public void obtain(PlayerModifier player) {
        player.moveFaithMarker(amount);
    }
}
