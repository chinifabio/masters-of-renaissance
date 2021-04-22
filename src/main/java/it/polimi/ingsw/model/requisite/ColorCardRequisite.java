package it.polimi.ingsw.model.requisite;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.exceptions.requisite.LootTypeException;
import it.polimi.ingsw.model.resource.ResourceType;

public class ColorCardRequisite implements Requisite{
    /**
     * color of devCard
     */
    private ColorDevCard color;

    /**
     * Number of the same type of card requested
     */
    private int amount;

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
     * return the amount of loot contained
     *
     * @return number of loot
     */
    @Override
    public int getAmount() {
        return this.amount;
    }

    /**
     * if implemented by ResourceLoot returns the resource type, else throw a LootTypeException
     *
     * @return resource type
     * @throws LootTypeException wrong method called
     */
    @Override
    public ResourceType getType() throws LootTypeException {
        throw new LootTypeException();
    }

    /**
     * if implemented by CardLoot returns the devCard level, else throw a LootTypeException
     *
     * @return level of the card
     * @throws LootTypeException wrong method called
     */
    @Override
    public LevelDevCard getLevel() throws LootTypeException {
        throw new LootTypeException();
    }

    /**
     * if implemented by CardLoot returns the devCard color, else throw a LootTypeException
     *
     * @return color of the card
     */
    @Override
    public ColorDevCard getColor() {
        return this.color;
    }

    /**
     * This method indicates the type of Requisite
     *
     * @return the type of requisite, so CARD or RESOURCE
     */
    @Override
    public RequisiteType getRequisiteType() {
        return RequisiteType.COLOR;
    }

    @Override
    public String toString() {
        return "ColorCardReq{" +
                ", color = " + getColor() +
                ", amount = " + getAmount() +
                '}';
    }
}
