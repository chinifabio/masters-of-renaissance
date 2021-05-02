package it.polimi.ingsw.model.player;

import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.exceptions.ExtraProductionException;
import it.polimi.ingsw.model.exceptions.PlayerStateException;
import it.polimi.ingsw.model.exceptions.card.AlreadyInDeckException;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;
import it.polimi.ingsw.model.exceptions.card.MissingCardException;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.exceptions.requisite.LootTypeException;
import it.polimi.ingsw.model.exceptions.warehouse.*;
import it.polimi.ingsw.model.exceptions.warehouse.production.UnknownUnspecifiedException;
import it.polimi.ingsw.model.exceptions.requisite.NoRequisiteException;
import it.polimi.ingsw.model.exceptions.tray.OutOfBoundMarketTrayException;
import it.polimi.ingsw.model.exceptions.tray.UnpaintableMarbleException;
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
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.Depot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.requisite.Requisite;
import it.polimi.ingsw.model.requisite.ResourceRequisite;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.util.Pair;

import java.util.*;

/**
 * This class identifies the Player
 */
public class Player implements PlayerAction, PlayableCardReaction, MatchToPlayer {
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
     * This save the initial setup of a player when the game starts
     * the first integer refer to the amount of resource to choose
     * second one integer refer to the amount of fait point player initially receive
     */
    public Pair<Integer> initialSetup;

    /**
     * This method create a player by nickname and saving the match reference
     * @param nickname identifies the player
     * @param matchReference used to reference the match which i am playing
     * @throws IllegalTypeInProduction if the Basic Production of the PersonalBoard has IllegalResources
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
        return this.playerState.doStuff();
    }

    /**
     * This method start the end game logic. The player state is set to counting points so
     * you can call only the countPoint method on a player with this state
     */
    public void setCountingPoints() {
        this.playerState = new CountingPointsPlayerState(this);
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
            // todo exception to player handler
        }
    }

    /**
     * This method adds an extra Depot in the Warehouse
     * @param depot new depot to be added to Warehouse depots
     */
    @Override
    public void addDepot(Depot depot) {
        try {
            this.personalBoard.addDepot(depot);
        } catch (ExtraDepotsException e) {
            // todo exception to player handler
        }
    }

    /**
     * This method gives a discount to the player when buying DevCards
     * @param discount the new discount
     */
    @Override
    public void addDiscount(Resource discount) {
        Resource temp = ResourceBuilder.buildFromType(discount.type(), 1);
        this.marketDiscount.add(temp);
    }

    /**
     * This method allow adding a marble conversion to the player
     * @param newConversion the resource type to transform white marbles
     */
    @Override
    public void addMarbleConversion(Marble newConversion) {
        this.marbleConversions.add(newConversion);
    }

    /**
     * This method insert the Resources obtained from the Market to the Depots
     * @param slot is the Depot where the Resources will be inserted
     * @param obt the resource obtained
     * @return the succeed of the operation
     * @throws WrongDepotException if the Depot can't be used
     */
    @Override
    public boolean obtainResource(DepotSlot slot, Resource obt) throws WrongDepotException {
        try {
            obt.onObtain(this);
        } catch (EndGameException e) {
            // reached the end of faith track so tells to the match to start end game logic
            this.match.startEndGameLogic();
        } catch (UnobtainableResourceException e) {
            // todo exception to player handler
        }
        if(obt.isStorable()) return this.personalBoard.insertInDepot(slot, obt);
        return false;
    }

    /**
     * This method moves the FaithMarker of the player when he gets FaithPoint
     * @param amount how many cells the marker moves
     */
    @Override
    public void moveFaithMarker(int amount) {
        this.personalBoard.moveFaithMarker(amount, this.match);
    }


    /**
     * Use the market tray
     *  @param rc enum to identify if I am pushing row or col
     * @param index the index of the row or column of the tray
     * @return true if the MarketTray is correctly used
     */
    @Override
    public boolean useMarketTray(RowCol rc, int index) throws OutOfBoundMarketTrayException, UnobtainableResourceException {
        try {
            return this.playerState.useMarketTray(rc, index);
        } catch (PlayerStateException e) {
            System.out.println(TextColors.colorText(TextColors.RED_BRIGHT, "fail using market tray"));
            return false;
        }
    }

    /**
     * This method allows the player to select which Resources to get when he activates two LeaderCards with the same
     * SpecialAbility that converts white marbles in resources
     * @param conversionsIndex the index of the marble conversions available
     * @param marbleIndex the index of chosen tray's marble to color
     * @throws UnpaintableMarbleException if the Marble can't be painted
     * @throws PlayerStateException if the Player can't do this action
     */
    @Override
    public void paintMarbleInTray(int conversionsIndex, int marbleIndex) throws UnpaintableMarbleException, PlayerStateException {
        this.playerState.paintMarbleInTray(conversionsIndex, marbleIndex);
    }

    /**
     * Player asks to buy the first card of the deck in position passed as parameter
     * @param row the row of the card required
     * @param col the column of the card required
     * @param destination the slot where put the dev card slot
     * @return true if there where no issue, false instead
     * @throws NoRequisiteException if the Card has no requisite
     * @throws EmptyDeckException if the Deck is empty
     * @throws LootTypeException if the attribute can't be obtained from this Requisite
     */
    @Override
    public boolean buyDevCard(LevelDevCard row, ColorDevCard col, DevCardSlot destination) throws NoRequisiteException, EmptyDeckException, LootTypeException {
        try {
            this.slotDestination = destination;
            return this.playerState.buyDevCard(row, col, destination);
        } catch (PlayerStateException e) {
            System.out.println(TextColors.colorText(TextColors.RED_BRIGHT, "fail buying develop card"));
            return false;
        }
    }

    /**
     * This method takes the resources from the Depots and the Strongbox to
     * activate the productions and insert the Resources obtained into the Strongbox
     * @return true if success
     * @throws UnobtainableResourceException if the Resource can't be obtained
     * @throws WrongPointsException if the Player can't obtain the points
     */
    @Override
    public boolean activateProductions() throws UnobtainableResourceException, WrongPointsException {
        try {
            return this.playerState.activateProductions();
        } catch (PlayerStateException e) {
            System.out.println(TextColors.colorText(TextColors.RED_BRIGHT, "fail activating production"));
            return false;
        }
    }

    /**
     * This method set the normal production of an unknown production
     *
     * @param normalProduction the input new normal production
     * @param id the id of the unknown production
     * @return the succeed of the operation
     */
    @Override
    public boolean setNormalProduction(ProductionID id, NormalProduction normalProduction) {
        try {
            return this.playerState.setNormalProduction(id, normalProduction);
        } catch (PlayerStateException e) {
            System.out.println(TextColors.colorText(TextColors.RED_BRIGHT, "fail setting normal production"));
            return false;
        }
    }

    /**
     * This method moves a resource from a depot to a production
     * @param from the source of the resource to move
     * @param dest the destination of the resource to move
     * @param loot the resource to move
     * @return the succeed of the operation
     * @throws UnknownUnspecifiedException if the Production is unspecified
     * @throws NegativeResourcesDepotException if the Depot hasn't enough resources
     */
    @Override
    public boolean moveInProduction(DepotSlot from, ProductionID dest, Resource loot) throws UnknownUnspecifiedException, NegativeResourcesDepotException {
        try {
            return this.playerState.moveInProduction(from, dest, loot);
        } catch (PlayerStateException | WrongDepotException e) {
            System.out.println(TextColors.colorText(TextColors.RED_BRIGHT, "fail moving resources in production"));
            return false;
        }
    }


    /**
     * This method allows the player to move Resources between Depots
     * @param from depot from which withdraw resource
     * @param to depot where insert withdrawn resource
     * @param loot the resource to move
     * @throws WrongDepotException if the Depot cannot be used
     * @throws NegativeResourcesDepotException if the Depot hasn't enough resources
     * @throws UnobtainableResourceException if the Resources cannot be obtained
     * @throws WrongPointsException if the Player cannot obtain the FaithPoint
     * @throws PlayerStateException if the Player can't do this action
     */
    @Override
    public void moveBetweenDepot(DepotSlot from, DepotSlot to, Resource loot) throws WrongDepotException, NegativeResourcesDepotException, UnobtainableResourceException, WrongPointsException, PlayerStateException {
        this.playerState.moveBetweenDepot(from, to, loot);
    }

    /**
     * This method activates the special ability of the LeaderCard
     * @param leaderId the string that identify the leader card
     * @throws MissingCardException if the Card isn't in the Deck
     * @throws PlayerStateException if the Player can't do this action
     */
    @Override
    public void activateLeaderCard(String leaderId) throws MissingCardException, PlayerStateException {
        try {
            this.playerState.activateLeaderCard(leaderId);
        } catch (EmptyDeckException | LootTypeException | WrongDepotException ignore) {
            // there is no leader card
        }
    }


    /**
     * This method removes a LeaderCard from the player
     * @param leaderId the string that identify the leader card to be discarded
     * @throws PlayerStateException if the Player cannot do this action
     * @throws EmptyDeckException if the deck is empty
     * @throws MissingCardException if the card isn't in the deck
     */
    @Override
    public void discardLeader(String leaderId) throws PlayerStateException, EmptyDeckException, MissingCardException {
        this.playerState.discardLeader(leaderId);
    }

    /**
     * The player ends its turn
     * @return true if success, false otherwise
     * @throws PlayerStateException if the Player can't do this action
     * @throws WrongDepotException if the Depot cannot be used
     */
    @Override
    public boolean endThisTurn() throws PlayerStateException, WrongDepotException {
        return this.playerState.endThisTurn();
    }

    /**
     * Set a chosen resource attribute in player
     * @param slot is the Depot where the Resource is located
     * @param chosen the resource chosen
     * @throws PlayerStateException if the Player can't do this action
     * @throws WrongDepotException if the Depot cannot be used
     */
    @Override
    public void chooseResource(DepotSlot slot, ResourceType chosen) throws PlayerStateException, WrongDepotException {
        this.playerState.chooseResource(slot, chosen);
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
            // todo error to player handler
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
    public boolean hasRequisite(List<Requisite> req, LevelDevCard row, ColorDevCard col,DevCard card) throws LootTypeException {
        if (!this.personalBoard.checkDevCard(slotDestination, card)) {
            return false;
        }
        //discount
        for (Resource resLoop : this.marketDiscount) {
            for (int j = 0; j < req.size(); j++) {
                try {
                    if (req.get(j).getType().equals(resLoop.type())) {
                        Resource tempRes = ResourceBuilder.buildFromType(req.get(j).getType(), req.get(j).getAmount() - 1);
                        ResourceRequisite tempReq = new ResourceRequisite(tempRes);
                        req.set(j,tempReq);
                    }
                } catch (LootTypeException ignored) {
                }
            }
        }
        //resource check
        List<Resource> tempBufferRes;
        tempBufferRes = this.personalBoard.viewDepotResource(DepotSlot.BUFFER);
        for (Requisite reqLoop : req) {
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
    public void receiveDevCard(DevCard newDevCard) {
        this.personalBoard.addDevCard(this.slotDestination, newDevCard, this.match);
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
     * @return true if success, false otherwise
     */
    @Override
    public boolean startHisTurn() {
        try {
            this.playerState.startTurn();
        } catch (PlayerStateException e) {
            // todo exception to player handler
        }
        return true;
    }

    /**
     * Returns a string representation of the object.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return this.nickname;
    }

    // FOR TESTING
    public FaithTrack getFT_forTest() {
        return this.personalBoard.getFT_forTest();
    }

    // FOR TESTING
    public void test_discardLeader() throws EmptyDeckException, PlayerStateException, MissingCardException {
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
}
