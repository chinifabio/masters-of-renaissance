package it.polimi.ingsw.model.player.personalBoard.faithTrack;

/**
 * This class represents normal cells that composed the FaithTrack, this Cell do nothing
 */
public class Normal implements Cell {

    /**
     * This attribute represents the points that the Player will get when this cell is passed;
     */
    private int victoryPoint;

    /**
     * This attribute is the VaticanSpace in which the cell is located
     */
    private VaticanSpace vaticanSpace;

    /**
     * This attribute is the constructor of the class
     * @param victoryPoint is the value of VictoryPoint of the cell
     * @param vaticanSpace is the VaticanSpace in which the cell is located
     */
    public Normal(int victoryPoint, VaticanSpace vaticanSpace) {
        this.victoryPoint = victoryPoint;
        this.vaticanSpace = vaticanSpace;
    }

    /**
     * This method returns the victoryPoint of the cell
     * @return victoryPoint
     */
    @Override
    public int getVictoryPoint() {
        return victoryPoint;
    }

    /**
     * This method do nothing
     */
    @Override
    public void onPlayerCross() {
    }

    /**
     * This method returns the VaticanSpace in which the cell is located
     * @return vaticanSpace
     */
    @Override
    public VaticanSpace getVaticanSpace() {
        return vaticanSpace;
    }
}
