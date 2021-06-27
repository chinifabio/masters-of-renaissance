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
     */
    void useMarketTray(RowCol rc, int index);

    /**
     * This method allows the player to select which Resources to get when he activates two LeaderCards with the same
     * SpecialAbility that converts white marbles in resources
     * @param conversionsIndex the index of chosen tray's marble to color
     * @param marbleIndex the index of the marble conversions available
     */
    void paintMarbleInTray(int conversionsIndex, int marbleIndex);

    /**
     * Player confirms the buy of a devcard
     * @param row the row of the card required
     * @param col the column of the card required
     * @param destination the slot where put the dev card slot
     */
    void buyDevCard(LevelDevCard row, ColorDevCard col, DevCardSlot destination);

    /**
     * Player asks to buy a devcard
     */
    void buyCard();

    /**
     * This method takes the resources from the Depots and the Strongbox to
     * activate the productions and insert the Resources obtained into the Strongbox
     */
    void activateProductions();

    /**
     * This method moves a resource from a depot to a production
     * @param from the source of the resource to move
     * @param dest the destination of the resource to move
     * @param loot the resource to move
     */
    void moveInProduction(DepotSlot from, ProductionID dest, Resource loot);

    /**
     * This method set the normal production of an unknown production
     * @param id the id of the unknown production
     * @param normalProduction the input new normal production
     */
    void setNormalProduction(ProductionID id, NormalProduction normalProduction);

    /**
     * This method allows the player to move Resources between Depots
     * @param from depot from which withdraw resource
     * @param to depot where insert withdrawn resource
     * @param loot is the Resource to move
     */
    void moveBetweenDepot(DepotSlot from, DepotSlot to, Resource loot);

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
     * set a chosen resource attribute in player
     * @param slot the Depot where the Resource is taken from
     * @param chosen the resource chosen
     */
    void chooseResource(DepotSlot slot, ResourceType chosen);

    /**
     * The player ends his turn
     */
    void endThisTurn();

    /**
     * Used during the buydevcard/production phase to return to the initial warehouse state.
     */
    void rollBack();

    /**
     * Player asks to use productions
     */
    void production();

    /**
     * Cheat commands used to make test faster
     */
    void resourceCheat();

    /**
     * Cheat commands used to make test faster
     */
    void fpCheat(int num);
}
