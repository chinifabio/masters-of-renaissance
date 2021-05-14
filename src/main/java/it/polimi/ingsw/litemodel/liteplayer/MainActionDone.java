package it.polimi.ingsw.litemodel.liteplayer;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public class MainActionDone extends LiteState {

    @JsonCreator
    public MainActionDone() {
        super(Arrays.asList(
                Actions.MOVE_BETWEEN_DEPOT,
                Actions.ACTIVATE_LEADER_CARD,
                Actions.DISCARD_LEADER_CARD,
                Actions.END_TURN
        ));
    }
}
