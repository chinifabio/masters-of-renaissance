package it.polimi.ingsw.view.litemodel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.VaticanSpace;


public class LiteCell {

    /**
     * This is the constructor of the class
     * @param type is the type of the Cell
     * @param points is the value of victoryPoints of the cell
     * @param vaticanSpace is the type of VaticanSpace of the cell
     */
    @JsonCreator
    public LiteCell(@JsonProperty("type") String type, @JsonProperty("victoryPoint") int points, @JsonProperty("vaticanSpace") VaticanSpace vaticanSpace) {
        this.type = type;
        this.vaticanSpace = vaticanSpace;
        this.victoryPoints = points;
    }

    /**
     * This attribute is the value of victory point of the cell
     */
    private final int victoryPoints;

    /**
     * This attribute is the type of cell, that it can be Normal or PopeSpace
     */
    private final String type;

    /**
     * This attribute is the type of vaticanSpace
     */
    private final VaticanSpace vaticanSpace;

    /**
     * This method returns the value of victoryPoints
     * @return (int) value of victoryPoints
     */
    public int getVictoryPoints(){
        return this.victoryPoints;
    }

    /**
     * This attribute returns the vaticanSpace of the cell
     * @return (VaticanSpace) the type of VaticanSpace
     */
    public VaticanSpace getVaticanSpace(){
        return this.vaticanSpace;
    }

    /**
     * This attribute return the type of the cell, that it can be Normal or PopeSpace
     * @return (String) type of the cell
     */
    public String isPopeSpace() {
        return this.type;
    }

}
