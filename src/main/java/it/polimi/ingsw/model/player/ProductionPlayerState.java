package it.polimi.ingsw.model.player;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalNormalProduction;
import it.polimi.ingsw.model.exceptions.warehouse.production.UnknownUnspecifiedException;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.NormalProduction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.model.resource.Resource;

public class ProductionPlayerState extends PlayerState{
    /**
     * the constructor take the two final attribute of the state that are the personal board and the context.
     * the player holdings are passed to not share it out of the player states and do information hiding
     *
     * @param context   is the context
     */
    protected ProductionPlayerState(Player context) {
        super(context, "production phase");
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
     * This method set the normal production of an unknown production
     *
     * @param normalProduction the input new normal production
     * @param id the id of the unknown production
     * @return the succeed of the operation
     */
    @Override
    public Packet setNormalProduction(ProductionID id, NormalProduction normalProduction) {
        try {
            this.context.personalBoard.setNormalProduction(id, normalProduction);
        } catch (IllegalNormalProduction e) {
            return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, e.getMessage());
        }

        return new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "You have normalized the " + id.name().toLowerCase() + " production");
    }

    /**
     * This method moves a resource from a depot to a production
     * @param from the source of the resource to move
     * @param dest the destination of the resource to move
     * @param loot the resource to move
     * @return the succeed of the operation
     */
    @Override
    public Packet moveInProduction(DepotSlot from, ProductionID dest, Resource loot) {
        try {
            this.context.personalBoard.moveInProduction(from, dest, loot);
        } catch (Exception e) {
            e.printStackTrace();
            return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, e.getMessage());
        }

        return new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "Resources moved in production");
    }

    /**
     * This method takes the resources from the Depots and the Strongbox to
     * activate the productions and insert the Resources obtained into the Strongbox
     * @return the result of the operation
     */
    @Override
    public Packet activateProductions() {
        boolean res = false;
        try {
            res = this.context.personalBoard.activateProductions();
        }

        catch (EndGameException e) {
            this.context.match.startEndGameLogic();                                      // stop the game when the last player end his turn
            this.context.setState(new CountingPointsPlayerState(this.context));                     // set the player state to counting point so he can't do nothing more
            return new Packet(HeaderTypes.END_GAME, ChannelTypes.PLAYER_ACTIONS, e.getMessage());   // send the result
        }

        catch (Exception e) {
            return new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, e.getMessage());
        }

        if(res){
            this.context.setState(new MainActionDonePlayerState(this.context));
        }
        else{
            this.context.setState(new NoActionDonePlayerState(this.context));
        }

        return res ?
                new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "You activated the selected productions"):
                new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, "You have no requisites activate productions");
    }

    @Override
    public Packet rollBack() {
        this.context.setState(new NoActionDonePlayerState(this.context));
        this.context.personalBoard.restoreProd();
        return new Packet(HeaderTypes.OK, ChannelTypes.PLAYER_ACTIONS, "Action cancelled. Initial warehouse resource position restored.");
    }
}
