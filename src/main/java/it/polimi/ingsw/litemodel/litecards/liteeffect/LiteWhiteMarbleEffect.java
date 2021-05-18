package it.polimi.ingsw.litemodel.litecards.liteeffect;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.litemarkettray.LiteMarble;
import it.polimi.ingsw.view.cli.CLI;

public class LiteWhiteMarbleEffect extends LiteEffect{

    private final LiteMarble marble;


    @JsonCreator
    public LiteWhiteMarbleEffect(@JsonProperty("marble") LiteMarble marble) {
        this.marble = marble;

    }

    public LiteMarble getMarble() {
        return marble;
    }

    @Override
    public void printEffect(String[][] leaderCard, int x, int y) {
        leaderCard[x + 4][y + 3] = "O";
        leaderCard[x + 4][y + 5] = "=";
        leaderCard[x + 4][y + 7] = CLI.colorResource.get(marble.getToResource());
    }
}
