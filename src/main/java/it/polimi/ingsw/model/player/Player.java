package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.exceptions.ExtraDiscountException;
import it.polimi.ingsw.model.exceptions.PlayerStateException;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;
import it.polimi.ingsw.model.exceptions.card.MissingCardException;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.exceptions.warehouse.production.UnknownUnspecifiedException;
import it.polimi.ingsw.model.exceptions.requisite.NoRequisiteException;
import it.polimi.ingsw.model.exceptions.tray.OutOfBoundMarketTrayException;
import it.polimi.ingsw.model.exceptions.tray.UnpaintableMarbleException;
import it.polimi.ingsw.model.exceptions.warehouse.NegativeResourcesDepotException;
import it.polimi.ingsw.model.exceptions.warehouse.UnobtainableResourceException;
import it.polimi.ingsw.model.exceptions.warehouse.WrongDepotException;
import it.polimi.ingsw.model.exceptions.warehouse.WrongPointsException;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
import it.polimi.ingsw.model.match.PlayerToMatch;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.Marble;
import it.polimi.ingsw.model.match.markettray.RowCol;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;
import it.polimi.ingsw.model.player.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.FaithTrack;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.Production;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.VaticanSpace;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.Depot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.requisite.Requisite;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;

import java.util.*;

/**
 * This class identify the Player
 */
public class Player implements PlayerAction, PlayerReactEffect, MatchToPlayer {
    /**
     * represent the player state and contains the implementation of the method of PlayerModifier
     */
    private PlayerState playerState;
    
    /**
     * this is the personal board associated to the player
     */
    final PersonalBoard personalBoard;

    /**
     * This attribute is the nickname of the Player, it is unique for each player in the match
     */
    final String nickname;

    /**
     * This attribute is the list of all the Discounts that has the player, that he can use when use the MarketTray
     */
    final List<Resource> marketDiscount;

    /**
     * This attribute contains all the player's white marble conversion
     */
    final List<Marble> marbleConversions;

    /**
     * the match instance that player uses to talk with the match instance
     */
    final PlayerToMatch match;

    /**
     * destination where put a dev card obtained
     */
    DevCardSlot slotDestination;

    /**
     * This method create a player by nickname and saving the match reference
     * @param nickname that identify the player
     * @param matchReference used to reference the match which i am playing
     */
    public Player(String nickname, PlayerToMatch matchReference) throws IllegalTypeInProduction {

        this.nickname = nickname;
        this.personalBoard = new PersonalBoard(this);
        this.match = matchReference;

        this.marbleConversions = new ArrayList<>();
        this.marketDiscount = new ArrayList<>();

        this.playerState = new PendingMatchStartPlayerState(this);
    }

    /**
     * This method set the nickname of the Player
     * @return the nickname of the player
     */
    public String getNickname(){
        return this.nickname;
    }

    /**
     * This method return the possibility of a player to do stuff
     * @return true if positive answer, false instead
     */
    public boolean canDoStuff() {
        return this.playerState.doStaff();
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Player) return this.nickname.equals(((Player) obj).getNickname());
        else return false;
    }

    /**
     * receive a state and set it as current state
     *
     * @param newPlayerState the new state of the context
     */
    public void setState(PlayerState newPlayerState) {
        this.playerState = newPlayerState;
    }

    // player react effect implementations

    /**
     * This method adds a Production to the list of available productions
     *
     * @param newProd the new production
     */
    @Override
    public void addProduction(Production newProd) {
        this.personalBoard.addProduction(newProd);
    }

    /**
     * This method adds an extra Depot in the Warehouse
     *
     * @param depot new depot to be added to Warehouse depots
     */
    @Override
    public void addDepot(Depot depot) {
        this.personalBoard.addDepot(depot);
    }

    /**
     * This method gives a discount to the player when buying DevCards
     *
     * @param discount the new discount
     */
    @Override
    public void addDiscount(Resource discount) throws ExtraDiscountException {
        if(this.marketDiscount.isEmpty() || this.marketDiscount.size() <2){
            Resource temp = ResourceBuilder.buildFromType(discount.type(), 1);
            this.marketDiscount.add(temp);
        }
        else {
            throw new ExtraDiscountException();
        }
    }

    /**
     * This method allow adding a marble conversion to the player
     *
     * @param newConversion the resource type to transform white marbles
     */
    @Override
    public void addMarbleConversion(Marble newConversion) {
        this.marbleConversions.add(newConversion);
    }

    /**
     * This method insert the Resources obtained from the Market to the Depots
     *
     * @param marble the resource in form of marble
     */
    @Override
    public void obtainResource(Marble marble) throws UnobtainableResourceException, EndGameException {
        Resource obt = marble.toResource();
        obt.onObtain(this);
        if(obt.isStorable()) this.personalBoard.obtainResource(obt);
    }

    /**
     * This method moves the FaithMarker of the player when he gets FaithPoint
     *
     * @param amount how many cells the marker moves
     */
    @Override
    public void moveFaithMarker(int amount) throws EndGameException {
        this.personalBoard.moveFaithMarker(amount, this.match);
    }

    /**
     * This method shuffle the solo action token
     */
    @Override
    public void shuffleToken(){
        //todo sistemare
    }

    /**
     * This method discard two card of the color passed in the dev setup
     *
     * @param color color of the dev card to discard
     */
    @Override
    public void discardDevCard(ColorDevCard color) {
        //todo sistemare
    }

    // player action implementations

    /**
     * Use the market tray
     *  @param rc enum to identify if I am pushing row or col
     * @param index the index of the row or column of the tray
     * @return true
     */
    @Override
    public boolean useMarketTray(RowCol rc, int index) throws OutOfBoundMarketTrayException, UnobtainableResourceException, EndGameException {
        try {
            this.playerState.useMarketTray(rc, index);
            return true;
        } catch (PlayerStateException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method allows the player to select which Resources to get when he activates two LeaderCards with the same
     * SpecialAbility that converts white marbles in resources
     * @param marbleIndex the index of chosen tray's marble to color
     * @param conversionsIndex the index of the marble conversions available
     */
    @Override
    public void paintMarbleInTray(int conversionsIndex, int marbleIndex) throws UnpaintableMarbleException, PlayerStateException {
        this.playerState.paintMarbleInTray(conversionsIndex, marbleIndex);
    }

    /**
     * player ask to buy the first card of the deck in position passed as parameter
     *
     * @param row the row of the card required
     * @param col the column of the card required
     * @return true if there where no issue, false instead
     */
    @Override
    public boolean buyDevCard(LevelDevCard row, ColorDevCard col, DevCardSlot destination) throws NoRequisiteException, PlayerStateException, EmptyDeckException {
        return this.playerState.buyDevCard(row, col, destination);
    }

    /**
     * This method takes the resources from the Depots and the Strongbox to
     * activate the productions and insert the Resources obtained into the Strongbox
     * @return true if success
     */
    @Override
    public boolean activateProductions() throws PlayerStateException, UnobtainableResourceException, WrongPointsException, EndGameException {
        this.playerState.activateProductions();
        return true;
    }

    /**
     * This method moves a resource from a depot to a production
     * @param from the source of the resource to move
     * @param dest the destination of the resource to move
     * @param loot the resource to move
     */
    @Override
    public void moveInProduction(DepotSlot from, ProductionID dest, Resource loot) throws PlayerStateException, UnknownUnspecifiedException, NegativeResourcesDepotException {
        this.playerState.moveInProduction(from, dest, loot);
    }

    /**
     * This method allows the player to move Resources between Depots
     *
     * @param from depot from which withdraw resource
     * @param to   depot where insert withdrawn resource
     * @param loot the resource to move
     */
    @Override
    public void moveBetweenDepot(DepotSlot from, DepotSlot to, Resource loot) throws WrongDepotException, NegativeResourcesDepotException, UnobtainableResourceException, WrongPointsException, EndGameException, PlayerStateException {
        this.playerState.moveBetweenDepot(from, to, loot);
    }

    /**
     * This method activates the special ability of the LeaderCard
     *
     * @param leaderId the string that identify the leader card
     */
    @Override
    public void activateLeaderCard(String leaderId) throws MissingCardException, PlayerStateException, EndGameException {
        this.playerState.activateLeaderCard(leaderId);
    }

    /**
     * This method removes a LeaderCard from the player
     *
     * @param leaderId the string that identify the leader card to be discarded
     */
    @Override
    public void discardLeader(String leaderId) throws PlayerStateException, EndGameException, EmptyDeckException, MissingCardException {
        this.playerState.discardLeader(leaderId);
    }

    /**
     * the player ends its turn
     * @return true if success, false otherwise
     */
    @Override
    public boolean endThisTurn() throws PlayerStateException {
        return this.playerState.endThisTurn();
    }

    // match to player implementation

    /**
     * This method adds a LeaderCard to the Players' PersonalBoard
     *
     * @param leaderCard the leader card to assign to the hand of the player
     */
    @Override
    public void addLeader(LeaderCard leaderCard) {
        this.personalBoard.addLeaderCard(leaderCard);
    }

    /**
     * this method check if the player has requisite
     * If it return true then the warehouse has eliminate the requisites yet
     * If it return false then the player has not the requisite;
     *
     * @param req the requisite
     * @return boolean indicating the succeed of the method
     */
    @Override
    public boolean hasRequisite(List<Requisite> req, LevelDevCard row, ColorDevCard col) {
        // TODO use checkDevCard before removing resources and adding it. use discount here!
        return false;
    }

    /**
     * This method adds a DevCard to the player's personal board, using resources taken from the Warehouse
     *
     * @param newDevCard the dev card received that need to be stored in the personal board
     */
    @Override
    public void receiveDevCard(DevCard newDevCard) {
        this.personalBoard.addDevCard(this.slotDestination, newDevCard);
    }

    /**
     * This method flips the PopeTile when the Player is in a Vatican Space or passed the relative PopeSpace
     *
     * @param popeTile the tile to check if it need to be flipped
     */
    @Override
    public void flipPopeTile(VaticanSpace popeTile) {
        this.personalBoard.flipPopeTile(popeTile);
    }

    /**
     * starts the turn of the player;
     *
     * @return true if success, false otherwise
     */
    @Override
    public boolean startHisTurn() throws PlayerStateException {
        this.playerState.startTurn();
        System.out.println(this.nickname + ": Turn started, waiting for action");
        return true;
    }

    // only for test
    public FaithTrack getFT_forTest() {
        return this.personalBoard.getFT_forTest();
    }

    public void discardLeader_test(){
        try {
            this.discardLeader(this.personalBoard.viewLeaderCard().peekFirstCard().getCardID());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
