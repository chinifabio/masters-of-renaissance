package it.polimi.ingsw.model.player.personalBoard.faithTrack;

import it.polimi.ingsw.model.resource.FaithPoint;
import it.polimi.ingsw.model.exceptions.NoMoreMovesException;
import java.util.*;

/**
 * This class represents the FaithTrack of each player that is composed by 25 cells which some of them have some special
 * effects like activating the Vatican Report or letting the Player receives some VictoryPoints
 */
public class FaithTrack {

    /**
     * This attribute is the representation of the track, composed by several cells
     */
    private List<Cell> track;

    /**
     * This attribute is the path of the file containing information to create the track
     */
    final private String path = "src/resources/TrackDetails.txt";

    /**
     * This attribute is the position of the Player in the track
     */
    private int playerPosition;

    /**
     * This attribute is the position of Lorenzo in the track
     */
    private int lorenzoPosition;

    /**
     * This attribute maps PopeSpace to the corresponding PopeTile that will be flipped if the player is in the same
     * Vatican Report space or further.
     */
    private Map<VaticanSpace, PopeTile> popeTiles;

    private Exception NoMoreMovesException;

    /**
     * This method is the constructor of the class, it reads from a file the details of the track and creates a list of cells,
     * also it initialize the Player and Lorenzo position to 0.
     */
    public FaithTrack() {
        this.track = new ArrayList<>();
        //Track to use for the testing
        track.add(new Normal(0, VaticanSpace.NONE));
        track.add(new Normal(0, VaticanSpace.NONE));
        track.add(new Normal(0, VaticanSpace.NONE));
        track.add(new Normal(1, VaticanSpace.NONE));
        track.add(new Normal(0, VaticanSpace.NONE));
        track.add(new Normal(0, VaticanSpace.FIRST));
        track.add(new Normal(2, VaticanSpace.FIRST));
        track.add(new Normal(0, VaticanSpace.FIRST));
        track.add(new PopeSpace(0, VaticanSpace.FIRST));
        track.add(new Normal(4, VaticanSpace.NONE));
        track.add(new Normal(0, VaticanSpace.NONE));
        track.add(new Normal(0, VaticanSpace.NONE));
        track.add(new Normal(6, VaticanSpace.SECOND));
        track.add(new Normal(0, VaticanSpace.SECOND));
        track.add(new Normal(0, VaticanSpace.SECOND));
        track.add(new Normal(9, VaticanSpace.SECOND));
        track.add(new PopeSpace(0, VaticanSpace.SECOND));
        track.add(new Normal(0, VaticanSpace.NONE));
        track.add(new Normal(12, VaticanSpace.NONE));
        track.add(new Normal(0, VaticanSpace.THIRD));
        track.add(new Normal(0, VaticanSpace.THIRD));
        track.add(new Normal(16, VaticanSpace.THIRD));
        track.add(new Normal(0, VaticanSpace.THIRD));
        track.add(new Normal(0, VaticanSpace.THIRD));
        track.add(new PopeSpace(20, VaticanSpace.THIRD));
        this.playerPosition = 0;
        this.lorenzoPosition = 0;

        popeTiles = new HashMap<>();
        popeTiles.put(VaticanSpace.FIRST, new PopeTile(2));
        popeTiles.put(VaticanSpace.SECOND, new PopeTile(3));
        popeTiles.put(VaticanSpace.THIRD, new PopeTile(4));
    }

    /**
     * This method returns the position of the player in the FaithTrack
     */
    public int getPlayerPosition(){
        return playerPosition;
    }

    /**
     * This method returns the position of Lorenzo in the FaithTrack
     */
    public int getLorenzoPosition(){
        return lorenzoPosition;
    }

    /**
     * This method move the FaithMarker of the player in the FaithTrack
     * @param points is the value of how far the player's marker must go
     */
    public void movePlayer(FaithPoint points) throws NoMoreMovesException{
        if (this.playerPosition >= track.size()-1 && points.amount() > 0) {
            throw new NoMoreMovesException("exception: The Player is in the last cell, he can't move");
        }
        this.playerPosition = playerPosition + points.amount();
        if (this.playerPosition >= track.size()-1) {
            this.playerPosition = track.size()-1;
        }
        //Da rimuovere e aggiungere al metodo che viene chiamato quando qualcuno attiva una PopeSpace
        flipPopeTile();
    }

    /**
     * This method move the marker of Lorenzo in the FaithTrack
     * @param amount is the value of how far the Lorenzo's marker must go
     */
    public void moveLorenzo(int amount) throws NoMoreMovesException {
        if (this.lorenzoPosition >= track.size()-1 && amount > 0) {
            throw new NoMoreMovesException("exception: Lorenzo is in the last cell, he can't move");
        }
        this.lorenzoPosition = lorenzoPosition + amount;
        if (this.lorenzoPosition >= track.size()-1) {
            this.lorenzoPosition = track.size()-1;
        }
    }

    /**
     * This method returns the value of the victoryPoint of the cell
     * @return victoryPoint
     */
    public int victoryPointCell(){
        return track.get(playerPosition).getVictoryPoint();
    }

    /**
     * This method indicates in which vaticanSpace the cell is located
     * @return VaticanSpace
     */
    public VaticanSpace vaticanSpaceCell(){
        return track.get(playerPosition).getVaticanSpace();
    }

    /**
     *
     * This method flips the PopeTile if the player is in a VaticanSpace and someone activated the PopeSpace
     */
    public void flipPopeTile(){
        //Aggiungere il controllo rispetto alle posizioni degli altri giocatori
        if (track.get(this.playerPosition).getVaticanSpace() != VaticanSpace.NONE) {
            if (!popeTiles.get(track.get(this.playerPosition).getVaticanSpace()).getIsFlipped()) {
                popeTiles.get(track.get(this.playerPosition).getVaticanSpace()).flipMe();
            }
        }
    }

    //Only for testing
    public boolean getTile(int tile){
        if (tile == 1) {
            return popeTiles.get(VaticanSpace.FIRST).getIsFlipped();
        }
        if (tile == 2) {
            return popeTiles.get(VaticanSpace.SECOND).getIsFlipped();
        }
        if (tile == 3) {
            return popeTiles.get(VaticanSpace.THIRD).getIsFlipped();
        }
        else return false;
    }


}
