package it.polimi.ingsw.model.player;

import it.polimi.ingsw.litemodel.liteplayer.CountingPoints;
import it.polimi.ingsw.litemodel.liteplayer.LiteState;

/**
 * This class is the State where are counted the VictoryPoints of the Player
 */
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
     * can the player do stuff?
     * @return true yes, false no
     */
    @Override
    public boolean doStuff() {
        return false;
    }

// --------------------- PLAYER ACTION IMPLEMENTATIONS ---------------------------


    /**
     * Create a lite version of the class and serialize it in json
     *
     * @return the json representation of the lite version of the class
     */
    @Override
    public LiteState liteVersion() {
        return new CountingPoints();
    }
}
