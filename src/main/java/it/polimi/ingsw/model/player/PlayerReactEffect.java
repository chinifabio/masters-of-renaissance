package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.exceptions.warehouse.UnobtainableResourceException;
import it.polimi.ingsw.model.exceptions.ExtraDiscountException;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.Marble;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.Production;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.Depot;
import it.polimi.ingsw.model.resource.Resource;

/**
 * This interface includes all the methods that can be called to modify the Player when an effect ca
 */
public interface PlayerReactEffect {
    /**
     * This method adds a Production to the list of available productions
     */
    void addProduction(Production newProd);

    /**
     * This method adds an extra Depot in the Warehouse
     * @param depot new depot to be added to Warehouse depots
     */
    void addDepot(Depot depot);

    /**
     * This method gives a discount to the player when buying DevCards
     */
    void addDiscount(Resource discount) throws ExtraDiscountException;

    /**
     * This method allow adding a marble conversion to the player
     * @param conversion the resource type to transform white marbles
     */
    void addMarbleConversion(Marble conversion);

    /**
     * This method insert the Resources obtained from the Market to the Depots
     * @param marble the resource in form of marble
     */
    void obtainResource(Marble marble) throws UnobtainableResourceException, EndGameException;

    /**
     * This method moves the FaithMarker of the player when he gets FaithPoint
     * @param amount how many cells the marker moves
     */
    void moveFaithMarker(int amount) throws EndGameException;

    /**
     * This method shuffle the solo action token
     */
    void shuffleToken();

    /**
     * This method discard two card of the color passed in the dev setup
     * @param color color of the dev card to discard
     */
    void discardDevCard(ColorDevCard color);
}
