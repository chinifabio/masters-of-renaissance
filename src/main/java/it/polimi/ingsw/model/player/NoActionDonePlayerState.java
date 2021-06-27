package it.polimi.ingsw.model.player;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.exceptions.tray.UnpaintableMarbleException;
import it.polimi.ingsw.model.match.markettray.RowCol;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.Resource;

/**
 * This class is the State where the Player doesn't execute his MainAction yet
 */
public class NoActionDonePlayerState extends PlayerState {
    /**
     * the constructor take the two final attribute of the state that are the personal board and the context.
     * the player holdings are passed to not share it out of the player states and do information hiding
     *
     * @param context        the context
     */
    protected NoActionDonePlayerState(Player context) {
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
        context.endThisTurn();
        return new NotHisTurnPlayerState(this.context);
    }


// -------------------------- PLAYER ACTION IMPLEMENTATIONS ---------------------------------


    /**
     * Uses the Market Tray
     * @param rc    enum to identify if I am pushing row or col
     * @param index the index of the row or column of the tray
     */
    @Override
    public void useMarketTray(RowCol rc, int index) {
        try {
            // using market tray
            this.context.match.useMarketTray(rc, index);
        }

        catch (EndGameException e) {
            this.context.match.startEndGameLogic();                                      // stop the game when the last player end his turn
            this.context.setState(new CountingPointsPlayerState(this.context));                     // set the player state to counting point so he can't do nothing more
            return;
        }

        catch (Exception e) {
            context.view.sendPlayerMessage(context.nickname, e.getMessage());
            return;
        }

        this.context.setState(new MainActionDonePlayerState(this.context));
    }

    /**
     * This method allows the player to select which Resources to get when he activates two LeaderCards with the same
     * SpecialAbility that converts white marbles in resources
     * @param conversionsIndex the index of the marble conversions available
     * @param marbleIndex      the index of chosen tray's marble to color
     */
    @Override
    public void paintMarbleInTray(int conversionsIndex, int marbleIndex) {
        try {
            context.match.paintMarbleInTray(this.context.marbleConversions.get(conversionsIndex).copy(), marbleIndex);
            context.view.sendPlayerMessage(context.nickname, "Marble painted successfully");
        } catch (UnpaintableMarbleException e) {
            context.view.sendPlayerError(context.nickname, e.getMessage());
        }

    }

    @Override
    public void buyDevCard(LevelDevCard row, ColorDevCard col, DevCardSlot destination) {
        context.view.sendPlayerError(context.nickname, "You have to enter the buy card phase!");
    }

    /**
     *  Player asks to buy a dev card
     */
    @Override
    public void buyCard() {
        context.setState(new BuyCardPlayerState(this.context));
        context.view.sendPlayerMessage(context.nickname, "Move the needed resource for the chosen card into the dev buffer");
    }


    /**
     * This method moves the player into the production phase
     */
    @Override
    public void production() {
        context.setState(new ProductionPlayerState(this.context));
        context.view.sendPlayerMessage(context.nickname, "Now you can move resources into productions");
    }

    /**
     * This method allows the player to move Resources between Depots
     * @param from depot from which withdraw resource
     * @param to   depot where insert withdrawn resource
     * @param loot resource to move
     */
    @Override
    public void moveBetweenDepot(DepotSlot from, DepotSlot to, Resource loot) {
        if(to == DepotSlot.DEVBUFFER) {
            context.view.sendPlayerError(context.nickname, "You can't move resources into " + to);
            return;
        }

        try {
            context.personalBoard.moveResourceDepot(from, to, loot);
            context.view.sendPlayerMessage(context.nickname, "Resource moved successfully");
        } catch (Exception e) {
            context.view.sendPlayerError(context.nickname, e.getMessage());
        }

    }

    /**
     * This method activates the special ability of the LeaderCard
     * @param leaderId the string that identify the leader card
     */
    @Override
    public void activateLeaderCard(String leaderId) {
        try {
            if (context.personalBoard.activateLeaderCard(leaderId)) context.view.sendMessage("You activated "+leaderId);
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
            context.personalBoard.moveFaithMarker(1, context.match);
            context.view.sendPlayerMessage(context.nickname, "You discarded " + leaderId);
        } catch (EndGameException e) {
            context.match.startEndGameLogic();                                      // stop the game when the last player end his turn
            context.setState(new CountingPointsPlayerState(context));                     // set the player state to counting point so he can't do nothing more
        }

    }

    /**
     * The player ends its turn
     */
    @Override
    public void endThisTurn() {
        context.view.sendPlayerError(context.nickname, "Your can't end your turn until you do the main action!");
    }
}
