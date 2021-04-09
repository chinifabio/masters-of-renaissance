package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.match.markettray.MarkerMarble.Marble;
import it.polimi.ingsw.model.player.personalBoard.Production;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.Depot;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceType;

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
    void addDiscount(Resource discount);

    /**
     * This method allow adding a marble conversion to the player
     * @param fromWhite the resource type to transform white marbles
     */
    void addMarbleConversion(ResourceType fromWhite);

    /**
     * This method insert the Resources obtained from the Market to the Depots
     * @param resource the resource
     */
    void obtainResource(Resource resource);

    /**
     * This method insert the Resources obtained from the Market to the Depots
     * @param marble the resource in form of marble
     */
    void obtainResource(Marble marble);

    /**
     * This method moves the FaithMarker of the player when he gets FaithPoint
     * @param amount how many cells the marker moves
     */
    void moveFaithMarker(int amount);

    /**
     * This method moves the Lorenzo's marker
     * @param amount how many cells the marker moves
     */
    void moveLorenzo(int amount);

    /**
     * This method shuffle the solo action token
     */
    void shuffleToken();
}
