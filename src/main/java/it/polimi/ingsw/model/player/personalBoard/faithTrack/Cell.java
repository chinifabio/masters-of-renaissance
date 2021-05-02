package it.polimi.ingsw.model.player.personalBoard.faithTrack;

import com.fasterxml.jackson.annotation.*;
import it.polimi.ingsw.model.match.PlayerToMatch;

/**
 * This interface represents the cells from which the FaithTrack is composed
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "Normal", value = Normal.class),
        @JsonSubTypes.Type(name = "PopeSpace", value = PopeSpace.class)
})
public abstract class Cell {

   /**
    * This attribute represents the points that the Player will get when this cell is passed;
    */
   protected  int victoryPoint;

   /**
    * This attribute is the VaticanSpace in which the cell is located
    */
   protected  VaticanSpace vaticanSpace;

   /**
    * This method is the constructor of the Cell, it is empty because it takes parameters from Json File
    */
   @JsonCreator
   public Cell() {}

   /**
    * This method returns the victoryPoint of the cell
    * @return victoryPoint
    */
   public abstract int getVictoryPoint();

   /**
    * This method activates the VaticanReport if the Player cross a PopeSpace or do nothing if the Player cross a normal cell
    */
   public abstract void onPlayerCross(PlayerToMatch pm);

   /**
    * This method indicates in which vaticanSpace the Cell is located
    * @return vaticanSpace
    */
   public abstract VaticanSpace getVaticanSpace();

}
