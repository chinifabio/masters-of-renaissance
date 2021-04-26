package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.exceptions.PlayerStateException;
import it.polimi.ingsw.model.exceptions.card.AlreadyInDeckException;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;
import it.polimi.ingsw.model.exceptions.card.MissingCardException;
import it.polimi.ingsw.model.exceptions.warehouse.WrongDepotException;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;

import java.util.Optional;

public class LeaderSelectionPlayerState extends PlayerState {
    /**
     * The number of resource to choose before end the turn
     */
    private int resourceToChoose;

    /**
     * How many resources the player chose
     */
    private int chosenResources = 0;

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
    public LeaderSelectionPlayerState(Player context) {
        super(context, "you need to discard leader card");

        Optional.of(context.initialSetup).ifPresent( x -> {
            this.context.moveFaithMarker(x.two);
            this.resourceToChoose = x.one;
        });

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
        throw new PlayerStateException("turn already started");
    }

// -------------------- PLAYER STATE IMPLEMENTATIONS -----------------------------------

    /**
     * This method removes a LeaderCard from the player
     *
     * @param leaderId the string that identify the leader card to be discarded
     */
    @Override
    public void discardLeader(String leaderId) throws PlayerStateException, EmptyDeckException, MissingCardException {
        if (discarded < toDiscard) {
            this.context.personalBoard.discardLeaderCard(leaderId);
            discarded ++;
        } else throw new PlayerStateException("you already discarded " + toDiscard + " cards");
    }

    /**
     * the player ends its turn
     *
     * @return true if success, false otherwise
     */
    @Override
    public boolean endThisTurn() throws PlayerStateException, WrongDepotException {
        if (chosenResources == resourceToChoose && discarded == toDiscard) {
            this.context.setState(new NotHisTurnPlayerState(this.context));
            return this.context.match.endMyTurn();
        } else throw new PlayerStateException("can't end the turn, complete your job {leader " + discarded + "/" + toDiscard+"} {resources " + chosenResources + "/" + resourceToChoose);
    }

    /**
     * set a chosen resource attribute in player
     *
     * @param chosen the resource chosen
     */
    @Override
    public void chooseResource(DepotSlot slot, ResourceType chosen) throws PlayerStateException, WrongDepotException {
        if (this.chosenResources < this.resourceToChoose) {
            if (this.context.obtainResource(slot, ResourceBuilder.buildFromType(chosen, 1))) chosenResources ++;
        } else if (slot == DepotSlot.STRONGBOX || slot == DepotSlot.BUFFER) throw new PlayerStateException("illegal depot chosen");
        else throw new PlayerStateException("you already chose " + resourceToChoose + " resources");
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
}
