package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.exceptions.PlayerStateException;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalNormalProduction;
import it.polimi.ingsw.model.match.markettray.RowCol;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.NormalProduction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.model.resource.Resource;

public class PendingMatchStartPlayerState extends PlayerState {

    /**
     * the constructor take the two final attribute of the state that are the personal board and the context.
     * the player holdings are passed to not share it out of the player states and do information hiding
     *
     * @param context        the context
     */
    public PendingMatchStartPlayerState(Player context) {
        super(context);
    }

    /**
     * can the player do staff?
     *
     * @return true yes, false no
     */
    @Override
    public boolean doStaff() {
        return false;
    }

    /**
     * this method start the turn of the player
     */
    @Override
    public void startTurn() {
        this.context.setState(new LeaderSelectionPlayerState(this.context));
    }

    // ----------------- PLAYER ACTION IMPLEMENTATIONS ---------------------------------


    /**
     * Use the market tray
     *
     * @param rc    enum to identify if I am pushing row or col
     * @param index the index of the row or column of the tray
     * @return the result of the operation
     */
    @Override
    public boolean useMarketTray(RowCol rc, int index) throws PlayerStateException {
        throw new PlayerStateException("match not started yet");
    }

    /**
     * This method allows the player to select which Resources to get when he activates two LeaderCards with the same
     * SpecialAbility that converts white marbles in resources
     *
     * @param conversionsIndex the index of the marble conversions available
     * @param marbleIndex      the index of chosen tray's marble to color
     */
    @Override
    public void paintMarbleInTray(int conversionsIndex, int marbleIndex) throws PlayerStateException {
        throw new PlayerStateException("match not started yet");
    }

    /**
     * player ask to buy the first card of the deck in position passed as parameter
     *
     * @param row         the row of the card required
     * @param col         the column of the card required
     * @param destination the slot where put the dev card slot
     * @return true if there where no issue, false instead
     */
    @Override
    public boolean buyDevCard(LevelDevCard row, ColorDevCard col, DevCardSlot destination) throws PlayerStateException {
        throw new PlayerStateException("match not started yet");
    }

    /**
     * This method takes the resources from the Depots and the Strongbox to
     * activate the productions and insert the Resources obtained into the Strongbox
     *
     * @return the result of the operation
     */
    @Override
    public boolean activateProductions() throws PlayerStateException {
        throw new PlayerStateException("match not started yet");
    }

    /**
     * This method set the normal production of an unknown production
     *
     * @param normalProduction the input new normal production
     * @param id the id of the unknown production
     * @return the succeed of the operation
     */
    @Override
    public boolean setNormalProduction(ProductionID id, NormalProduction normalProduction) throws PlayerStateException {
        throw new PlayerStateException("match not started yet");
    }

    /**
     * This method moves a resource from a depot to a production
     *  @param from the source of the resource to move
     * @param dest the destination of the resource to move
     * @param loot the resource to move
     * @return
     */
    @Override
    public boolean moveInProduction(DepotSlot from, ProductionID dest, Resource loot) throws PlayerStateException {
        throw new PlayerStateException("match not started yet");
    }

    /**
     * This method allows the player to move Resources between Depots
     *
     * @param from depot from which withdraw resource
     * @param to   depot where insert withdrawn resource
     * @param loot resource to move
     */
    @Override
    public void moveBetweenDepot(DepotSlot from, DepotSlot to, Resource loot) throws PlayerStateException {
        throw new PlayerStateException("match not started yet");
    }

    /**
     * This method activates the special ability of the LeaderCard
     *
     * @param leaderId the string that identify the leader card
     */
    @Override
    public void activateLeaderCard(String leaderId) throws PlayerStateException {
        throw new PlayerStateException("match not started yet");
    }

    /**
     * This method removes a LeaderCard from the player
     *
     * @param leaderId the string that identify the leader card to be discarded
     */
    @Override
    public void discardLeader(String leaderId) throws PlayerStateException {
        throw new PlayerStateException("match not started yet");
    }

    /**
     * the player ends its turn
     *
     * @return true if success, false otherwise
     */
    @Override
    public boolean endThisTurn() throws PlayerStateException {
        throw new PlayerStateException("match not started yet");
    }

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "PendingStart state";
    }
}