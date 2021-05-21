package it.polimi.ingsw.litemodel.liteplayer;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.ArrayList;

public class PendingInit extends LiteState{

    @JsonCreator
    public PendingInit() {
        super(new ArrayList<>());
    }
}
