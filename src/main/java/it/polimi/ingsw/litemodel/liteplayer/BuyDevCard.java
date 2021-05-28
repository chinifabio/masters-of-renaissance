package it.polimi.ingsw.litemodel.liteplayer;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.client.Actions;

import java.util.Arrays;

public class BuyDevCard extends LiteState{

    @JsonCreator
    public BuyDevCard() {
        super(Arrays.asList(
                Actions.MOVE_BETWEEN_DEPOT,
                //Actions.ROLLBACK,
                Actions.BUY_DEV_CARD
        ));
    }
}
