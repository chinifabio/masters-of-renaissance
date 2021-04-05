package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.resource.ResourceType;

/**
 * This class is a part of the strategy pattern, it implements the interface Effect.
 */
public class AddDepot implements Effect{

    /**
     * This is the constructor of the class. It needs a ResourceType that will be used to determine what Resource can be stored in this depot.
     * @param res that defines what can be stored.
     */
    public AddDepot(ResourceType res) {
        this.res = res;
    }

    /**
     * This attribute saves the type of resource that can be stored in the depot.
     */
    private ResourceType res;

    /**
     * This method is activated by a LeaderCard, it adds a depot that contains up to two resources.
     * @param p the player that is getting a new depot.
     */
    @Override
    public void use(Player p) {
        p.addDepot();
    }
}
