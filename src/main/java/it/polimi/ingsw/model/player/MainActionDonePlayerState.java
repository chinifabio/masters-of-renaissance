package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.exceptions.PlayerStateException;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;
import it.polimi.ingsw.model.exceptions.card.MissingCardException;
import it.polimi.ingsw.model.exceptions.requisite.LootTypeException;
import it.polimi.ingsw.model.exceptions.warehouse.NegativeResourcesDepotException;
import it.polimi.ingsw.model.exceptions.warehouse.UnobtainableResourceException;
import it.polimi.ingsw.model.exceptions.warehouse.WrongDepotException;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.Resource;

public class MainActionDonePlayerState extends PlayerState {

    /**
     * the constructor take the two final attribute of the state that are the personal board and the context.
     * the player holdings are passed to not share it out of the player states and do information hiding
     *
     * @param context the context
     */
    public MainActionDonePlayerState(Player context) {
        super(context, "main action already done");
    }

    /**
     * can the player do staff?
     *
     * @return true yes, false no
     */
    @Override
    public boolean doStaff() {
        return true;
    }

    /**
     * this method start the turn of the player
     */
    @Override
    public void startTurn() throws PlayerStateException {
        throw new PlayerStateException("can't start turn");
    }

// ------------------- PLAYER ACTION IMPLEMENTATIONS -----------------------

    /**
     * This method allows the player to move Resources between Depots
     *
     * @param from depot from which withdraw resource
     * @param to   depot where insert withdrawn resource
     * @param loot resource to move
     */
    @Override
    public void moveBetweenDepot(DepotSlot from, DepotSlot to, Resource loot) throws WrongDepotException, NegativeResourcesDepotException, UnobtainableResourceException, PlayerStateException {
        this.context.personalBoard.moveResourceDepot(from, to, loot);
    }

    /**
     * This method activates the special ability of the LeaderCard
     *
     * @param leaderId the string that identify the leader card
     */
    @Override
    public void activateLeaderCard(String leaderId) throws MissingCardException, EmptyDeckException, LootTypeException, WrongDepotException {
        this.context.personalBoard.activateLeaderCard(leaderId);
    }

    /**
     * This method removes a LeaderCard from the player
     *
     * @param leaderId the string that identify the leader card to be discarded
     */
    @Override
    public void discardLeader(String leaderId) throws EmptyDeckException, MissingCardException {
        this.context.personalBoard.discardLeaderCard(leaderId);
        this.context.personalBoard.moveFaithMarker(1, this.context.match);
    }

    /**
     * the player ends its turn
     *
     * @return true if success, false otherwise
     */
    @Override
    public boolean endThisTurn() throws PlayerStateException, WrongDepotException {
        this.context.personalBoard.flushBufferDepot(this.context.match);
        this.context.setState(new NotHisTurnPlayerState(this.context));
        return this.context.match.endMyTurn();
    }
}
