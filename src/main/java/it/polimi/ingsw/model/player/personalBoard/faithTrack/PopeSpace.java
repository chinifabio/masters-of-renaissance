package it.polimi.ingsw.model.player.personalBoard.faithTrack;


/**
 * This class represents special cells that activates the Vatican Report
 */
public class PopeSpace implements Cell {

    /**
     * This attribute represents the points that the Player will get when this cell is passed;
     */
    private int victoryPoint;


    /**
     *This attribute is the VaticanSpace in which the cell is located
     */
    private VaticanSpace vaticanSpace;

    /**
     * This method is the constructor of the class
     * @param vaticanSpace is the VaticanSpace in which the cell is located
     */
    public PopeSpace(int victoryPoint, VaticanSpace vaticanSpace) {
        this.vaticanSpace = vaticanSpace;
        this.victoryPoint = victoryPoint;
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
