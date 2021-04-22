package it.polimi.ingsw.model.requisite;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.exceptions.requisite.LootTypeException;
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
     * Number of the same type of card requested
     */
    private int amount;

    /**
     * exception thrown in case someone calls getType() method
     */
    private Exception LootTypeException;

    /**
     * constructor that need the level, color
     * @param level of the card
     * @param color of the card
     */
    @JsonCreator
    public CardRequisite(@JsonProperty("level") LevelDevCard level, @JsonProperty("color") ColorDevCard color, @JsonProperty("amount") int amount) {
        this.level = level;
        this.color = color;
        this.amount = amount;
    }

    /**
     * if this method is invoked there is en error
     * @throws LootTypeException wrong method called
     */
    @Override
    public ResourceType getType() throws LootTypeException {
        throw new LootTypeException();
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
     * return the amount of card that is always 1, in case of same card required there is multiple instance of Requisite
     *
     * @return number of loot
     */
    @Override
    public int getAmount() {
        return this.amount;
    }

    @Override
    public String toString() {
        return "CardReq{" +
                ", lev=" + level +
                ", color=" + color +
                '}';
    }

    @Override
    public RequisiteType getRequisiteType() {
        return RequisiteType.CARD;
    }
}
