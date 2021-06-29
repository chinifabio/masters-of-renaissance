package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.match.markettray.RowCol;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.NormalProduction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;

/**
 * abstract class that need to have basics of concrete player states
 */
public abstract class PlayerState implements PlayerAction {

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
    public abstract PlayerState handleDisconnection();

// ----------------------------------------------------------------
// ----- player state action as default launch an exception -------
// ----------------------------------------------------------------


    /**
     * Use the market tray
     * @param rc enum to identify if I am pushing row or col
     * @param index the index of the row or column of the tray
     */
    @Override
    public void useMarketTray(RowCol rc, int index) {
        context.view.sendPlayerError(context.nickname, errorMessage);
    }

    /**
     * This method allows the player to select which Resources to get when he activates two LeaderCards with the same
     * SpecialAbility that converts white marbles in resources
     * @param conversionsIndex the index of chosen tray's marble to color
     * @param marbleIndex the index of the marble conversions available
     */
    @Override
    public void paintMarbleInTray(int conversionsIndex, int marbleIndex) {
        context.view.sendPlayerError(context.nickname, errorMessage);
    }

    /**
     * Player confirms the buy of a dev card
     * @param row the row of the card required
     * @param col the column of the card required
     * @param destination the slot where put the dev card slot
     */
    @Override
    public void buyDevCard(LevelDevCard row, ColorDevCard col, DevCardSlot destination) {
        context.view.sendPlayerError(context.nickname, errorMessage);
    }

    /**
     * Player asks to buy a dev card
     */
    @Override
    public void buyCard() {
        context.view.sendPlayerError(context.nickname, errorMessage);
    }

    /**
     * This method takes the resources from the Depots and the Strongbox to
     * activate the productions and insert the Resources obtained into the Strongbox
     */
    @Override
    public void activateProductions() {
        context.view.sendPlayerError(context.nickname, errorMessage);
    }

    /**
     * This method moves a resource from a depot to a production
     * @param from the source of the resource to move
     * @param dest the destination of the resource to move
     * @param loot the resource to move
     */
    @Override
    public void moveInProduction(DepotSlot from, ProductionID dest, Resource loot) {
        context.view.sendPlayerError(context.nickname, errorMessage);
    }

    /**
     * This method set the normal production of an unknown production
     * @param id the id of the unknown production
     * @param normalProduction the input new normal production
     */
    @Override
    public void setNormalProduction(ProductionID id, NormalProduction normalProduction)  {
        context.view.sendPlayerError(context.nickname, errorMessage);
    }

    /**
     * This method allows the player to move Resources between Depots
     * @param from depot from which withdraw resource
     * @param to depot where insert withdrawn resource
     * @param loot is the Resource to move
     */
    @Override
    public void moveBetweenDepot(DepotSlot from, DepotSlot to, Resource loot) {
        context.view.sendPlayerError(context.nickname, errorMessage);
    }

    /**
     * This method activates the special ability of the LeaderCard
     * @param leaderId the string that identify the leader card
     */
    @Override
    public void activateLeaderCard(String leaderId) {
        context.view.sendPlayerError(context.nickname, errorMessage);
    }

    /**
     * This method removes a LeaderCard from the player
     * @param leaderId the string that identify the leader card to be discarded
     */
    @Override
    public void discardLeader(String leaderId) {
        context.view.sendPlayerError(context.nickname, errorMessage);
    }

    /**
     * The Player ends its turn
     */
    @Override
    public void endThisTurn() {
        context.view.sendPlayerError(context.nickname, errorMessage);
    }

    /**
     * Set a chosen resource attribute in player
     * @param slot the Depot where the Resource is taken from
     * @param chosen the resource chosen
     */
    @Override
    public void chooseResource(DepotSlot slot, ResourceType chosen) {
        context.view.sendPlayerError(context.nickname, errorMessage);
    }

    /**
     * Used during the buy dev card/production phase to return to the initial warehouse state.
     */
    @Override
    public void rollBack() {
        context.view.sendPlayerError(context.nickname, errorMessage);
    }

    /**
     * Player asks to use productions
     */
    @Override
    public void production() {
        context.view.sendPlayerError(context.nickname, errorMessage);
    }

    /**
     * Cheat commands used to make test faster
     */
    @Override
    public void resourceCheat() {
        context.view.sendPlayerMessage(context.nickname, "Cheat codes activated! You got 50 resource of any type in the strongbox");
        for (ResourceType loop : ResourceType.storable()) context.obtainResource(DepotSlot.STRONGBOX, ResourceBuilder.buildFromType(loop,50));
    }

    /**
     * Cheat commands used to make test faster
     */
    @Override
    public void fpCheat(int fp) {
        context.view.sendPlayerMessage(context.nickname, "Cheat codes activated! You move forward in the faith track");
        this.context.obtainResource(DepotSlot.STRONGBOX, ResourceBuilder.buildFaithPoint(fp));
    }
}