package it.polimi.ingsw.litemodel.liteplayer;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.ArrayList;

public class PendingStart extends LiteState {

    @JsonCreator
    public PendingStart() {
        super(new ArrayList<>());
    }
}
