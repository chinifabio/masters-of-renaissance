package it.polimi.ingsw.model.player.personalBoard.faithTrack;

/**
 * This class represents PopeTile that allow the Player to get VictoryPoints when they are flipped. They are flipped if
 * the player is in the corresponding VaticanSpace when a VaticanReport is activated.
 */
public class PopeTile {
    /**
     * This attribute is the victoryPoint that the Player could get if the tile is flipped
     */
    private int victoryPoint;

    /**
     * This attribute indicates if the Tile is flipped
     */
    private boolean isFlipped;

    /**
     * This method is the constructor of the class
     * @param victoryPoint is the VictoryPoint of the Tile that the player will receive if this Tile is flipped
     */
    public PopeTile(int victoryPoint) {
        this.victoryPoint = victoryPoint;
        this.isFlipped = false;
    }

    /**
     * This method flips the Tile
     */
    public void flipMe(){
        this.isFlipped = true;
    }

    /**
     * This method indicates if the Tile is flipped or not
     * @return the status of the Tile
     */
    public boolean getIsFlipped(){
        return this.isFlipped;
    }

    /**
     * This method returns the value of the victoryPoints
     */
    public int getVictoryPoint(){
        return this.victoryPoint;
    }
}