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
     */
    Packet useMarketTray(RowCol rc, int index);

    /**
     * This method allows the player to select which Resources to get when he activates two LeaderCards with the same
     * SpecialAbility that converts white marbles in resources
     * @param conversionsIndex the index of chosen tray's marble to color
     * @param marbleIndex the index of the marble conversions available
     */
    Packet paintMarbleInTray(int conversionsIndex, int marbleIndex);

    /**
     * Player confirms the buy of a devcard
     * @param row the row of the card required
     * @param col the column of the card required
     * @param destination the slot where put the dev card slot
     * @return true if there where no issue, false instead
     */
    Packet buyDevCard(LevelDevCard row, ColorDevCard col, DevCardSlot destination);

    /**
     * Player asks to buy a devcard
     * @return the result of the operation
     */
    Packet buyCard();

    /**
     * This method takes the resources from the Depots and the Strongbox to
     * activate the productions and insert the Resources obtained into the Strongbox
     * @return the result of the operation
     */
    Packet activateProductions();

    /**
     * This method moves a resource from a depot to a production
     * @param from the source of the resource to move
     * @param dest the destination of the resource to move
     * @param loot the resource to move
     * @return  true if the resources are correctly moved in Production
     */
    Packet moveInProduction(DepotSlot from, ProductionID dest, Resource loot);

    /**
     * This method set the normal production of an unknown production
     * @param id the id of the unknown production
     * @param normalProduction the input new normal production
     * @return the succeed of the operation
     */
    Packet setNormalProduction(ProductionID id, NormalProduction normalProduction);

    /**
     * This method allows the player to move Resources between Depots
     * @param from depot from which withdraw resource
     * @param to depot where insert withdrawn resource
     * @param loot is the Resource to move
     */
    Packet moveBetweenDepot(DepotSlot from, DepotSlot to, Resource loot);

    /**
     * This method activates the special ability of the LeaderCard
     * @param leaderId the string that identify the leader card
     */
    Packet activateLeaderCard(String leaderId);

    /**
     * This method removes a LeaderCard from the player
     * @param leaderId the string that identify the leader card to be discarded
     */
    Packet discardLeader(String leaderId);

    /**
     * set a chosen resource attribute in player
     * @param slot the Depot where the Resource is taken from
     * @param chosen the resource chosen
     */
    Packet chooseResource(DepotSlot slot, ResourceType chosen);

    /**
     * The player ends his turn
     * @return true if success, false otherwise
     */
    Packet endThisTurn();

    /**
     * Used during the buydevcard/production phase to return to the initial warehouse state.
     * @return a warning packet
     */
    Packet rollBack();

    /**
     * Player asks to use productions
     * @return the result of the operation
     */
    Packet production();

    /**
     * Cheat commands used to make test faster
     * @return the result of the operation
     */
    Packet resourceCheat();

    /**
     * Cheat commands used to make test faster
     * @return the result of the operation
     */
    Packet fpCheat(int num);
}
