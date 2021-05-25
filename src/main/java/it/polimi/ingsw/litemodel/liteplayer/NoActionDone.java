package it.polimi.ingsw.litemodel.liteplayer;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.client.Actions;

import java.util.Arrays;

public class NoActionDone extends LiteState {

    @JsonCreator
    public NoActionDone() {
        super(Arrays.asList(
                Actions.USE_MARKET_TRAY,
                Actions.PAINT_MARBLE_IN_TRAY,
                Actions.BUY_DEV_CARD,
                Actions.ACTIVATE_PRODUCTION,
                Actions.MOVE_IN_PRODUCTION,
                Actions.SET_NORMAL_PRODUCTION,
                Actions.MOVE_BETWEEN_DEPOT,
                Actions.ACTIVATE_LEADER_CARD,
                Actions.DISCARD_LEADER_CARD
        ));
    }
}
