package it.polimi.ingsw.model.player;

import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.match.markettray.RowCol;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.NormalProduction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceType;

/**
 * This interface indicates the action that the Player can do
 */
public interface PlayerAction {

    /**
     * Use the market tray
     * @param rc enum to identify if I am pushing row or col
     * @param index the index of the row or column of the tray
     * @return the result of the operation
     * @throws OutOfBoundMarketTrayException if the MarketTray is out of bound
     * @throws PlayerStateException if the Player can't do this action
     * @throws UnobtainableResourceException if the Player can't obtain that Resource
     */
    Packet useMarketTray(RowCol rc, int index);

    /**
     * This method allows the player to select which Resources to get when he activates two LeaderCards with the same
     * SpecialAbility that converts white marbles in resources
     * @param conversionsIndex the index of chosen tray's marble to color
     * @param marbleIndex the index of the marble conversions available
     * @throws UnpaintableMarbleException if the Marble can't be painted
     * @throws PlayerStateException if the Player can't do this action
     */
    Packet paintMarbleInTray(int conversionsIndex, int marbleIndex);

    /**
     * player ask to buy the first card of the deck in position passed as parameter
     * @param row the row of the card required
     * @param col the column of the card required
     * @param destination the slot where put the dev card slot
     * @return true if there where no issue, false instead
     * @throws NoRequisiteException if the Card has no requisite
     * @throws PlayerStateException if the Player can't do this action
     * @throws EmptyDeckException if the Deck is empty
     * @throws LootTypeException if the attribute cannot be obtained from this Requisite
     */
    Packet buyDevCard(LevelDevCard row, ColorDevCard col, DevCardSlot destination);

    /**
     * This method takes the resources from the Depots and the Strongbox to
     * activate the productions and insert the Resources obtained into the Strongbox
     * @return the result of the operation
     * @throws PlayerStateException if the Player can't do this action
     * @throws UnobtainableResourceException if the Player can't obtain the Resource
     * @throws WrongPointsException if the Player can't obtain the FaithPoints
     */
    Packet activateProductions();

    /**
     * This method moves a resource from a depot to a production
     * @param from the source of the resource to move
     * @param dest the destination of the resource to move
     * @param loot the resource to move
     * @return  true if the resources are correctly moved in Production
     * @throws UnknownUnspecifiedException if the Production is unspecified
     * @throws NegativeResourcesDepotException if the Depot hasn't enough resources
     * @throws PlayerStateException if the Player can't do this action
     * @throws WrongDepotException if the Depot cannot be used
     */
    Packet moveInProduction(DepotSlot from, ProductionID dest, Resource loot);

    /**
     * This method set the normal production of an unknown production
     * @param id the id of the unknown production
     * @param normalProduction the input new normal production
     * @return the succeed of the operation
     * @throws PlayerStateException if the Player can't do this action
     */
    Packet setNormalProduction(ProductionID id, NormalProduction normalProduction);

    /**
     * This method allows the player to move Resources between Depots
     * @param from depot from which withdraw resource
     * @param to depot where insert withdrawn resource
     * @param loot is the Resource to move
     * @throws NegativeResourcesDepotException if the Depot hasn't enough resources
     * @throws PlayerStateException if the Player can't do this action
     * @throws UnobtainableResourceException if the Resources cannot be obtained by the Player
     * @throws WrongPointsException if the Player can't obtain the FaithPoint
     * @throws WrongDepotException if the Depot cannot be used
     */
    Packet moveBetweenDepot(DepotSlot from, DepotSlot to, Resource loot);

    /**
     * This method activates the special ability of the LeaderCard
     * @param leaderId the string that identify the leader card
     * @throws MissingCardException if the card isn't in the Deck
     * @throws PlayerStateException if the Player can't do this action
     * @throws EmptyDeckException if the Deck is empty
     * @throws LootTypeException if the attribute cannot be obtained from this Requisite
     * @throws WrongDepotException if the Depot cannot be used
     */
    Packet activateLeaderCard(String leaderId);

    /**
     * This method removes a LeaderCard from the player
     * @param leaderId the string that identify the leader card to be discarded
     * @throws PlayerStateException if the Player can't do this action
     * @throws EmptyDeckException if the Deck is empty
     * @throws MissingCardException if the card isn't in the Deck
     */
    Packet discardLeader(String leaderId);

    /**
     * set a chosen resource attribute in player
     * @param slot the Depot where the Resource is taken from
     * @param chosen the resource chosen
     * @throws PlayerStateException if the Player can't do this action
     * @throws WrongDepotException if the Depot cannot be used
     */
    Packet chooseResource(DepotSlot slot, ResourceType chosen);

    /**
     * The player ends his turn
     * @return true if success, false otherwise
     * @throws PlayerStateException if the Player can't do this action
     * @throws WrongDepotException if the Depot can't be used
     */
    Packet endThisTurn();

    /**
     * Create a game with the passed number of player
     * @param number the number of player of the match to be create
     * @return the succeed of the operation
     */
    Packet setPlayerNumber(int number);
}
