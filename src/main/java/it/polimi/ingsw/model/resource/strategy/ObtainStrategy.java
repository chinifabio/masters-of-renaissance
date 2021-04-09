package it.polimi.ingsw.model.resource.strategy;

import it.polimi.ingsw.model.exceptions.UnobtainableResourceException;
import it.polimi.ingsw.model.player.PlayerReactEffect;

/**
 * strategy for obtaining resource
 */
public interface ObtainStrategy {
    /**
     * method to invoke when a resource is obtained
     * @param player the player who obtain the resource
     * @throws UnobtainableResourceException launched if the resource is not obtainable
     */
    void obtain(PlayerReactEffect player) throws UnobtainableResourceException;
}
