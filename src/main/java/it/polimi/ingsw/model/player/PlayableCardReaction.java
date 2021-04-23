package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.cards.effects.CardReaction;
import it.polimi.ingsw.model.exceptions.ExtraProductionException;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.exceptions.warehouse.UnobtainableResourceException;
import it.polimi.ingsw.model.exceptions.ExtraDiscountException;
import it.polimi.ingsw.model.exceptions.warehouse.WrongDepotException;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.Marble;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.Production;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.Depot;
import it.polimi.ingsw.model.resource.Resource;

/**
 * This interface includes all the methods that can be called to modify the Player when an effect ca
 */
public interface PlayableCardReaction extends CardReaction {
    /**
     * This method adds a Production to the list of available productions
     */
    void addProduction(Production newProd);

    /**
     * This method adds an extra Production to the list of available productions
     */
    void addExtraProduction(Production prod);

    /**
     * This method adds an extra Depot in the Warehouse
     * @param depot new depot to be added to Warehouse depots
     */
    void addDepot(Depot depot);

    /**
     * This method gives a discount to the player when buying DevCards
     */
    void addDiscount(Resource discount);

    /**
     * This method allow adding a marble conversion to the player
     * @param conversion the resource type to transform white marbles
     */
    void addMarbleConversion(Marble conversion);

    /**
     * This method insert the Resources obtained from the Market to the Depots
     * @param obt the resource
     */
    void obtainResource(Resource obt) throws WrongDepotException;

    /**
     * This method moves the FaithMarker of the player when he gets FaithPoint
     * @param amount how many cells the marker moves
     */
    void moveFaithMarker(int amount);
}
