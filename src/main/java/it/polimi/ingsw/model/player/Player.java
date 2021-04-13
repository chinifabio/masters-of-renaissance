package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.match.PlayerToMatch;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.Marble;
import it.polimi.ingsw.model.match.markettray.RowCol;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;
import it.polimi.ingsw.model.player.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.Production;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.VaticanSpace;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.Depot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.player.state.Context;
import it.polimi.ingsw.model.player.state.NotHisTurnState;
import it.polimi.ingsw.model.player.state.State;
import it.polimi.ingsw.model.requisite.Requisite;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceType;

import java.util.*;

/**
 * This class identify the Player
 */
public class Player implements Context, PlayerAction, PlayerReactEffect, MatchToPlayer {
    /**
     * represent the player state and contains the implementation of the method of PlayerModifier
     */
    private State playerState;

    /**
     * this is the personal board associated to the player
     */
    private final PersonalBoard personalBoard;

    /**
     * This attribute is the nickname of the Player, it is unique for each player in the match
     */
    private final String nickname;

    /**
     * This attribute is the list of all the Discounts that has the player, that he can use when use the MarketTray
     */
    private final List<Optional<Resource>> marketDiscount;

    /**
     * This attribute contains all the player's white marble conversion
     */
    private final LinkedList<ResourceType> marbleConversions;

    /**
     * the match instance that player uses to talk with the match instance
     */
    protected PlayerToMatch match;

    /**
     * destination where put a dev card obtained
     */
    private DevCardSlot slotDestination;

    /**
     * This method create a player by nickname and saving the match reference
     * @param nickname that identify the player
     * @param matchReference used to reference the match which i am playing
     */
    public Player(String nickname, PlayerToMatch matchReference){
        this.nickname = nickname;
        this.personalBoard = new PersonalBoard();
        this.playerState = new NotHisTurnState(this);
        this.marbleConversions = new LinkedList<>();
        this.marbleConversions.add(ResourceType.UNKNOWN);
        this.marketDiscount = new ArrayList<>();
        this.match = matchReference;
    }

    /**
     * This method set the nickname of the Player
     * @return the nickname of the player
     */
    public String getNickname(){
        return nickname;
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
        if (obj instanceof Player) return nickname.equals(((Player) obj).getNickname());
        else return false;
    }

    // player state implementation

    /**
     * receive a state and set it as current state
     *
     * @param newState the new state of the context
     */
    @Override
    public void setState(State newState) {
        this.playerState = newState;
    }

    /**
     * only for test, return the current player state
     */
    public State getPlayerState(){
        return playerState;
    }

    // player react effect implementations

    /**
     * This method adds a Production to the list of available productions
     *
     * @param newProd the new production
     */
    @Override
    public void addProduction(Production newProd) {
        personalBoard.addProduction(newProd);
    }

    /**
     * This method adds an extra Depot in the Warehouse
     *
     * @param depot new depot to be added to Warehouse depots
     */
    @Override
    public void addDepot(Depot depot) {
        personalBoard.addDepot(depot);
    }

    /**
     * This method gives a discount to the player when buying DevCards
     *
     * @param discount the new discount
     */
    @Override
    public void addDiscount(Resource discount) {
        // TODO da implementare i discount
    }

    /**
     * This method allow adding a marble conversion to the player
     *
     * @param newConversion the resource type to transform white marbles
     */
    @Override
    public void addMarbleConversion(ResourceType newConversion) {
        this.marbleConversions.add(newConversion);
    }

    /**
     * This method insert the Resources obtained from the Market to the Depots
     *
     * @param resource the resource
     */
    @Override
    public void obtainResource(Resource resource) {
        try {
            resource.onObtain(this);
        } catch (UnobtainableResourceException e) {
            // todo dire al model dell'errore
        }
        if(resource.isStorable()) this.personalBoard.obtainResource(resource);
            // TODO improve this mechanism
    }

    /**
     * This method insert the Resources obtained from the Market to the Depots
     *
     * @param marble the resource in form of marble
     */
    @Override
    public void obtainResource(Marble marble) {
        this.obtainResource(marble.toResource(this.marbleConversions.element()));
    }

    /**
     * This method moves the FaithMarker of the player when he gets FaithPoint
     *
     * @param amount how many cells the marker moves
     */
    @Override
    public void moveFaithMarker(int amount) {
        this.personalBoard.moveFaithMarker(amount);
    }

    /**
     * This method shuffle the solo action token
     */
    @Override
    public void shuffleToken(){
        //todo dire che qualcosa non va
    }

    /**
     * This method discard two card of the color passed in the dev setup
     *
     * @param color color of the dev card to discard
     */
    @Override
    public void discardDevCard(ColorDevCard color) {
        //todo dire che qualcosa non va
    }

    // player action implementations


    /**
     * This method return a representation of the tray as a list of marble
     *
     * @return the tray representation as list
     */
    @Override
    public List<Marble> viewMarketTray() {
        return this.match.viewMarketTray();
    }

    /**
     * Use the market tray
     *  @param rc enum to identify if I am pushing row or col
     * @param index the index of the row or column of the tray
     * @return true
     */
    @Override
    public boolean useMarketTray(RowCol rc, int index) {
        try{
            playerState.doMainActionInput();
        } catch (IllegalMovesException e) {
            return false;
        }
        // TODO aggiungere una reazione alle eccezioni
        try {
            this.match.useMarketTray(rc, index);
        } catch (Exception e) {
            System.out.println("market tray exception");
        }
        return true;
    }

    /**
     * This method allows the player to select which Resources to get when he activates two LeaderCards with the same
     * SpecialAbility that converts white marbles in resources
     *
     * @param resource resource to set as default as white marble conversion
     */
    @Override
    public void selectWhiteConversion(Resource resource) {
        this.marbleConversions.remove(resource.type());
        this.marbleConversions.addFirst(resource.type());
    }

    /**
     * return a view of the dev setup. It is shown only the first card of each decks
     *
     * @return the list representation of the dev setup
     */
    @Override
    public List<DevCard> viewDevSetup() {
        return this.match.viewDevSetup();
    }

    /**
     * player ask to buy the first card of the deck in position passed as parameter
     *
     * @param row the row of the card required
     * @param col the column of the card required
     * @return true if there where no issue, false instead
     */
    @Override
    public boolean buyDevCard(LevelDevCard row, ColorDevCard col, DevCardSlot destination) {
        try {
            playerState.doMainActionInput();
        } catch (IllegalMovesException e) {
            return false;
        }
        this.slotDestination = destination;
        System.out.println(nickname + ": Asking to Match to buy devCard");
        match.buyDevCard(row, col);
        return true;
    }

    /**
     * return a list of all available production for the player
     *
     * @return the list of production
     */
    @Override
    public List<Production> possibleProduction() {
        return this.personalBoard.possibleProduction();
    }

    /**
     * This method takes the resources from the Depots and the Strongbox to
     * activate the productions and insert the Resources obtained into the Strongbox
     * @return true if success
     */
    @Override
    public boolean activateProductions() {
        try {
            playerState.doMainActionInput();
        } catch (IllegalMovesException e) {
            return false;
        }
        this.personalBoard.activateProductions();
        return true;
    }

    /**
     * This method select the productions that will be activated by the player
     *
     * @param prod the production to set as selected
     */
    @Override
    public void selectProduction(ProductionID prod) {
        this.personalBoard.selectProduction(prod);
    }

    /**
     * This method allows the player to move Resources between Depots
     *
     * @param from depot from which withdraw resource
     * @param to   depot where insert withdrawn resource
     * @param loot the resource to move
     */
    @Override
    public void moveBetweenDepot(DepotSlot from, DepotSlot to, Resource loot) throws WrongDepotException, NegativeResourcesDepotException {
        this.personalBoard.moveResourceDepot(from, to, loot);
    }

    /**
     * This method activates the special ability of the LeaderCard
     *
     * @param leaderId the string that identify the leader card
     */
    @Override
    public void activateLeaderCard(String leaderId) {
        this.personalBoard.activateLeaderCard(leaderId);
    }

    /**
     * This method removes a LeaderCard from the player
     *
     * @param leaderId the string that identify the leader card to be discarded
     */
    @Override
    public void discardLeader(String leaderId) {
        this.personalBoard.discardLeaderCard(leaderId);
    }

    /**
     * Return a deck of leader card that a player has
     *
     * @return deck of leader card
     */
    @Override
    public Deck<LeaderCard> viewLeader() {
        return this.personalBoard.viewLeaderCard();
    }

    /**
     * return a list of all available player's resource. It doesn't mattare the depot they are stored
     *
     * @return a list of resource
     */
    @Override
    public List<Resource> viewResource() {
        return this.personalBoard.viewResources();
    }

    /**
     * return a map of the top develop card placed in the player board decks
     *
     * @return a map of devCardSlot - DevCard
     */
    @Override
    public Map<DevCardSlot, DevCard> viewDevCards() {
        return this.personalBoard.viewDevCards();
    }

    /**
     * return the faith marker position of the player
     *
     * @return faith marker position of the player
     */
    @Override
    public int viewFaithMarkerPosition() {
        return personalBoard.FaithMarkerPosition();
    }

    /**
     * this method tell if the player can so stuff
     *
     * @return true if it can, false otherwise
     */
    @Override
    public boolean canDoStuff() {
        return playerState.doStaff();
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
     * this method check if the player has requisite.
     * If it return true then the warehouse has eliminate the requisites yet
     * If it return false then the player has not the requisite;
     *
     * @param req the requisite
     * @return boolean indicating the succeed of the method
     */
    @Override
    public boolean hasRequisite(Requisite req) throws NoRequisiteException {
        // TODO gino
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
    public boolean startHisTurn() {
        try {
            playerState.startTurnInput();
        } catch (IllegalMovesException e) {
            return false;
        }
        System.out.println(nickname + ": Turn started, waiting for action");
        // TODO mandare indietro che io ho iniziato il turno
        return true;
    }

    /**
     * the player ends its turn
     * @return true if success, false otherwise
     */
    @Override
    public boolean endThisTurn() {
        try {
            playerState.endTurnInput();
        } catch (IllegalMovesException e) {
            return false;
        }
        System.out.println(nickname + ": Turn ended");
        return match.endMyTurn();
    }
}
