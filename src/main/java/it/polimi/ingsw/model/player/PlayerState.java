package it.polimi.ingsw.model.player;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.litemodel.liteplayer.LiteState;
import it.polimi.ingsw.model.MappableToLiteVersion;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.match.markettray.RowCol;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.NormalProduction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceType;

/**
 * abstract class that need to have basics of concrete player states
 */
public abstract class PlayerState implements PlayerAction, MappableToLiteVersion {

    /**
     * the context to refer for changing the current state of the player
     */
    protected final Player context;

    /**
     * This attribute is set by a subclasses to put a message in the player state exception
     */
    private final String errorMessage;


    /**
     * the constructor take the two final attribute of the state that are the personal board and the context.
     * the player holdings are passed to not share it out of the player states and do information hiding
     * @param context is the context
     * @param eMsg is the error message
     */
    protected PlayerState(Player context, String eMsg) {
        this.context = context;
        this.errorMessage = eMsg;
    }

    /**
     * can the player do stuff?
     * @return true yes, false no
     */
    public abstract boolean doStuff();

    /**
     * Receive the input to start the turn
     */
    public void starTurn() {}

    /**
     * Give the state of the player in case of reconnection
     * @return the reconnection player state
     */
    public abstract PlayerState reconnectionState();

// ----------------------------------------------------------------
// ----- player state action as default launch an exception -------
// ----------------------------------------------------------------


    /**
     * Use the market tray
     * @param rc enum to identify if I am pushing row or col
     * @param index the index of the row or column of the tray
     * @return the result of the operation
     */
    @Override
    public Packet useMarketTray(RowCol rc, int index) {
        return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, errorMessage);
    }

    /**
     * This method allows the player to select which Resources to get when he activates two LeaderCards with the same
     * SpecialAbility that converts white marbles in resources
     * @param conversionsIndex the index of chosen tray's marble to color
     * @param marbleIndex the index of the marble conversions available
     */
    @Override
    public Packet paintMarbleInTray(int conversionsIndex, int marbleIndex) {
        return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, errorMessage);
    }

    /**
     * Player asks to buy the first card of the deck in position passed as parameter
     * @param row the row of the card required
     * @param col the column of the card required
     * @param destination the slot where put the dev card slot
     * @return true if there where no issue, false instead
     */
    @Override
    public Packet buyDevCard(LevelDevCard row, ColorDevCard col, DevCardSlot destination) {
        return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, errorMessage);
    }

    /**
     * This method takes the resources from the Depots and the Strongbox to
     * activate the productions and insert the Resources obtained into the Strongbox
     * @return the result of the operation
     */
    @Override
    public Packet activateProductions() {
        return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, errorMessage);
    }

    /**
     * This method moves a resource from a depot to a production
     * @param from the source of the resource to move
     * @param dest the destination of the resource to move
     * @param loot the resource to move
     * @return true if the resources are correctly moved in Production
     */
    @Override
    public Packet moveInProduction(DepotSlot from, ProductionID dest, Resource loot) {
        return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, errorMessage);
    }

    /**
     * This method set the normal production of an unknown production
     * @param id the id of the unknown production
     * @param normalProduction the input new normal production
     * @return the succeed of the operation
     */
    @Override
    public Packet setNormalProduction(ProductionID id, NormalProduction normalProduction)  {
        return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, errorMessage);
    }

    /**
     * This method allows the player to move Resources between Depots
     * @param from depot from which withdraw resource
     * @param to depot where insert withdrawn resource
     * @param loot is the Resource to move
     */
    @Override
    public Packet moveBetweenDepot(DepotSlot from, DepotSlot to, Resource loot) {
        return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, errorMessage);
    }

    /**
     * This method activates the special ability of the LeaderCard
     * @param leaderId the string that identify the leader card
     */
    @Override
    public Packet activateLeaderCard(String leaderId) {
        return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, errorMessage);
    }

    /**
     * This method removes a LeaderCard from the player
     * @param leaderId the string that identify the leader card to be discarded
     */
    @Override
    public Packet discardLeader(String leaderId) {
        return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, errorMessage);
    }

    /**
     * The Player ends its turn
     * @return true if success, false otherwise
     */
    @Override
    public Packet endThisTurn() {
        return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, errorMessage);
    }

    /**
     * Set a chosen resource attribute in player
     * @param slot the Depot where the Resource is taken from
     * @param chosen the resource chosen
     */
    @Override
    public Packet chooseResource(DepotSlot slot, ResourceType chosen) {
        return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, errorMessage);
    }

    /**
     * Create a lite version of the class and serialize it in json
     *
     * @return the json representation of the lite version of the class
     */
    @Override
    public abstract LiteState liteVersion();

}
