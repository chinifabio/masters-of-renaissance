package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalNormalProduction;
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
        super(context, "You are in the production phase");
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
    public PlayerState handleDisconnection() {
        context.rollBack();
        return new NotHisTurnPlayerState(this.context);
    }

// -------------------------- PLAYER ACTION IMPLEMENTATIONS ---------------------------------

    /**
     * This method set the normal production of an unknown production
     *
     * @param id the id of the unknown production
     * @param normalProduction the input new normal production
     */
    @Override
    public void setNormalProduction(ProductionID id, NormalProduction normalProduction) {
        try {
            context.personalBoard.setNormalProduction(id, normalProduction);
            context.view.sendPlayerMessage(context.nickname, "You normalized " + id + " production");
        } catch (IllegalNormalProduction e) {
            context.view.sendPlayerError(context.nickname, e.getMessage());
        }
    }

    /**
     * This method moves a resource from a depot to a production
     * @param from the source of the resource to move
     * @param dest the destination of the resource to move
     * @param loot the resource to move
     */
    @Override
    public void moveInProduction(DepotSlot from, ProductionID dest, Resource loot) {
        try {
            context.personalBoard.moveInProduction(from, dest, loot);
            context.view.sendPlayerMessage(context.nickname, "Resources moved in production");
        } catch (Exception e) {
            context.view.sendPlayerError(context.nickname, e.getMessage());
        }
    }

    /**
     * This method takes the resources from the Depots and the Strongbox to
     * activate the productions and insert the Resources obtained into the Strongbox
     */
    @Override
    public void activateProductions() {
        try {
            if(context.personalBoard.activateProductions()) {
                context.setState(new MainActionDonePlayerState(this.context));
                context.view.sendPlayerMessage(context.nickname, "You activated the selected productions");
            } else {
                context.setState(new NoActionDonePlayerState(this.context));
                context.view.sendPlayerError(context.nickname, "Incompatible added resources to activate the selected productions");
            }
        }

        catch (EndGameException e) {
            context.match.startEndGameLogic(context.nickname + " reached the end of the faith track!");                                      // stop the game when the last player end his turn
        }

        catch (Exception e) {
            context.view.sendPlayerError(context.nickname, e.getMessage());
        }
    }

    /**
     * This method restore the original status of Productions and Depots
     */
    @Override
    public void rollBack() {
        context.setState(new NoActionDonePlayerState(this.context));
        context.personalBoard.restoreProd();
        context.view.sendPlayerMessage(context.nickname, "Action cancelled. Initial warehouse resource position restored.");
    }
}
