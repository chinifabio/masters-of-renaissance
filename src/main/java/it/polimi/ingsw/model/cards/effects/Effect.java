package it.polimi.ingsw.model.cards.effects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.model.exceptions.card.AlreadyInDeckException;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.player.PlayableCardReaction;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "DestroyCards", value = DestroyCardsEffect.class),
        @JsonSubTypes.Type(name = "MoveTwo", value = MoveTwoEffect.class),
        @JsonSubTypes.Type(name = "ShuffleMoveOne", value = ShuffleMoveOneEffect.class),
        @JsonSubTypes.Type(name = "AddProduction", value = AddProductionEffect.class),
        @JsonSubTypes.Type(name = "AddDepot", value = AddDepotEffect.class),
        @JsonSubTypes.Type(name = "AddDiscount", value = AddDiscountEffect.class),
        @JsonSubTypes.Type(name = "WhiteMarble", value = WhiteMarbleEffect.class)
})
public abstract class Effect {

    /**
     * This method activates the effect of cards
     * @param p is the interface to react a card effect
     */
    public abstract void use(CardReaction p);
}
