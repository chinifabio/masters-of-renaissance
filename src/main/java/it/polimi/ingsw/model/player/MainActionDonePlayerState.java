package it.polimi.ingsw.model.player;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.Resource;

/**
 * This class is the State where the Player did the MainAction
 */
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
     * can the player do stuff?
     *
     * @return true yes, false no
     */
    @Override
    public boolean doStuff() {
        return true;
    }

// ------------------- PLAYER ACTION IMPLEMENTATIONS -----------------------

    /**
     * This method allows the player to move Resources between Depots
     * @param from depot from which withdraw resource
     * @param to   depot where insert withdrawn resource
     * @param loot resource to move
     * @throws WrongDepotException if the Resource can't be moved in this Depots
     * @throws NegativeResourcesDepotException if the Depot hasn't enough resources
     * @throws UnobtainableResourceException if the Player can't obtain that Resource
     * @throws PlayerStateException if the Player can't do this action
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
     * @throws MissingCardException if the LeaderCard isn't in the Deck
     * @throws EmptyDeckException if the Deck of LeaderCard is empty
     * @throws LootTypeException if this attribute cannot be obtained from this Requisite
     * @throws WrongDepotException if the Resource can't be taken from the Depot
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
     * @throws EmptyDeckException if the Deck of LeaderCard is empty
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
     * This method ends the turn of the Player
     * @return true if the turn is correctly ended
     * @throws PlayerStateException if the Player can't do this action
     */
    @Override
    public Packet endThisTurn() {
        this.context.personalBoard.flushBufferDepot(this.context.model.getMatch());
        this.context.setState(new NotHisTurnPlayerState(this.context));
        this.context.model.getMatch().endMyTurn();
        return new Packet(HeaderTypes.END_TURN, ChannelTypes.PLAYER_ACTIONS, "your turn is ended");
    }
}
