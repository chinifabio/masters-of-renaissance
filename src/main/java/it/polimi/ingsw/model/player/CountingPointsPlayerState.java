package it.polimi.ingsw.model.player;


import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.exceptions.PlayerStateException;
import it.polimi.ingsw.model.match.markettray.RowCol;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.NormalProduction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.model.resource.Resource;

public class CountingPointsPlayerState extends PlayerState {
    /**
     * the constructor take the two final attribute of the state that are the personal board and the context.
     * the player holdings are passed to not share it out of the player states and do information hiding
     * @param context the context
     */
    protected CountingPointsPlayerState(Player context) {
        super(context, "match ended");
    }

    /**
     * can the player do staff?
     *
     * @return true yes, false no
     */
    @Override
    public boolean doStaff() {
        return false;
    }

    /**
     * this method start the turn of the player
     */
    @Override
    public void startTurn() throws PlayerStateException {
        throw new PlayerStateException("match ended, can't start the turn");
    }

// --------------------- PLAYER ACTION IMPLEMENTATIONS ---------------------------

}
