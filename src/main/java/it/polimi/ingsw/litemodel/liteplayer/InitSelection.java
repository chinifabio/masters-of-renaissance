package it.polimi.ingsw.litemodel.liteplayer;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.client.Actions;

import java.util.Arrays;

public class InitSelection extends LiteState {

    @JsonCreator
    public InitSelection() {
        super(Arrays.asList(Actions.DISCARD_LEADER_CARD, Actions.CHOOSE_RESOURCE, Actions.END_TURN));
    }
}
