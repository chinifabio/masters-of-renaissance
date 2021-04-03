package it.polimi.ingsw.model.player.personalBoard.faithTrack;

/**
 * This interface represents the cells from which the FaithTrack is composed
 */
public interface Cell {
   /**
    * This method returns the victoryPoint of the cell
    * @return victoryPoint
    */
   int getVictoryPoint();

   /**
    * To be implemented
    */
   void onPlayerCross();

   /**
    * This method indicates in which vaticanSpace the Cell is located
    * @return vaticanSpace
    */
   VaticanSpace getVaticanSpace();

}
