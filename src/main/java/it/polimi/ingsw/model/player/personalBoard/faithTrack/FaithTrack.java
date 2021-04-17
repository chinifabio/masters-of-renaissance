package it.polimi.ingsw.model.player.personalBoard.faithTrack;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.exceptions.faithtrack.IllegalMovesException;
import it.polimi.ingsw.model.exceptions.warehouse.WrongPointsException;
import it.polimi.ingsw.model.match.PlayerToMatch;


import java.io.File;
import java.io.IOException;
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
     * This attribute is the position of the Player in the track
     */
    private int playerPosition;

    /**
     * This attribute maps PopeSpace to the corresponding PopeTile that will be flipped if the player is in the same
     * Vatican Report space or further.
     */
    private final Map<VaticanSpace, PopeTile> popeTiles;


    /**
     * This method is the constructor of the class, it reads from a file the details of the track and creates a list of cells,
     * also it initialize the Player and Lorenzo position to 0.
     */
    public FaithTrack() {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.track = objectMapper.readValue(
                    new File("src/resources/FaithTrack.json"),
                    new TypeReference<List<Cell>>(){});
        }catch (IOException e){
            System.out.println("The file to create the faith track wasn't found");
        }

        this.playerPosition = 0;

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
     * This method move the FaithMarker of the player in the FaithTrack
     * @param points is the value of how far the player's marker must go
     */
    public void movePlayer(int points, PlayerToMatch pm) throws IllegalMovesException, WrongPointsException {

        if (points< 0){
            throw new WrongPointsException("exception: the Player can't move backwards!");
        }

        if (this.playerPosition >= track.size()-1 && points > 0) {
            throw new IllegalMovesException("exception: The Player is in the last cell, he can't move");
        }

        for (int i = 0; i< points; i++){
            if (this.playerPosition >= track.size()-1) {
                i = points;
                this.playerPosition = track.size()-1;
            } else {
                this.playerPosition++;
                this.track.get(playerPosition).onPlayerCross(pm);
            }
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
     * This method flips the PopeTile if the player is in a VaticanSpace and someone activated the PopeSpace
     */
    public void flipPopeTile(VaticanSpace toCheck){
        if (track.get(this.playerPosition).getVaticanSpace().ordinal < toCheck.ordinal){
            //Deactivates the PopeTile -> Il popeTile non può più essere accettato
            popeTiles.get(toCheck).deactivate();
        }
    }

    //Only for testing
    public Cell getTile(){
        return this.track.get(playerPosition);
    }

    //only for testing
    public boolean isFlipped(VaticanSpace vs){
        if(vs == VaticanSpace.NONE) return false;
        return this.popeTiles.get(vs).isDeactivated();
    }
}
