package it.polimi.ingsw.model.player.personalBoard.faithTrack;

import it.polimi.ingsw.model.match.PlayerToMatch;

/**
 * This class represents normal cells that composed the FaithTrack, this Cell do nothing
 */
public class Normal extends Cell {

    /**
     * This attribute is the constructor of the class, it is empty because it takes parameters from Json File
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
     * @param pm is the Player that crossed this cell
     */
    @Override
    public void onPlayerCross(PlayerToMatch pm) {
    }

    /**
     * This method returns the VaticanSpace in which the cell is located
     * @return vaticanSpace
     */
    @Override
    public VaticanSpace getVaticanSpace() {
        return vaticanSpace;
    }

    @Override
    public boolean isPopeSpace() {
        return false;
    }
}
