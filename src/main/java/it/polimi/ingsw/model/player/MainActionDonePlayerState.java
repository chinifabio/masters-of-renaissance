package it.polimi.ingsw.model.player;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.litemodel.liteplayer.LiteState;
import it.polimi.ingsw.litemodel.liteplayer.MainActionDone;
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

    /**
     * Give the state of the player in case of reconnection
     *
     * @return the reconnection player state
     */
    @Override
    public PlayerState reconnectionState() {
        return new NotHisTurnPlayerState(this.context);
    }

    // ------------------- PLAYER ACTION IMPLEMENTATIONS -----------------------

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

        return new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "operation done successfully");
    }

    /**
     * This method activates the special ability of the LeaderCard
     * @param leaderId the string that identify the leader card
     */
    @Override
    public Packet activateLeaderCard(String leaderId) {
        boolean res;
        try {
            res = this.context.personalBoard.activateLeaderCard(leaderId);
        } catch (Exception e) {
            return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, e.getMessage());
        }

        return res ?
                new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "operation done successfully"):
                new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, "you have no requisite to activate the leader");
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

        return new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "operation done successfully");
    }

    /**
     * This method ends the turn of the Player
     * @return true if the turn is correctly ended
     */
    @Override
    public Packet endThisTurn() {
        this.context.personalBoard.flushBufferDepot(this.context.match);
        this.context.setState(new NotHisTurnPlayerState(this.context));
        this.context.match.turnDone();
        return new Packet(HeaderTypes.END_TURN, ChannelTypes.PLAYER_ACTIONS, "your turn is ended");
    }

    /**
     * Create a lite version of the class and serialize it in json
     *
     * @return the json representation of the lite version of the class
     */
    @Override
    public LiteState liteVersion() {
        return new MainActionDone();
    }
}
