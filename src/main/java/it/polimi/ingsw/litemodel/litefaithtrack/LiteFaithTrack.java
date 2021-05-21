package it.polimi.ingsw.litemodel.litefaithtrack;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.FaithTrack;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.VaticanSpace;
import it.polimi.ingsw.util.Tuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiteFaithTrack {

    private int playerPosition = 0;

    private final Map<String, Boolean> tiles;

    public LiteFaithTrack() {
        tiles = new HashMap<>();
        tiles.put("FIRST",false);
        tiles.put("SECOND",false);
        tiles.put("THIRD",false);

    }

    @JsonCreator
    public LiteFaithTrack(@JsonProperty("tiles") List<Tuple<String, Boolean>> til) {
        this.tiles = new HashMap<>();
        til.forEach(t -> this.tiles.put(t.a, t.b));
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

    @JsonGetter("tiles")
    public List<Tuple<String, Boolean>> getTiles() {
        List<Tuple<String, Boolean>> res = new ArrayList<>();
        tiles.forEach((s, b)->res.add(new Tuple<>(s, b)));
        return res;
    }



}
