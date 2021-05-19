package it.polimi.ingsw.model.cards.effects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.litecards.liteeffect.LiteAddDiscountEffect;
import it.polimi.ingsw.litemodel.litecards.liteeffect.LiteEffect;
import it.polimi.ingsw.model.player.PlayableCardReaction;
import it.polimi.ingsw.model.resource.*;
import it.polimi.ingsw.model.resource.ResourceType;

/**
 * This class is a part of the strategy pattern, it implements the interface Effect.
 */
public class AddDiscountEffect extends Effect{

    /**
     * This is the constructor of the class. It needs the ResourceType that will be discounted.
     * @param t type that will be discounted.
     */
    @JsonCreator
    public AddDiscountEffect(@JsonProperty("resource") ResourceType t) {
        this.resource = t;
    }

    /**
     * This attribute saves the discounted resource of the Card.
     */
    private final ResourceType resource;

    /**
     * This method is activated by a LeaderCard, it adds a discount to a specific resource when buying a DevCard.
     * @param p the player
     */
    @Override
    public void use(CardReaction p) {
        ((PlayableCardReaction) p).addDiscount(this.resource);
    }

    /**
     * Return a lite version of the effect
     *
     * @return a lite version of the effect
     */
    @Override
    public LiteEffect liteVersion() {
        return new LiteAddDiscountEffect(this.resource);
    }
}