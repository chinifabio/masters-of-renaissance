package it.polimi.ingsw.model.requisite;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.litecards.literequirements.LiteColorCardRequisite;
import it.polimi.ingsw.litemodel.litecards.literequirements.LiteRequisite;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.exceptions.requisite.LootTypeException;
import it.polimi.ingsw.model.resource.ResourceType;

public class ColorCardRequisite implements Requisite{

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
     * @param color is the color of DevCard
     * @param amount is the number of DevCard with this color
     */
    @JsonCreator
    public ColorCardRequisite(@JsonProperty("color") ColorDevCard color, @JsonProperty("amount") int amount) {
        this.color = color;
        this.amount = amount;
    }

    /**
     * return the amount of the same type of card requested
     *
     * @return number of cards
     */
    @Override
    public int getAmount() {
        return this.amount;
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
     * This method can't be invoked by this class
     * @return nothing
     * @throws LootTypeException if this method is invoked by this class
     */
    @Override
    public LevelDevCard getLevel() throws LootTypeException {
        throw new LootTypeException();
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
     * This method indicates if the Requisite is a Card, Color or Resource
     * @return the RequisiteType COLOR
     */
    @Override
    public RequisiteType getRequisiteType() {
        return RequisiteType.COLOR;
    }

    /**
     * Return a lite version of the effect
     *
     * @return a lite version of the effect
     */
    @Override
    public LiteRequisite liteVersion() {
        return new LiteColorCardRequisite(this.color, this.amount);
    }

    @Override
    public String toString() {
        return "ColorCardReq{" +
                ", color = " + getColor() +
                ", amount = " + getAmount() +
                '}';
    }
}
