package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Pair;
import it.polimi.ingsw.model.VirtualView;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.exceptions.ExtraProductionException;
import it.polimi.ingsw.model.exceptions.card.AlreadyInDeckException;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.exceptions.requisite.LootTypeException;
import it.polimi.ingsw.model.exceptions.warehouse.*;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
import it.polimi.ingsw.model.match.PlayerToMatch;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.Marble;
import it.polimi.ingsw.model.match.markettray.RowCol;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;
import it.polimi.ingsw.model.player.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.FaithTrack;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.NormalProduction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.Production;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.VaticanSpace;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.requisite.Requisite;
import it.polimi.ingsw.model.requisite.ResourceRequisite;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.*;
import it.polimi.ingsw.model.resource.ResourceType;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class identifies the Player
 */
public class Player implements PlayerAction, PlayableCardReaction, MatchToPlayer {
    /**
     * This virtual view is used to notify all clients for changes in player holdings
     */
    public final VirtualView view;

    /**
     * this attribute flag the waiting state
     */
    public boolean waiting = false;

    /**
     * represent the player state and contains the implementation of the method of PlayerModifier
     */
    private PlayerState playerState;
    
    /**
     * this is the personal board associated to the player
     */
    public final PersonalBoard personalBoard;

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
     * This attribute reference the match in the model in the  server
     */
    PlayerToMatch match;

    /**
     * destination where put a dev card obtained
     */
    DevCardSlot slotDestination;

    /**
     * This save the initial setup of a player when the game starts
     * the first integer refer to the amount of resource to choose
     * second one integer refer to the amount of fait point player initially receive
     */
    protected Pair<Integer> initialSetup = new Pair<>(0,0);

    /**
     * the player state to set in case of reconnection
     */
    private PlayerState reconnectionState;

    /**
     * This method create a player by nickname and saving the match reference
     * @param nickname (type String) identifies the player
     * @throws IllegalTypeInProduction if the Basic Production of the PersonalBoard has IllegalResources
     */
    public Player(String nickname, PlayerToMatch match, VirtualView view) throws IllegalTypeInProduction, IOException {
        this.view = view;
        this.nickname = nickname;

        this.view.publish(model -> model.createPlayer(nickname));

        this.personalBoard = new PersonalBoard(this);

        this.marbleConversions = new ArrayList<>();
        this.marketDiscount = new ArrayList<>();

        this.playerState = new PendingInitPlayerState(this);

        this.match = match;
    }

    /**
     * Set the initial resource of the player in the game init
     * @param integerPair a pair of integer representing the number of resources
     */
    public void setInitialSetup(Pair<Integer> integerPair) {
        initialSetup = integerPair;
    }

    /**
     * Return the total victory points obtained by the player in the match
     * @return the victory points
     */
    public int calculateVictoryPoints() {
        return personalBoard.getTotalVictoryPoints();
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
        return this.playerState.doStuff();
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

    /**
     * This method adds a Production to the list of available productions
     *
     * @param newProd the new production
     */
    @Override
    public void addProduction(Production newProd) {
        this.personalBoard.addProduction(newProd, this.slotDestination);
    }

    /**
     * This method adds an extra Production to the list of available productions
     * @param prod is the production to add
     */
    @Override
    public void addExtraProduction(Production prod) {
        try {
            this.personalBoard.addExtraProduction(prod);
        } catch (ExtraProductionException e) {
            view.sendError(e.getMessage());
        }
    }

    /**
     * This method adds an extra Depot in the Warehouse
     * @param res new depot type to be added to Warehouse depots
     */
    @Override
    public void addDepot(ResourceType res) {
        try {
            this.personalBoard.addDepot(res);
        } catch (ExtraDepotsException e) {
            view.sendError(e.getMessage());
        }
    }

    /**
     * This method gives a discount to the player when buying DevCards
     * @param discount the new discount
     */
    @Override
    public void addDiscount(ResourceType discount) {
        Resource temp = ResourceBuilder.buildFromType(discount, 1);
        this.marketDiscount.add(temp);
        view.publish(model -> model.setDiscounts(nickname, ResourceBuilder.mapResource(marketDiscount)));
    }

    /**
     * This method allow adding a marble conversion to the player
     * @param newConversion the resource type to transform white marbles
     */
    @Override
    public void addMarbleConversion(Marble newConversion) {
        marbleConversions.add(newConversion);
        view.publish(model -> model.setConversions(nickname, marbleConversions.stream().map(Marble::color).collect(Collectors.toList())));
    }

    /**
     * This method insert the Resources obtained from the Market to the Depots
     * @param slot is the Depot where the Resources will be inserted
     * @param obt the resource obtained
     * @return the succeed of the operation
     */
    @Override
    public boolean obtainResource(DepotSlot slot, Resource obt) {
        try {
            obt.onObtain(this);
            return (obt.isStorable() && this.personalBoard.insertInDepot(slot, obt));
        }

        catch (EndGameException end) {
            match.startEndGameLogic();
            return true;
        }

        catch (Exception e) {
            view.sendError(e.getMessage());
            return false;
        }
    }

    /**
     * This method moves the FaithMarker of the player when he gets FaithPoint
     * @param amount how many cells the marker moves
     */
    @Override
    public void moveFaithMarker(int amount) {
        try {
            personalBoard.moveFaithMarker(amount, match);
        } catch (EndGameException e) {
            match.startEndGameLogic();
        }
    }


    /**
     * Use the market tray
     *  @param rc enum to identify if I am pushing row or col
     * @param index the index of the row or column of the tray
     */
    @Override
    public void useMarketTray(RowCol rc, int index) {
        playerState.useMarketTray(rc, index);
    }

    /**
     * This method allows the player to select which Resources to get when he activates two LeaderCards with the same
     * SpecialAbility that converts white marbles in resources
     * @param conversionsIndex the index of the marble conversions available
     * @param marbleIndex the index of chosen tray's marble to color
     */
    @Override
    public void paintMarbleInTray(int conversionsIndex, int marbleIndex) {
        playerState.paintMarbleInTray(conversionsIndex, marbleIndex);
    }

    /**
     * Player asks to buy the first card of the deck in position passed as parameter
     * @param row the row of the card required
     * @param col the column of the card required
     * @param destination the slot where put the dev card slot
     */
    @Override
    public void buyDevCard(LevelDevCard row, ColorDevCard col, DevCardSlot destination) {
        playerState.buyDevCard(row, col, destination);
    }

    /**
     *
     */
    @Override
    public void buyCard() {
        playerState.buyCard();
    }

    /**
     * This method takes the resources from the Depots and the Strongbox to
     * activate the productions and insert the Resources obtained into the Strongbox
     */
    @Override
    public void activateProductions() {
        playerState.activateProductions();
    }

    /**
     * This method set the normal production of an unknown production
     *
     * @param id the id of the unknown production
     * @param normalProduction the input new normal production
     */
    @Override
    public void setNormalProduction(ProductionID id, NormalProduction normalProduction) {
        playerState.setNormalProduction(id, normalProduction);
    }

    /**
     * This method moves a resource from a depot to a production
     * @param from the source of the resource to move
     * @param dest the destination of the resource to move
     * @param loot the resource to move
     */
    @Override
    public void moveInProduction(DepotSlot from, ProductionID dest, Resource loot) {
        playerState.moveInProduction(from, dest, loot);
    }


    /**
     * This method allows the player to move Resources between Depots
     * @param from depot from which withdraw resource
     * @param to depot where insert withdrawn resource
     * @param loot the resource to move
     */
    @Override
    public void moveBetweenDepot(DepotSlot from, DepotSlot to, Resource loot) {
        playerState.moveBetweenDepot(from, to, loot);
    }

    /**
     * This method activates the special ability of the LeaderCard
     * @param leaderId the string that identify the leader card
     */
    @Override
    public void activateLeaderCard(String leaderId) {
        playerState.activateLeaderCard(leaderId);
    }


    /**
     * This method removes a LeaderCard from the player
     * @param leaderId the string that identify the leader card to be discarded
     */
    @Override
    public void discardLeader(String leaderId) {
        playerState.discardLeader(leaderId);
    }

    /**
     * The player ends its turn
     */
    @Override
    public void endThisTurn() {
        playerState.endThisTurn();
    }

    /**
     * Used during the buydevcard/production phase to return to the initial warehouse state.
     */
    @Override
    public void rollBack() {
        playerState.rollBack();
    }

    /**
     * Player asks to use productions
     */
    @Override
    public void production() {
        playerState.production();
    }


    /**
     * Set a chosen resource attribute in player
     * @param slot is the Depot where the Resource is located
     * @param chosen the resource chosen
     */
    @Override
    public void chooseResource(DepotSlot slot, ResourceType chosen) {
        playerState.chooseResource(slot, chosen);
    }

    // match to player implementation

    /**
     * This method adds a LeaderCard to the Players' PersonalBoard
     *
     * @param leaderCard the leader card to assign to the hand of the player
     */
    @Override
    public void addLeader(LeaderCard leaderCard) {
        try {
            this.personalBoard.addLeaderCard(leaderCard);
        } catch (AlreadyInDeckException e) {
            view.sendError(e.getMessage());
        }
    }

    /**
     * this method check if the player has requisite
     * @param req is the list of requisite needed
     * @param row is the row of the DevSetup where the DevCard is located
     * @param col is the column of the DevSetup where the DevCard is located
     * @param card is the DevCard
     * @return true when the warehouse has eliminate the requisites yet, else the player has not the requisite;
     * @throws LootTypeException if the attribute cannot be obtained from the requisite
     */
    @Override
    public boolean hasRequisite(List<Requisite> req, LevelDevCard row, ColorDevCard col, DevCard card) throws LootTypeException {
        if (!this.personalBoard.checkDevCard(slotDestination, card)) {
            return false;
        }

        List<Requisite> discountedReq = new ArrayList<>(req.size());
        discountedReq.addAll(req);
        //discount
        for (Resource resLoop : this.marketDiscount) {
            for (int j = 0; j < discountedReq.size(); j++) {
                try {
                    if (discountedReq.get(j).getType().equals(resLoop.type())) {
                        Resource tempRes = ResourceBuilder.buildFromType(discountedReq.get(j).getType(), discountedReq.get(j).getAmount() - 1);
                        ResourceRequisite tempReq = new ResourceRequisite(tempRes);
                        discountedReq.set(j,tempReq);
                    }
                } catch (LootTypeException ignored) {
                }
            }
        }

        //resource check
        List<Resource> tempBufferRes;
        tempBufferRes = this.personalBoard.viewDepotResource(DepotSlot.DEVBUFFER);
        for (Requisite reqLoop : discountedReq) {
            for (Resource tempListResLoop : tempBufferRes) {
                if (tempListResLoop.type().equals(reqLoop.getType())) {
                    if ( !(tempListResLoop.amount() == reqLoop.getAmount()) ) return false;
                }
            }
        }

        //resource removal
        this.personalBoard.flushBufferDevCard();
        return true;
    }

    /**
     * This method adds a DevCard to the player's personal board, using resources taken from the Warehouse
     * @param newDevCard the dev card received that need to be stored in the personal board
     */
    @Override
    public void receiveDevCard(DevCard newDevCard) throws AlreadyInDeckException, EndGameException {
        this.personalBoard.addDevCard(this.slotDestination, newDevCard);
        newDevCard.useEffect(this);
    }

    /**
     * This method flips the PopeTile when the Player is in a Vatican Space or passed the relative PopeSpace
     * @param popeTile the tile to check if it need to be flipped
     */
    @Override
    public void flipPopeTile(VaticanSpace popeTile) {
        this.personalBoard.flipPopeTile(popeTile);
    }

    /**
     * starts the turn of the player;
     */
    @Override
    public void startHisTurn() {
        this.playerState.starTurn();
    }

    /**
     * Returns a string representation of the object.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return nickname;
    }

    /**
     * make the player disconnect so he skip his turn if it is his turn
     */
    public void disconnect() {
        this.reconnectionState = this.playerState.reconnectionState();
        this.playerState = new DisconnectedPlayerState(this);
    }

    public void reconnect() {
        this.playerState = this.reconnectionState;
    }

    // FOR CHEATING
    @Override
    public void resourceCheat() {
        playerState.resourceCheat();
    }

    @Override
    public void fpCheat(int fp) {
        playerState.fpCheat(fp);
    }

    // FOR TESTING
    public FaithTrack getFT_forTest() {
        return this.personalBoard.getFT_forTest();
    }

    // FOR TESTING
    public void test_discardLeader() throws EmptyDeckException {
        this.discardLeader(this.personalBoard.viewLeaderCard().peekFirstCard().getCardID());
    }

    // for testing
    public PersonalBoard test_getPB() {
        return this.personalBoard;
    }

    // for testing
    public void test_setDest(DevCardSlot slot) {
        this.slotDestination = slot;
    }

    // for testing
    public List<Resource> test_getDiscount() {
        return this.marketDiscount;
    }

    // for testing
    public List<Marble> test_getConv() {
        return this.marbleConversions;
    }

    // for testing
    public void test_endTurnNoMain() {
        this.playerState = new MainActionDonePlayerState(this);
        this.endThisTurn();
    }
}
