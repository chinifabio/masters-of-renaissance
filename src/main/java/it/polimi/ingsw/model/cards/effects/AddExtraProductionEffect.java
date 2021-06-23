package it.polimi.ingsw.model.cards.effects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.litecards.liteeffect.LiteAddProductionEffect;
import it.polimi.ingsw.litemodel.litecards.liteeffect.LiteEffect;
import it.polimi.ingsw.model.exceptions.ExtraProductionException;
import it.polimi.ingsw.model.player.PlayableCardReaction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.Production;

/**
 * This class is a part of the strategy pattern, it implements the interface Effect.
 */
public class AddExtraProductionEffect extends Effect{

    /**
     * This is the constructor of the class. It needs the Production that will added to the PersonalBoard.
     * @param prod that will be available to the player once activated.
     */
    @JsonCreator
    public AddExtraProductionEffect(@JsonProperty("prod") Production prod) {
        this.prod = prod;
    }

    /**
     * This attribute is the production of the Card.
     */
    private final Production prod;


    /**
     * This method is activated by a LeaderCard or a DevCard and it adds a possible production.
     * @param p the player that is getting a new production.
     */
    @Override
    public void use(CardReaction p) {
        ((PlayableCardReaction) p).addExtraProduction(this.prod);
    }

    /**
     * Return a lite version of the effect
     *
     * @return a lite version of the effect
     */
    @Override
    public LiteEffect liteVersion() {
        return new LiteAddProductionEffect(this.prod.liteVersion());
    }
}