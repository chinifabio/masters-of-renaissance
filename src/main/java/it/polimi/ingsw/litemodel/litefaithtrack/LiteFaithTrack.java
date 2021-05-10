package it.polimi.ingsw.litemodel.litefaithtrack;

import java.util.HashMap;
import java.util.Map;

public class LiteFaithTrack {

    private int playerPosition = 0;

    private final Map<String, Boolean> popeTiles = new HashMap<String, Boolean>(){{
        put("FIRST",false);
        put("SECOND",false);
        put("THIRD",false);
    }};


    public Integer getPlayerPosition() {
        return this.playerPosition;
    }

    public void movePlayer(int amount) {
        this.playerPosition += amount;
    }

    public void flipPopeTile(String toFlip){
        this.popeTiles.put(toFlip, true);
    }

    public Map<String, Boolean> getPopeTiles() {
        return popeTiles;
    }

}
