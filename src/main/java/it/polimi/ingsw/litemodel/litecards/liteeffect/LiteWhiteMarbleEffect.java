package it.polimi.ingsw.litemodel.litecards.liteeffect;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.litemarkettray.LiteMarble;

public class LiteWhiteMarbleEffect extends LiteEffect{

    private final LiteMarble marble;

    @JsonCreator
    public LiteWhiteMarbleEffect(@JsonProperty("marble") LiteMarble marble) {
        this.marble = marble;
    }

    public LiteMarble getMarble() {
        return marble;
    }
}
