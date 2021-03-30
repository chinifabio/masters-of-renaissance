package it.polimi.ingsw.model.resource;

import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.exceptions.LootTypeException;
import it.polimi.ingsw.model.resource.resourceTypes.ResourceType;

/**
 * This class is used to store a group of resources that can be DevCards or Resources.
 *
 */

public abstract class Loot {
    /**
     * number of abstract loot contained in the istance
     */
    protected int amount;

    /**
     * return the amount of loot contained
     * @return number of loot
     */
    public int getAmount() {
        return amount;
    }

    /**
     * if implemented by ResourceLoot returns the resource type, else throw a LootTypeException
     * @return resource type
     * @throws LootTypeException
     */
    public abstract ResourceType getType() throws LootTypeException;

    /**
     * if implemented by CardLoot returns the devCard level, else throw a LootTypeException
     * @return level of the card
     * @throws LootTypeException
     */
    public abstract LevelDevCard getLevel() throws LootTypeException;

    /**
     * if implemented by CardLoot returns the devCard color, else throw a LootTypeException
     * @return color of the card
     * @throws LootTypeException
     */
    public abstract ColorDevCard getColor() throws LootTypeException;
}
