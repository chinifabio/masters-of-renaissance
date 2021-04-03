package it.polimi.ingsw.model.requisite;

import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.exceptions.LootTypeException;
import it.polimi.ingsw.model.resource.ResourceType;

/**
 * this is used to contain a devCard required to activate a leaderCard
 */

public class CardRequisite implements Requisite {
    /**
     * level of devCard
     */
    private LevelDevCard level;
    /**
     * color of devCard
     */
    private ColorDevCard color;

    /**
     * exception thrown in case someone calls getType() method
     */
    private Exception LootTypeException;

    /**
     * costructor that need the level, color
     * @param level of the card
     * @param color of the card
     */
    public CardRequisite(LevelDevCard level, ColorDevCard color) {
        this.level = level;
        this.color = color;
    }

    /**
     * if this method is invoked there is en error
     * @throws LootTypeException wrong method called
     */
    @Override
    public ResourceType getType() throws LootTypeException {
        throw new LootTypeException("exception: CardLoot.getType();");
    }

    /**
     * return the level of the devCard
     * @return level of the card
     */
    @Override
    public LevelDevCard getLevel() {
        return level;
    }

    /**
     * return the color of the devCard
     * @return color of the card
     */
    @Override
    public ColorDevCard getColor() {
        return color;
    }

    /**
     * return the amount of card is always 1, in case of same card required there is multiple istance of Requisite
     *
     * @return number of loot
     */
    @Override
    public int getAmount() {
        return 1;
    }
}
