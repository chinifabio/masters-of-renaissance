package it.polimi.ingsw.model.cards.effects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.exceptions.ExtraProductionException;
import it.polimi.ingsw.model.player.PlayableCardReaction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.Production;

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
    private Production prod;


    /**
     * This method is activated by a LeaderCard or a DevCard and it adds a possible production.
     * @param p the player that is getting a new production.
     */
    @Override
    public void use(CardReaction p) {
        ((PlayableCardReaction) p).addExtraProduction(this.prod);
    }
}