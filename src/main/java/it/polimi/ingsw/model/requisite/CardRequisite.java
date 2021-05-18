package it.polimi.ingsw.model.requisite;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.litecards.literequirements.LiteCardRequisite;
import it.polimi.ingsw.litemodel.litecards.literequirements.LiteRequisite;
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
    private final LevelDevCard level;
    /**
     * color of devCard
     */
    private final ColorDevCard color;

    /**
     * Number of the same type of card requested
     */
    private final int amount;


    /**
     * This method is the constructor of the class
     * @param level is the level of the card
     * @param color is the color of the card
     * @param amount is the number of card of the same type
     */
    @JsonCreator
    public CardRequisite(@JsonProperty("level") LevelDevCard level, @JsonProperty("color") ColorDevCard color, @JsonProperty("amount") int amount) {
        this.level = level;
        this.color = color;
        this.amount = amount;
    }

    /**
     * This method can't be invoked by this class
     * @return nothing
     * @throws LootTypeException if this method is invoked by this class
     */
    @Override
    public ResourceType getType() throws LootTypeException {
        throw new LootTypeException();
    }

    /**
     * Return the level of the devCard
     * @return level of the card
     */
    @Override
    public LevelDevCard getLevel() {
        return this.level;
    }

    /**
     * Return the color of the devCard
     * @return color of the card
     */
    @Override
    public ColorDevCard getColor() {
        return this.color;
    }

    /**
     * Return the amount of card of the same type that are required
     * @return number of cards
     */
    @Override
    public int getAmount() {
        return this.amount;
    }

    @Override
    public String toString() {
        return "CardReq{" +
                ", lev =" + getLevel() +
                ", color =" + getColor() +
                ", amount = " + getAmount() +
                '}';
    }

    /**
     * This method indicates if the Requisite is a Card o a Resource
     * @return the RequisiteType CARD
     */
    @Override
    public RequisiteType getRequisiteType() {
        return RequisiteType.CARD;
    }

    /**
     * Return a lite version of the effect
     *
     * @return a lite version of the effect
     */
    @Override
    public LiteRequisite liteVersion() {
        return new LiteCardRequisite(this.level, this.color, this.amount);
    }
}
