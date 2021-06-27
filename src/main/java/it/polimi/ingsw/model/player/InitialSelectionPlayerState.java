package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Pair;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.exceptions.card.AlreadyInDeckException;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;

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
        this.context.moveFaithMarker(initRes.two);
        this.toChoose = initRes.one;

        try {
            // todo addLeaderCards take a list of card as parameter so you don't send 4 update leader packets
            for(LeaderCard ld : this.context.match.requestLeaderCard()) this.context.personalBoard.addLeaderCard(ld);
        } catch (EmptyDeckException | AlreadyInDeckException e) {
            context.view.sendError(e.getMessage());
        }

        context.view.sendPlayerMessage(context.nickname, initRes.one > 0 ?
                "Please discard 2 leader cards and choose " + initRes.one + " resources" :
                "Please discard 2 leader cards");
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
    public void discardLeader(String leaderId) {
        if (discarded < toDiscard) {
            try {
                context.personalBoard.discardLeaderCard(leaderId);
                discarded ++;
                context.view.sendPlayerMessage(context.nickname, "You discarded " + leaderId);
            } catch (Exception e) {
                context.view.sendPlayerError(context.nickname, e.getMessage());
            }
        } else context.view.sendPlayerError(context.nickname, "you already discarded " + toDiscard + " cards");
    }


    /**
     * The player ends its turn
     */
    @Override
    public void endThisTurn() {
        if (chosen == toChoose && discarded == toDiscard) {
            this.context.setState(new PendingMatchStartPlayerState(this.context));
            context.view.sendMessage(context.nickname + " finished his game initialization");
            this.context.match.initialSelectionDone();
        } else {
            context.view.sendPlayerError(context.nickname, "You have to complete your job: discarded leader " + discarded + "/" + toDiscard + "; chosen resources " + chosen + "/" + toChoose);
        }
    }

    /**
     * set a chosen resource attribute in player
     * @param slot the Depot where the Resources are taken from
     * @param res the resource chosen
     */
    @Override
    public void chooseResource(DepotSlot slot, ResourceType res) {
        if (slot == DepotSlot.STRONGBOX || slot == DepotSlot.BUFFER) {
            context.view.sendPlayerError(context.nickname, "You can't place you resource in: " + slot);
            return;
        }
        if (chosen < toChoose) {
            try {
                if (context.obtainResource(slot, ResourceBuilder.buildFromType(res, 1))) {
                    chosen++;
                    context.view.sendPlayerMessage(context.nickname, "You obtained " + res.name().toLowerCase());
                } else context.view.sendPlayerError(context.nickname, "You can't override resource");

            } catch (Exception e) {
                context.view.sendPlayerError(context.nickname, e.getMessage());
            }
        } else context.view.sendPlayerError(context.nickname, "You already chose " + toChoose + " resources");
    }
}


