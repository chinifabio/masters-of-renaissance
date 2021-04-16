package it.polimi.ingsw.model.resource.strategy;

import it.polimi.ingsw.model.exceptions.warehouse.UnobtainableResourceException;
import it.polimi.ingsw.model.player.PlayerReactEffect;

/**
 * strategy for launch exception when a resource not obtainable is obtained
 */
public class LaunchExceptionBehavior implements ObtainStrategy{

    public LaunchExceptionBehavior() {}

    /**
     * always throw an exception because unknown resource is not obtainable
     * @param player player that obtain the resource
     * @throws UnobtainableResourceException thrown because unknown resource is not obtainable
     */
    @Override
    public void obtain(PlayerReactEffect player) throws UnobtainableResourceException {
        throw new UnobtainableResourceException("exception: The Player can't receive this resource");
    }
}
