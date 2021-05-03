package it.polimi.ingsw.model.player;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.exceptions.tray.UnpaintableMarbleException;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalNormalProduction;
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
    public void startTurn() {
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
    public Packet useMarketTray(RowCol rc, int index) {
        try {
            // using market tray
            this.context.model.getMatch().useMarketTray(rc, index);

        } catch (EndGameException e) {

            this.context.model.getMatch().startEndGameLogic();                                      // stop the game when the last player end his turn
            this.context.setState(new CountingPointsPlayerState(this.context));                     // set the player state to counting point so he can't do nothing more
            return new Packet(HeaderTypes.END_TURN, ChannelTypes.PLAYER_ACTIONS, e.getMessage());   // send the result

        } catch (Exception e) {
            return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, e.getMessage());
        }

        this.context.setState(new MainActionDonePlayerState(this.context));
        return new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "operation done successfully");
    }

    /**
     * This method allows the player to select which Resources to get when he activates two LeaderCards with the same
     * SpecialAbility that converts white marbles in resources
     * @param conversionsIndex the index of the marble conversions available
     * @param marbleIndex      the index of chosen tray's marble to color
     * @throws UnpaintableMarbleException if the Marble can't be painted
     */
    @Override
    public Packet paintMarbleInTray(int conversionsIndex, int marbleIndex) {
        try {
            this.context.model.getMatch().paintMarbleInTray(this.context.marbleConversions.get(conversionsIndex).copy(), marbleIndex);
        } catch (UnpaintableMarbleException e) {
            return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, e.getMessage());
        }

        return new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "operation done successfully");
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
    public Packet buyDevCard(LevelDevCard row, ColorDevCard col, DevCardSlot destination) {
        this.context.slotDestination = destination;
        boolean res = false;
        try {
            res = this.context.model.getMatch().buyDevCard(row, col);
        } catch (Exception e) {
            return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, e.getMessage());
        }

        if (res) this.context.setState(new MainActionDonePlayerState(this.context));
        return res ?
                new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, "you have no requisite to buy this card"):
                new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "operation done successfully");
    }

    /**
     * This method takes the resources from the Depots and the Strongbox to
     * activate the productions and insert the Resources obtained into the Strongbox
     * @return the result of the operation
     * @throws UnobtainableResourceException if the Resource can't be obtained by the Player
     */
    @Override
    public Packet activateProductions() {
        try {
            this.context.personalBoard.activateProductions();
        } catch (EndGameException e) {

            this.context.model.getMatch().startEndGameLogic();                                      // stop the game when the last player end his turn
            this.context.setState(new CountingPointsPlayerState(this.context));                     // set the player state to counting point so he can't do nothing more
            return new Packet(HeaderTypes.END_TURN, ChannelTypes.PLAYER_ACTIONS, e.getMessage());   // send the result

        } catch (Exception e) {
            return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, e.getMessage());
        }

        this.context.setState(new MainActionDonePlayerState(this.context));
        return new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "operation done successfully");
    }

    /**
     * This method set the normal production of an unknown production
     *
     * @param normalProduction the input new normal production
     * @param id the id of the unknown production
     * @return the succeed of the operation
     */
    @Override
    public Packet setNormalProduction(ProductionID id, NormalProduction normalProduction) {
        try {
            this.context.personalBoard.setNormalProduction(id, normalProduction);
        } catch (IllegalNormalProduction e) {
            return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, e.getMessage());
        }

        return new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "operation done successfully");
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
    public Packet moveInProduction(DepotSlot from, ProductionID dest, Resource loot) {
        try {
            this.context.personalBoard.moveInProduction(from, dest, loot);
        } catch (Exception e) {
            return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, e.getMessage());
        }

        return new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "operation done successfully");
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
    public Packet moveBetweenDepot(DepotSlot from, DepotSlot to, Resource loot) {
        try {
            this.context.personalBoard.moveResourceDepot(from, to, loot);
        } catch (Exception e) {
            return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, e.getMessage());
        }

        return new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "operation done successfully");
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
    public Packet activateLeaderCard(String leaderId) {
        try {
            this.context.personalBoard.activateLeaderCard(leaderId);
        } catch (Exception e) {
            return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, e.getMessage());
        }

        return new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "operation done successfully");
    }

    /**
     * This method removes a LeaderCard from the player
     * @param leaderId the string that identify the leader card to be discarded
     * @throws PlayerStateException if the Player can't do this action
     * @throws EmptyDeckException if the Deck is empty
     * @throws MissingCardException if the Card isn't in the Deck
     */
    @Override
    public Packet discardLeader(String leaderId) {
        try {
            this.context.personalBoard.discardLeaderCard(leaderId);
        } catch (Exception e) {
            return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, e.getMessage());
        }

        try {
            this.context.personalBoard.moveFaithMarker(1, this.context.model.getMatch());
        } catch (EndGameException e) {

            this.context.model.getMatch().startEndGameLogic();                                      // stop the game when the last player end his turn
            this.context.setState(new CountingPointsPlayerState(this.context));                     // set the player state to counting point so he can't do nothing more
            return new Packet(HeaderTypes.END_TURN, ChannelTypes.PLAYER_ACTIONS, e.getMessage());   // send the result

        }

        return new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "operation done successfully");
    }

    /**
     * The player ends its turn
     * @return true if success, false otherwise
     * @throws PlayerStateException if the Player can't do this action
     * @throws WrongDepotException if the Depot cannot be used
     */
    @Override
    public Packet endThisTurn() {
        this.context.personalBoard.flushBufferDepot(this.context.model.getMatch());
        this.context.setState(new NotHisTurnPlayerState(this.context));
        this.context.model.getMatch().endMyTurn();
        return new Packet(HeaderTypes.END_TURN, ChannelTypes.PLAYER_ACTIONS, "your turn is ended");
    }
}
