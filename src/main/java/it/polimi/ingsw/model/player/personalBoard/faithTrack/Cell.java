package it.polimi.ingsw.model.player.personalBoard.faithTrack;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * This interface represents the cells from which the FaithTrack is composed
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
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
   public Cell() {

   }


   /**
    * This method returns the victoryPoint of the cell
    * @return victoryPoint
    */
   public abstract int getVictoryPoint();

   /**
    * To be implemented
    */
   public abstract void onPlayerCross();

   /**
    * This method indicates in which vaticanSpace the Cell is located
    * @return vaticanSpace
    */
   public abstract VaticanSpace getVaticanSpace();

}
