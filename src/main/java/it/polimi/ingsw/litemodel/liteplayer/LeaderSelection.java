package it.polimi.ingsw.litemodel.liteplayer;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;
import java.util.List;

public class LeaderSelection extends LiteState {

    @JsonCreator
    public LeaderSelection() {
        super(Arrays.asList(Actions.DISCARD_LEADER_CARD, Actions.CHOOSE_RESOURCE, Actions.END_TURN));
    }
}
