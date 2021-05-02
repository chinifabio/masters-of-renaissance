package it.polimi.ingsw.model.requisite;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.exceptions.requisite.LootTypeException;
import it.polimi.ingsw.model.resource.ResourceType;

/**
 * This class is used to store a group of resources that can be DevCards or Resources.
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "ResourceReq", value = ResourceRequisite.class),
        @JsonSubTypes.Type(name = "CardReq", value = CardRequisite.class),
        @JsonSubTypes.Type(name = "ColorCardReq", value = ColorCardRequisite.class)
})
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

    /**
     * This method indicates the type of Requisite
     * @return the type of requisite, so CARD or RESOURCE
     */
    RequisiteType getRequisiteType();
}
