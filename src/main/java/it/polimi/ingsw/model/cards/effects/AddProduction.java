package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerModifier;
import it.polimi.ingsw.model.player.personalBoard.Production;

import java.util.List;

/**
 * This class is a part of the strategy pattern, it implements the interface Effect.
 */
public class AddProduction implements Effect{

    /**
     * This is the constructor of the class. It needs the Production that will added to the PersonalBoard.
     * @param prod that will be available to the player once activated.
     */
    public AddProduction(Production prod) {
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
    public void use(PlayerModifier p) {
        p.addProduction(this.prod);
    }
}
