package it.polimi.ingsw.litemodel.litefaithtrack;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the lite version of the FaithTrack
 */
public class LiteFaithTrack {

    /**
     * This attribute is the actual position of the player in the track
     */
    private int playerPosition = 0;

    /**
     * This map contains every PopeTile and if they are turned
     */
    private final HashMap<String, Boolean> tiles;

    /**
     * This is the constructor of the class: it sets every PopeTile as false
     */
    @JsonCreator
    public LiteFaithTrack() {
        tiles = new HashMap<>();
        tiles.put("FIRST",false);
        tiles.put("SECOND",false);
        tiles.put("THIRD",false);
    }

    /**
     * This method returns the player position
     * @return the player position
     */
    public Integer getPlayerPosition() {
        return this.playerPosition;
    }

    //TODO printer test
    /**
     * printer test
     * @param amount
     */
    public void movePlayer(int amount) {
        this.playerPosition = amount;
    }

    /**
     * This method flips a PopeTile given by the string
     * @param toFlip to set true
     */
    public void flipPopeTile(String toFlip){
        this.tiles.put(toFlip, true);
    }

    /**
     * This method returns the map of the PopeTiles and if they are flipped
     * @return the map of PopeTiles and activation
     */
    public HashMap<String, Boolean> getPopeTiles() {
        return tiles;
    }
}
