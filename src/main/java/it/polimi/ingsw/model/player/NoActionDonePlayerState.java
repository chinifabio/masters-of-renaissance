package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.exceptions.PlayerStateException;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;
import it.polimi.ingsw.model.exceptions.card.MissingCardException;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.exceptions.requisite.LootTypeException;
import it.polimi.ingsw.model.exceptions.requisite.NoRequisiteException;
import it.polimi.ingsw.model.exceptions.tray.OutOfBoundMarketTrayException;
import it.polimi.ingsw.model.exceptions.tray.UnpaintableMarbleException;
import it.polimi.ingsw.model.exceptions.warehouse.NegativeResourcesDepotException;
import it.polimi.ingsw.model.exceptions.warehouse.UnobtainableResourceException;
import it.polimi.ingsw.model.exceptions.warehouse.WrongDepotException;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalNormalProduction;
import it.polimi.ingsw.model.exceptions.warehouse.production.UnknownUnspecifiedException;
import it.polimi.ingsw.model.match.markettray.RowCol;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.NormalProduction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.model.resource.Resource;

/**
 * This class is the State where the Player doesn't execute his MainAction yet
 */
public class NoActionDonePlayerState extends PlayerState {
    /**
     * the constructor take the two final attribute of the state that are the personal board and the context.
     * the player holdings are passed to not share it out of the player states and do information hiding
     *
     * @param context        the context
     */
    protected NoActionDonePlayerState(Player context) {
        super(context, "");
    }

    /**
     * can the player do stuff?
     * @return true yes, false no
     */
    @Override
    public boolean doStuff() {
        return true;
    }

    /**
     * This method starts the turn of the player
     * @throws PlayerStateException if the Player can't do this action
     */
    @Override
    public void startTurn() throws PlayerStateException {
        this.context.setState(new NotHisTurnPlayerState(this.context));
    }

// -------------------------- PLAYER ACTION IMPLEMENTATIONS ---------------------------------



    /**
     * Uses the Market Tray
     * @param rc    enum to identify if I am pushing row or col
     * @param index the index of the row or column of the tray
     * @return true if the MarketTray is correctly used
     * @throws UnobtainableResourceException if the Player can't obtain that Resources
     * @throws OutOfBoundMarketTrayException if the MarketTray is out of bound
     */
    @Override
    public boolean useMarketTray(RowCol rc, int index) throws UnobtainableResourceException, OutOfBoundMarketTrayException {
        try {
            this.context.match.useMarketTray(rc, index);
        } catch (EndGameException | WrongDepotException e) {
            this.context.match.startEndGameLogic();
        }
        this.context.setState(new MainActionDonePlayerState(this.context));
        return true;
    }

    /**
     * This method allows the player to select which Resources to get when he activates two LeaderCards with the same
     * SpecialAbility that converts white marbles in resources
     * @param conversionsIndex the index of the marble conversions available
     * @param marbleIndex      the index of chosen tray's marble to color
     * @throws UnpaintableMarbleException if the Marble can't be painted
     */
    @Override
    public void paintMarbleInTray(int conversionsIndex, int marbleIndex) throws UnpaintableMarbleException {
        this.context.match.paintMarbleInTray(this.context.marbleConversions.get(conversionsIndex).copy(), marbleIndex);
    }

    /**
     *  player ask to buy the first card of the deck in position passed as parameter
     * @param row         the row of the card required
     * @param col         the column of the card required
     * @param destination the slot where put the dev card slot
     * @return true if there where no issue, false instead
     * @throws NoRequisiteException if the Card doesn't have requisite
     * @throws PlayerStateException if the Player can't do this action
     * @throws EmptyDeckException if the Deck is empty
     * @throws LootTypeException if this attribute cannot be obtained from this Requisite
     */
    @Override
    public boolean buyDevCard(LevelDevCard row, ColorDevCard col, DevCardSlot destination) throws NoRequisiteException, PlayerStateException, EmptyDeckException, LootTypeException {
        this.context.slotDestination = destination;
        boolean res = this.context.match.buyDevCard(row, col);
        if (res) this.context.setState(new MainActionDonePlayerState(this.context));
        return res;
    }

    /**
     * This method takes the resources from the Depots and the Strongbox to
     * activate the productions and insert the Resources obtained into the Strongbox
     * @return the result of the operation
     * @throws UnobtainableResourceException if the Resource can't be obtained by the Player
     */
    @Override
    public boolean activateProductions() throws UnobtainableResourceException {
        try {
            this.context.personalBoard.activateProductions();
        } catch (EndGameException | WrongDepotException e) {
            this.context.match.startEndGameLogic();
        }
        this.context.setState(new MainActionDonePlayerState(this.context));
        return true;
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
            return this.context.personalBoard.setNormalProduction(id, normalProduction);
        } catch (IllegalNormalProduction illegalNormalProduction) {
            return false;
            // todo error to player handler
        }
    }

    /**
     * This method moves a resource from a depot to a production
     * @param from the source of the resource to move
     * @param dest the destination of the resource to move
     * @param loot the resource to move
     * @return the succeed of the operation
     * @throws UnknownUnspecifiedException if the Production is unspecified
     * @throws NegativeResourcesDepotException if the Depot doesn't have enough resources
     * @throws WrongDepotException if the Player can't use the Depot
     */
    @Override
    public boolean moveInProduction(DepotSlot from, ProductionID dest, Resource loot) throws UnknownUnspecifiedException, NegativeResourcesDepotException, WrongDepotException {
        return this.context.personalBoard.moveInProduction(from, dest, loot);
    }

    /**
     * This method allows the player to move Resources between Depots
     * @param from depot from which withdraw resource
     * @param to   depot where insert withdrawn resource
     * @param loot resource to move
     * @throws UnobtainableResourceException if the Player can't obtain the Resource
     * @throws WrongDepotException if the Depot can't be used
     * @throws NegativeResourcesDepotException if the Depot doesn't have enough resources
     */
    @Override
    public void moveBetweenDepot(DepotSlot from, DepotSlot to, Resource loot) throws UnobtainableResourceException, WrongDepotException, NegativeResourcesDepotException {
        this.context.personalBoard.moveResourceDepot(from, to, loot);
    }

    /**
     * This method activates the special ability of the LeaderCard
     * @param leaderId the string that identify the leader card
     * @throws MissingCardException if the Card isn't in the Deck
     * @throws PlayerStateException if the Player can't do this action
     * @throws EmptyDeckException if the Deck is empty
     * @throws LootTypeException if the attribute cannot be obtained from this requisite
     * @throws WrongDepotException if the Depot can't be used
     */
    @Override
    public void activateLeaderCard(String leaderId) throws MissingCardException, PlayerStateException, EmptyDeckException, LootTypeException, WrongDepotException {
        this.context.personalBoard.activateLeaderCard(leaderId);
    }

    /**
     * This method removes a LeaderCard from the player
     * @param leaderId the string that identify the leader card to be discarded
     * @throws PlayerStateException if the Player can't do this action
     * @throws EmptyDeckException if the Deck is empty
     * @throws MissingCardException if the Card isn't in the Deck
     */
    @Override
    public void discardLeader(String leaderId) throws PlayerStateException, EmptyDeckException, MissingCardException {
        this.context.personalBoard.discardLeaderCard(leaderId);
        this.context.personalBoard.moveFaithMarker(1, this.context.match);
    }

    /**
     * The player ends its turn
     * @return true if success, false otherwise
     * @throws PlayerStateException if the Player can't do this action
     * @throws WrongDepotException if the Depot cannot be used
     */
    @Override
    public boolean endThisTurn() throws PlayerStateException, WrongDepotException {
        this.context.personalBoard.flushBufferDepot(this.context.match);
        this.context.setState(new NotHisTurnPlayerState(this.context));
        return this.context.match.endMyTurn();
    }
}
