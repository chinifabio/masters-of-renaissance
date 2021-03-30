package it.polimi.ingsw.model.resource;

import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.exceptions.LootTypeException;
import it.polimi.ingsw.model.resource.resourceTypes.ResourceType;

/**
 * this is used to contain a Resource required to activate a LeaderCard or to buy a devCard
 */
public class ResourceLoot extends Loot {
    /**
     * resource type for the activation/buy
     */
    private ResourceType resource;

    /**
     * costructor need the resource type and
     * @param resource
     * @param i
     */
    public ResourceLoot(ResourceType resource, int i) {
        this.resource = resource;
        this.amount = i;
    }

    /**
     *  return the resource type required by the card
     * @return
     */
    @Override
    public ResourceType getType() {
        return resource;
    }

    /**
     * if this method is invoked there is en error
     * @return
     * @throws LootTypeException
     */
    @Override
    public LevelDevCard getLevel() throws LootTypeException {
        throw new LootTypeException("exception: ResourceLoot.getLevel();");
    }

    /**
     * if this method is invoked there is en error
     * @return
     * @throws LootTypeException
     */
    @Override
    public ColorDevCard getColor() throws LootTypeException {
        throw new LootTypeException("exception: ResourceLoot.getColot();");
    }
}
