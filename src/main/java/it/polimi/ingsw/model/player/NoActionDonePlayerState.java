package it.polimi.ingsw.model.player;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.litemodel.liteplayer.LiteState;
import it.polimi.ingsw.litemodel.liteplayer.NoActionDone;
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
     * Give the state of the player in case of reconnection
     *
     * @return the reconnection player state
     */
    @Override
    public PlayerState reconnectionState() {
        return new NotHisTurnPlayerState(this.context);
    }

// -------------------------- PLAYER ACTION IMPLEMENTATIONS ---------------------------------



    /**
     * Uses the Market Tray
     * @param rc    enum to identify if I am pushing row or col
     * @param index the index of the row or column of the tray
     * @return true if the MarketTray is correctly used
     */
    @Override
    public Packet useMarketTray(RowCol rc, int index) {
        try {
            // using market tray
            this.context.match.useMarketTray(rc, index);

        } catch (EndGameException e) {

            this.context.match.startEndGameLogic();                                      // stop the game when the last player end his turn
            this.context.setState(new CountingPointsPlayerState(this.context));                     // set the player state to counting point so he can't do nothing more
            return new Packet(HeaderTypes.END_TURN, ChannelTypes.PLAYER_ACTIONS, e.getMessage());   // send the result

        } catch (Exception e) {
            return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, e.getMessage());
        }

        this.context.setState(new MainActionDonePlayerState(this.context));
        return new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "Used market tray successfully");
    }

    /**
     * This method allows the player to select which Resources to get when he activates two LeaderCards with the same
     * SpecialAbility that converts white marbles in resources
     * @param conversionsIndex the index of the marble conversions available
     * @param marbleIndex      the index of chosen tray's marble to color
     */
    @Override
    public Packet paintMarbleInTray(int conversionsIndex, int marbleIndex) {
        try {
            this.context.match.paintMarbleInTray(this.context.marbleConversions.get(conversionsIndex).copy(), marbleIndex);
        } catch (UnpaintableMarbleException e) {
            return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, e.getMessage());
        }

        return new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "Marble painted successfully");
    }

    /**
     *  player ask to buy the first card of the deck in position passed as parameter
     * @param row         the row of the card required
     * @param col         the column of the card required
     * @param destination the slot where put the dev card slot
     * @return true if there where no issue, false instead
     */
    @Override
    public Packet buyDevCard(LevelDevCard row, ColorDevCard col, DevCardSlot destination) {
        this.context.slotDestination = destination;
        boolean res;
        try {
            res = this.context.match.buyDevCard(row, col);
        } catch (Exception e) {
            return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, e.getMessage());
        }

        if (res) {
            this.context.setState(new MainActionDonePlayerState(this.context));
        }
        return res ?
                new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "You bought the develop card requested"):
                new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, "You have no requisite to buy the card");
    }

    /**
     * This method takes the resources from the Depots and the Strongbox to
     * activate the productions and insert the Resources obtained into the Strongbox
     * @return the result of the operation
     */
    @Override
    public Packet activateProductions() {
        try {
            this.context.personalBoard.activateProductions();
        } catch (EndGameException e) {

            this.context.match.startEndGameLogic();                                      // stop the game when the last player end his turn
            this.context.setState(new CountingPointsPlayerState(this.context));                     // set the player state to counting point so he can't do nothing more
            return new Packet(HeaderTypes.END_TURN, ChannelTypes.PLAYER_ACTIONS, e.getMessage());   // send the result

        } catch (Exception e) {
            return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, e.getMessage());
        }

        this.context.setState(new MainActionDonePlayerState(this.context));
        return new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "You activated the production");
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

        return new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "You have normalized the " + id.name().toLowerCase() + " production");
    }

    /**
     * This method moves a resource from a depot to a production
     * @param from the source of the resource to move
     * @param dest the destination of the resource to move
     * @param loot the resource to move
     * @return the succeed of the operation
     */
    @Override
    public Packet moveInProduction(DepotSlot from, ProductionID dest, Resource loot) {
        try {
            this.context.personalBoard.moveInProduction(from, dest, loot);
        } catch (Exception e) {
            return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, e.getMessage());
        }

        return new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "Resources moved in production");
    }

    /**
     * This method allows the player to move Resources between Depots
     * @param from depot from which withdraw resource
     * @param to   depot where insert withdrawn resource
     * @param loot resource to move
     */
    @Override
    public Packet moveBetweenDepot(DepotSlot from, DepotSlot to, Resource loot) {
        try {
            this.context.personalBoard.moveResourceDepot(from, to, loot);
        } catch (Exception e) {
            return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, e.getMessage());
        }

        return new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "Resource moved successfully");
    }

    /**
     * This method activates the special ability of the LeaderCard
     * @param leaderId the string that identify the leader card
     */
    @Override
    public Packet activateLeaderCard(String leaderId) {
        try {
            return this.context.personalBoard.activateLeaderCard(leaderId) ?
                    new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "You have activate "+leaderId):
                    new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, "You have no requisite to activate the leader");
        } catch (Exception e) {
            return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, e.getMessage());
        }
    }

    /**
     * This method removes a LeaderCard from the player
     * @param leaderId the string that identify the leader card to be discarded
     */
    @Override
    public Packet discardLeader(String leaderId) {
        try {
            this.context.personalBoard.discardLeaderCard(leaderId);
        } catch (Exception e) {
            return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, e.getMessage());
        }

        try {
            this.context.personalBoard.moveFaithMarker(1, this.context.match);
        } catch (EndGameException e) {

            this.context.match.startEndGameLogic();                                      // stop the game when the last player end his turn
            this.context.setState(new CountingPointsPlayerState(this.context));                     // set the player state to counting point so he can't do nothing more
            return new Packet(HeaderTypes.END_TURN, ChannelTypes.PLAYER_ACTIONS, e.getMessage());   // send the result

        }

        return new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "You discarded " + leaderId);
    }

    /**
     * The player ends its turn
     * @return true if success, false otherwise
     */
    @Override
    public Packet endThisTurn() {
        this.context.personalBoard.flushBufferDepot(this.context.match);
        this.context.setState(new NotHisTurnPlayerState(this.context));
        this.context.match.turnDone();
        return new Packet(HeaderTypes.END_TURN, ChannelTypes.PLAYER_ACTIONS, "Your turn is ended");
    }

    /**
     * Create a lite version of the class and serialize it in json
     *
     * @return the json representation of the lite version of the class
     */
    @Override
    public LiteState liteVersion() {
        return new NoActionDone();
    }
}
