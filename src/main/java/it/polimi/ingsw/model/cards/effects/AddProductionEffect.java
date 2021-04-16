package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.model.exceptions.game.LorenzoMovesException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.player.PlayerReactEffect;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.Production;

/**
 * This class is a part of the strategy pattern, it implements the interface Effect.
 */
public class AddProductionEffect extends Effect{

    public AddProductionEffect() {}

    /**
     * This is the constructor of the class. It needs the Production that will added to the PersonalBoard.
     * @param prod that will be available to the player once activated.
     */
    @JsonCreator
    public AddProductionEffect(@JsonProperty("prod") Production prod) {
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
    public void use(PlayerReactEffect p) throws LorenzoMovesException {
        p.addProduction(this.prod);
    }
}
