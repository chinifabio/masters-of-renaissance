package it.polimi.ingsw.model.player.personalBoard.faithTrack;


/**
 * This class represents special cells that activates the Vatican Report
 */
public class PopeSpace extends Cell {


    /**
     * * This method is the constructor of the class
     */
    public PopeSpace() {
        super();
    }

    /**
     * Do nothing because PopeSpace doesn't have victoryPoint
     * @return nothing
     */
    @Override
    public int getVictoryPoint() {
        return victoryPoint;
    }

    /**
     * To be implemented
     */
    @Override
    public void onPlayerCross() {
        //To be implemented
    }

    /**
     * This method returns the VaticanSpace in which the cell is located
     * @return vaticanSpace
     */
    @Override
    public VaticanSpace getVaticanSpace() {
        return this.vaticanSpace;
    }
}
