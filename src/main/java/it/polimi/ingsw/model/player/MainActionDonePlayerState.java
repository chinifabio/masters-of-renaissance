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

    /**
     * Give the state of the player in case of reconnection
     *
     * @return the reconnection player state
     */
    @Override
    public PlayerState reconnectionState() {
        context.endThisTurn();
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
    public void moveBetweenDepot(DepotSlot from, DepotSlot to, Resource loot) {
        if(to == DepotSlot.DEVBUFFER) {
            context.view.sendPlayerError(context.nickname, "You can't do that!");
            return;
        }

        try {
            this.context.personalBoard.moveResourceDepot(from, to, loot);
        } catch (Exception e) {
            context.view.sendPlayerError(context.nickname, e.getMessage());
            return;
        }

        context.view.sendPlayerMessage(context.nickname, "Resource moved successfully");
    }

    /**
     * This method activates the special ability of the LeaderCard
     * @param leaderId the string that identify the leader card
     */
    @Override
    public void activateLeaderCard(String leaderId) {
        try {
            if (this.context.personalBoard.activateLeaderCard(leaderId)) context.view.sendMessage("You activated "+leaderId);
            else context.view.sendPlayerError(context.nickname, "You have not enough requisite to activate the leader");
        } catch (Exception e) {
            context.view.sendPlayerError(context.nickname, e.getMessage());
        }
    }

    /**
     * This method removes a LeaderCard from the player
     * @param leaderId the string that identify the leader card to be discarded
     */
    @Override
    public void discardLeader(String leaderId) {
        try {
            this.context.personalBoard.discardLeaderCard(leaderId);
        } catch (Exception e) {
            context.view.sendPlayerError(context.nickname, e.getMessage());
            return;
        }

        try {
            this.context.personalBoard.moveFaithMarker(1, this.context.match);
        }

        catch (EndGameException e) {
            this.context.match.startEndGameLogic();                                      // stop the game when the last player end his turn
            this.context.setState(new CountingPointsPlayerState(this.context));                     // set the player state to counting point so he can't do nothing more
            return;
        }

        context.view.sendPlayerMessage(context.nickname, "You discarded " + leaderId);
    }

    /**
     * This method ends the turn of the Player
     */
    @Override
    public void endThisTurn() {
        context.personalBoard.flushBufferDepot(context.match);
        context.setState(new NotHisTurnPlayerState(context));
        context.match.turnDone();
    }
}
