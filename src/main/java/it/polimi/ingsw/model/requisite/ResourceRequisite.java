package it.polimi.ingsw.model.requisite;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.litecards.literequirements.LiteRequisite;
import it.polimi.ingsw.litemodel.litecards.literequirements.LiteResourceRequisite;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.exceptions.requisite.LootTypeException;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceType;

/**
 * this is used to contain a Resource required to activate a LeaderCard or to buy a devCard
 */
public class ResourceRequisite implements Requisite {

    /**
     * resource type for the activation/buy
     */
    private final Resource resource;

    /**
     * constructor need the resource type and
     * @param resource set the resource to handle
     */
    @JsonCreator
    public ResourceRequisite(@JsonProperty("resource") Resource resource) {
        this.resource = resource;
    }

    /**
     *  return the resource type required by the card
     * @return the type of the resource
     */
    @Override
    public ResourceType getType() {
        return resource.type();
    }

    /**
     * if this method is invoked there is en error
     * @throws LootTypeException wrong method called
     */
    @Override
    public LevelDevCard getLevel() throws LootTypeException {
        throw new LootTypeException();
    }

    /**
     * if this method is invoked there is en error
     * @throws LootTypeException wrong method called
     */
    @Override
    public ColorDevCard getColor() throws LootTypeException {
        throw new LootTypeException();
    }

    /**
     * return the amount of loot contained
     *
     * @return number of loot
     */
    @Override
    public int getAmount() {
        return resource.amount();
    }

    @Override
    public String toString() {
        return "ResourceReq{" +
                ", type =" + getType() +
                ", amount =" + getAmount() +
                '}';
    }
    /**
     * This method indicates if the Requisite is a Card, Color or Resource
     * @return the RequisiteType RESOURCE
     */
    @Override
    public RequisiteType getRequisiteType() {
        return RequisiteType.RESOURCE;
    }

    /**
     * Return a lite version of the effect
     *
     * @return a lite version of the effect
     */
    @Override
    public LiteRequisite liteVersion() {
        return new LiteResourceRequisite(this.resource.liteVersion());
    }
}
