package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.resource.ResourceType;

/**
 * This class is a part of the strategy pattern, it implements the interface Effect.
 */
public class AddDiscount implements Effect{

    /**
     * This is the constructor of the class. It needs the ResourceType that will be discounted.
     * @param t that will be discounted.
     */
    public AddDiscount(ResourceType t) {
        this.t = t;
    }

    /**
     * This attribute saves the discounted resource of the Card.
     */
    private ResourceType t;

    /**
     * This method is activated by a LeaderCard, it adds a discount to a specific resource when buying a DevCard.
     * @param p
     */
    @Override
    public void use(Player p) {
        p.addDiscount();
    }
}