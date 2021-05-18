package it.polimi.ingsw.litemodel.liteplayer;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.ArrayList;
import java.util.List;

public class CountingPoints extends LiteState {

    @JsonCreator
    public CountingPoints() {
        super(new ArrayList<>());
    }
}
