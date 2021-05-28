package it.polimi.ingsw.litemodel.litefaithtrack;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

public class LiteFaithTrack {

    private int playerPosition = 0;

    private final Map<String, Boolean> tiles;

    @JsonCreator
    public LiteFaithTrack() {
        tiles = new HashMap<>();
        tiles.put("FIRST",false);
        tiles.put("SECOND",false);
        tiles.put("THIRD",false);
    }


    public Integer getPlayerPosition() {
        return this.playerPosition;
    }

    public void movePlayer(int amount) {
        this.playerPosition = amount;
    }

    public void flipPopeTile(String toFlip){
        this.tiles.put(toFlip, true);
    }

    @JsonIgnore
    public Map<String, Boolean> getPopeTiles() {
        return tiles;
    }
}
