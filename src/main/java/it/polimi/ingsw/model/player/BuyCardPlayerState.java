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
        super(context, "",
                new Packet(HeaderTypes.RECONNECTED, ChannelTypes.PLAYER_ACTIONS, "reconnect"));
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
     * @return true if there where no issue, false instead
     */
    @Override
    public Packet buyDevCard(LevelDevCard row, ColorDevCard col, DevCardSlot destination) {
        this.context.slotDestination = destination;
        boolean res;
        try {
            res = this.context.match.buyDevCard(row, col);
        }

        catch (EndGameException e) {
            this.context.match.startEndGameLogic();                                                 // stop the game when the last player end his turn
            this.context.setState(new CountingPointsPlayerState(this.context));                     // set the player state to counting point so he can't do nothing more
            return new Packet(HeaderTypes.END_GAME, ChannelTypes.PLAYER_ACTIONS, e.getMessage());   // send the result
        }

        catch (Exception e) {
            return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, e.getMessage());
        }

        if (res) {
            this.context.setState(new MainActionDonePlayerState(this.context));
            this.history = new ArrayList<>();
        }
        else {
            rollBack();
        }
        return res ?
                new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "You bought the develop card requested"):
                new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, "You have no requisite to buy the card");
    }

    /**
     * This method allows the player to move Resources between Depots
     * @param from depot from which withdraw resource
     * @param to   depot where insert withdrawn resource, in this case only the devbuffer
     * @param loot resource to move
     */
    @Override
    public Packet moveBetweenDepot(DepotSlot from, DepotSlot to, Resource loot) {
        if(from == DepotSlot.BUFFER || to != DepotSlot.DEVBUFFER){
            return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, "You can't do that!");
        }

        try {
            this.context.personalBoard.moveResourceDepot(from, to, loot);
        } catch (Exception e) {
            return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, e.getMessage());
        }
        this.history.add(0,new MoveResource(from,to,loot));
        return new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "Resource moved successfully");
    }

    /**
     * The warehouse returns to the initial state
     * @return true if success, false otherwise
     */
    @Override
    public Packet rollBack() {
        for(MoveResource mr : this.history) {
            try {
                this.context.obtainResource(mr.getFrom(),mr.getResources());
            } catch (Exception e) {
                System.out.println("Problema!");
                System.out.println(mr.getDest().toString() + mr.getFrom().toString() + mr.getResources().toString());
            }
        }
        this.context.setState(new NoActionDonePlayerState(this.context));
        this.context.personalBoard.flushBufferDevCard();
        return new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "Action cancelled. Initial warehouse resource position restored.");
    }
}
