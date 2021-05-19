package it.polimi.ingsw.model.player;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.litemodel.liteplayer.LeaderSelection;
import it.polimi.ingsw.litemodel.liteplayer.LiteState;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.exceptions.card.AlreadyInDeckException;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.util.Pair;

import java.util.Optional;

/**
 * This class is the State where the LeaderCard are selected by the Player
 */
public class InitialSelectionPlayerState extends PlayerState {

    /**
     * The number of resource to choose before end the turn
     */
    private final int toChoose;

    /**
     * How many resources the player chose
     */
    private int chosen = 0;

    /**
     * counter of discarded leader card
     */
    private int discarded = 0;

    /**
     * the amount of card that need to be discarded
     */
    private final int toDiscard = 2;

    /**
     * the constructor take the two final attribute of the state that are the personal board and the context.
     * the player holdings are passed to not share it out of the player states and do information hiding
     *
     * @param context        the context
     */
    public InitialSelectionPlayerState(Player context) {
        super(context, "You are in the game initialization phase, you need to discard leader card or choose resource");

        Pair<Integer> initRes = Optional.of(context.initialSetup).orElse(new Pair<>(0,0));
        try { this.context.moveFaithMarker(initRes.two); } catch (EndGameException ignore) {}
        this.toChoose = initRes.one;

        for(LeaderCard ld : this.context.match.requestLeaderCard()) {
            try {
                this.context.personalBoard.addLeaderCard(ld);
            } catch (AlreadyInDeckException e) {
                e.printStackTrace();
                // todo error to player handler
            }
        }
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
        return this;
    }

    /**
     * Receive the input to start the turn
     */
    @Override
    public void starTurn() {
        System.out.println("errore");
    }

    // -------------------- PLAYER STATE IMPLEMENTATIONS -----------------------------------

    /**
     * This method removes a LeaderCard from the player
     * @param leaderId the string that identify the leader card to be discarded
     */
    @Override
    public Packet discardLeader(String leaderId) {
        if (discarded < toDiscard) {
            try {
                this.context.personalBoard.discardLeaderCard(leaderId);
                discarded ++;
                return new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "Operation done successfully! (ɔ◔‿◔)ɔ ♥");
            } catch (Exception e) {
                return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, e.getMessage());
            }
        } else return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, "you already discarded " + toDiscard + " cards");
    }


    /**
     * The player ends its turn
     * @return true if success, false otherwise
     */
    @Override
    public Packet endThisTurn() {
        if (chosen == toChoose && discarded == toDiscard) {

            this.context.setState(new PendingMatchStartPlayerState(this.context));
            this.context.match.initDone();
            return new Packet(HeaderTypes.END_TURN, ChannelTypes.PLAYER_ACTIONS, "Initial phase done!");

        } else
            return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, "(*෴*) You have to complete your job: discarded leader " + discarded + "/" + toDiscard+"; chosen resources " + chosen + "/" + toChoose +"}");
    }

    /**
     * set a chosen resource attribute in player
     * @param slot the Depot where the Resources are taken from
     * @param chosen the resource chosen
     */
    @Override
    public Packet chooseResource(DepotSlot slot, ResourceType chosen) {
        if (slot == DepotSlot.STRONGBOX || slot == DepotSlot.BUFFER)
            return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, "invalid destination");
        if (this.chosen < this.toChoose) {
            try {
                if (this.context.obtainResource(slot, ResourceBuilder.buildFromType(chosen, 1))) {
                    this.chosen++;
                    return new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "operation done successfully");
                } else
                    return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, "can't override resource");

            } catch (Exception e) {
                return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, e.getMessage());
            }
        } else return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, "you already chose " + toChoose + " resources");
    }

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "LeaderSelection state";
    }

    /**
     * Create a lite version of the class and serialize it in json
     *
     * @return the json representation of the lite version of the class
     */
    @Override
    public LiteState liteVersion() {
        return new LeaderSelection();
    }
}
