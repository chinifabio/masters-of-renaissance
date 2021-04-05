package it.polimi.ingsw.model.resource.builder;

import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.model.resource.strategy.GiveFaithPointBehavior;

/**
 * builder for faithpoint
 */
class FaithPointBuilder implements ResourceBuilder{
    /**
     * build the resource whit default amount = 1
     * @return the new resource
     */
    @Override
    public Resource build() {
        return new Resource(ResourceType.FAITHPOINT, new GiveFaithPointBehavior(1));
    }

    /**
     * build the resource whit the custom amount passed as parameter
     * @param amount amount of the resource created
     * @return the new resource
     */
    @Override
    public Resource build(int amount) {
        return new Resource(ResourceType.FAITHPOINT, new GiveFaithPointBehavior(amount), amount);
    }
}