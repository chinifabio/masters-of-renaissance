package it.polimi.ingsw.model.requisite;

import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.exceptions.LootTypeException;
import it.polimi.ingsw.model.resource.ResourceType;

/**
 * This class is used to store a group of resources that can be DevCards or Resources.
 *
 */

public interface Requisite {

    /**
     * return the amount of loot contained
     * @return number of loot
     */
    int getAmount();

    /**
     * if implemented by ResourceLoot returns the resource type, else throw a LootTypeException
     * @return resource type
     * @throws LootTypeException wrong method called
     */
    ResourceType getType() throws LootTypeException;

    /**
     * if implemented by CardLoot returns the devCard level, else throw a LootTypeException
     * @return level of the card
     * @throws LootTypeException wrong method called
     */
    LevelDevCard getLevel() throws LootTypeException;

    /**
     * if implemented by CardLoot returns the devCard color, else throw a LootTypeException
     * @return color of the card
     * @throws LootTypeException wrong method called
     */
    ColorDevCard getColor() throws LootTypeException;
}
