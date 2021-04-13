package it.polimi.ingsw.model.player.personalBoard.faithTrack;


/**
 * This class represents special cells that activates the Vatican Report
 */
public class PopeSpace extends Cell {

    //TODO Salvare un riferimento a Match e chiamare match.vaticanReport(VaticanSpace)

    /**
     * * This method is the constructor of the class, it is empty because it takes parameters from Json File
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
        //TODO DA IMPLEMENTARE
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
