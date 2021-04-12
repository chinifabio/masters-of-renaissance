package it.polimi.ingsw.model.player.personalBoard.faithTrack;

/**
 * This class represents normal cells that composed the FaithTrack, this Cell do nothing
 */
public class Normal extends Cell {


    /**
     * This attribute is the constructor of the class
     */
    public Normal() {
        super();
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