package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.cards.DevCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.exceptions.moves.MainActionDoneException;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.Marble;
import it.polimi.ingsw.model.player.personalBoard.Production;
import it.polimi.ingsw.model.player.personalBoard.ProductionID;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.PopeTile;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.Depot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceType;

//TODO aggiungere i throw alla javadoc di tutte le classi [questa, player e tutti gli stati]

/**
 * This interface includes all the methods that can be called to modify the Player status and attributes
 */
public interface PlayerModifier {
    /**
     * This method adds a LeaderCard to the Players' PersonalBoard
     * @param leaderCard the leader card to assign to the hand of the player
     */
    void addLeader(LeaderCard leaderCard);

    /**
     * This method activates the special ability of the LeaderCard
     * @param leaderId the string that identify the leader card
     */
    void activateLeaderCard(String leaderId);

    /**
     * This method removes a LeaderCard from the player
     * @param leaderId the string that identify the leader card to be discarded
     */
    void discardLeader(String leaderId);

    /**
     * This method adds a Production to the list of available productions
     */
    void addProduction(Production newProd);

    /**
     * This method select the productions that will be activated by the player
     * @param prod the production to set as selected
     */
    void selectProduction(ProductionID prod);

    /**
     * This method takes the resources from the Depots and the Strongbox to
     * activate the productions and insert the Resources obtained into the Strongbox
     * @param prod this identify the production that is required to be activated
     */
    void activateProduction(ProductionID prod);

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
     * This method adds a DevCard to the player's personal board, using resources taken from the Warehouse
     * @param newDevCard the dev card received that need to be stored in the personal board
     */
    void receiveDevCard(DevCard newDevCard);

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
     * This method allows the player to move Resources between Depots
     * @param from depot from which withdraw resource
     * @param to depot where insert withdrawn resource
     * @param loot the resource to move
     */
    void moveResourceDepot(DepotSlot from, DepotSlot to, Resource loot);

    /**
     * This method flips the PopeTile when the Player is in a Vatican Space or passed the relative PopeSpace
     * @param popeTile the tile to check if it need to be flipped
     */
    void flipPopeTile(PopeTile popeTile);

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
     * This method allows the player to select which Resources to get when he activates two LeaderCards with the same
     * SpecialAbility that converts white marbles in resources
     * @param resource resource to set as default as white marble conversion
     */
    void selectWhiteConversion(Resource resource);

    /**
     * This method allow adding a marble conversion to the player
     * @param fromWhite the resource type to transform white marbles
     */
    void addMarbleConversion(ResourceType fromWhite);
}
