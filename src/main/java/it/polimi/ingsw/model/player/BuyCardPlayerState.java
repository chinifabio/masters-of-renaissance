package it.polimi.ingsw.model.player;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.MoveResource;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.Resource;

import java.util.ArrayList;
import java.util.List;

public class BuyCardPlayerState extends PlayerState{

    private List<MoveResource> history = new ArrayList<>();

    /**
     * the constructor take the two final attribute of the state that are the personal board and the context.
     * the player holdings are passed to not share it out of the player states and do information hiding
     *
     * @param context        the context
     */
    protected BuyCardPlayerState(Player context) {
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
     *  player ask to buy the first card of the deck in position passed as parameter
     * @param row         the row of the card required
     * @param col         the column of the card required
     * @param destination the slot where put the dev card slot
     */
    @Override
    public void buyDevCard(LevelDevCard row, ColorDevCard col, DevCardSlot destination) {
        this.context.slotDestination = destination;
        boolean res;
        try {
            res = this.context.match.buyDevCard(row, col);
        }

        catch (EndGameException e) {
            this.context.match.startEndGameLogic();                                                 // stop the game when the last player end his turn
            this.context.setState(new CountingPointsPlayerState(this.context));                     // set the player state to counting point so he can't do nothing more
            return;
        }

        catch (Exception e) {
            context.view.sendPlayerError(context.nickname, e.getMessage());
            rollBack();
            return;
        }

        if (res) {
            context.view.sendPlayerMessage(context.nickname, "You bought the develop card requested");
            this.context.setState(new MainActionDonePlayerState(this.context));
            this.history = new ArrayList<>();
        }
        else {
            context.view.sendPlayerError(context.nickname, "You have not enough requisite to buy the card");
            rollBack();
        }
    }

    /**
     * This method allows the player to move Resources between Depots
     * @param from depot from which withdraw resource
     * @param to   depot where insert withdrawn resource, in this case only the devbuffer
     * @param loot resource to move
     */
    @Override
    public void moveBetweenDepot(DepotSlot from, DepotSlot to, Resource loot) {
        if(from == DepotSlot.BUFFER || to != DepotSlot.DEVBUFFER){
            context.view.sendPlayerError(context.nickname, "You can't use resource from you buffer or strongbox");
            return;
        }

        try {
            this.context.personalBoard.moveResourceDepot(from, to, loot);
        } catch (Exception e) {
            context.view.sendPlayerError(context.nickname, e.getMessage());
            return;
        }

        this.history.add(0,new MoveResource(from,to,loot));
        context.view.sendPlayerMessage(context.nickname, "Resource moved successfully");;
    }

    /**
     * The warehouse returns to the initial state
     */
    @Override
    public void rollBack() {
        for(MoveResource mr : this.history) {
            try {
                context.obtainResource(mr.getFrom(),mr.getResources());
            } catch (Exception e) {
                context.view.sendPlayerError(context.nickname, "Problem occurred: " + mr.getDest().toString() + mr.getFrom().toString() + mr.getResources().toString());
            }
        }
        context.setState(new NoActionDonePlayerState(this.context));
        context.personalBoard.flushBufferDevCard();
        context.view.sendPlayerMessage(context.nickname, "Action cancelled. Initial warehouse resource position restored.");
    }
}
