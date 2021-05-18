package it.polimi.ingsw.litemodel.liteplayer;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.ArrayList;
import java.util.List;

public class NotHisTurn extends LiteState {

    @JsonCreator
    public NotHisTurn() {
        super(new ArrayList<>());
    }
}
