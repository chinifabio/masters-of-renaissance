package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.exceptions.PlayerStateException;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;
import it.polimi.ingsw.model.exceptions.card.MissingCardException;
import it.polimi.ingsw.model.exceptions.warehouse.WrongDepotException;
import it.polimi.ingsw.model.exceptions.warehouse.WrongPointsException;
import it.polimi.ingsw.model.exceptions.warehouse.production.UnknownUnspecifiedException;
import it.polimi.ingsw.model.exceptions.requisite.NoRequisiteException;
import it.polimi.ingsw.model.exceptions.tray.OutOfBoundMarketTrayException;
import it.polimi.ingsw.model.exceptions.tray.UnpaintableMarbleException;
import it.polimi.ingsw.model.exceptions.warehouse.NegativeResourcesDepotException;
import it.polimi.ingsw.model.exceptions.warehouse.UnobtainableResourceException;
import it.polimi.ingsw.model.match.markettray.RowCol;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.Resource;

public interface PlayerAction {

    /**
     * Use the market tray
     * @param rc enum to identify if I am pushing row or col
     * @param index the index of the row or column of the tray
     * @return the result of the operation
     */
    boolean useMarketTray(RowCol rc, int index) throws OutOfBoundMarketTrayException, PlayerStateException, UnobtainableResourceException;

    /**
     * This method allows the player to select which Resources to get when he activates two LeaderCards with the same
     * SpecialAbility that converts white marbles in resources
     * @param marbleIndex the index of chosen tray's marble to color
     * @param conversionsIndex the index of the marble conversions available
     */
    void paintMarbleInTray(int conversionsIndex, int marbleIndex) throws UnpaintableMarbleException, PlayerStateException;

    /**
     * player ask to buy the first card of the deck in position passed as parameter
     * @param col the column of the card required
     * @param row the row of the card required
     * @param destination the slot where put the dev card slot
     * @return true if there where no issue, false instead
     */
    boolean buyDevCard(LevelDevCard row, ColorDevCard col, DevCardSlot destination) throws NoRequisiteException, PlayerStateException, EmptyDeckException;

    /**
     * This method takes the resources from the Depots and the Strongbox to
     * activate the productions and insert the Resources obtained into the Strongbox
     * @return  the result of the operation
     */
    boolean activateProductions() throws PlayerStateException, UnobtainableResourceException, WrongPointsException;

    /**
     * This method moves a resource from a depot to a production
     * @param from the source of the resource to move
     * @param dest the destination of the resource to move
     * @param loot the resource to move
     * @return
     */
    boolean moveInProduction(DepotSlot from, ProductionID dest, Resource loot) throws UnknownUnspecifiedException, NegativeResourcesDepotException, PlayerStateException;

    /**
     * This method allows the player to move Resources between Depots
     * @param from depot from which withdraw resource
     * @param to depot where insert withdrawn resource
     */
    void moveBetweenDepot(DepotSlot from, DepotSlot to, Resource loot) throws NegativeResourcesDepotException, PlayerStateException, UnobtainableResourceException, WrongPointsException, WrongDepotException;

    /**
     * This method activates the special ability of the LeaderCard
     * @param leaderId the string that identify the leader card
     */
    void activateLeaderCard(String leaderId) throws MissingCardException, PlayerStateException, EmptyDeckException;

    /**
     * This method removes a LeaderCard from the player
     * @param leaderId the string that identify the leader card to be discarded
     */
    void discardLeader(String leaderId) throws PlayerStateException, EmptyDeckException, MissingCardException;

    /**
     * the player ends its turn
     * @return true if success, false otherwise
     */
    boolean endThisTurn() throws PlayerStateException;

}
