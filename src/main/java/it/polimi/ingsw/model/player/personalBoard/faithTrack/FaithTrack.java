package it.polimi.ingsw.model.player.personalBoard.faithTrack;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.exceptions.IllegalMovesException;
import it.polimi.ingsw.model.exceptions.WrongPointsException;


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
     * This attribute is the position of Lorenzo in the track
     */
    private int lorenzoPosition;

    /**
     * This attribute maps PopeSpace to the corresponding PopeTile that will be flipped if the player is in the same
     * Vatican Report space or further.
     */
    private Map<VaticanSpace, PopeTile> popeTiles;


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
    public void movePlayer(int points) throws IllegalMovesException, WrongPointsException {

        if (points< 0){
            throw new WrongPointsException("exception: the Player can't use this resources to move!");
        }

        if (this.playerPosition >= track.size()-1 && points > 0) {
            throw new IllegalMovesException("exception: The Player is in the last cell, he can't move");
        }
        this.playerPosition = playerPosition + points;
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
    public void moveLorenzo(int amount) throws IllegalMovesException, WrongPointsException {

        if (amount < 0){
            throw new WrongPointsException("exception: Lorenzo can't go backward");
        }

        if (this.lorenzoPosition >= track.size()-1 && amount > 0) {
            throw new IllegalMovesException("exception: Lorenzo is in the last cell, he can't move");
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
     * This method flips the PopeTile if the player is in a VaticanSpace and someone activated the PopeSpace
     * @return true if the Tile is flipped
     */
    //TODO guardare meglio
    public boolean flipPopeTile(){
        //Aggiungere il controllo rispetto alle posizioni degli altri giocatori
        if (track.get(this.playerPosition).getVaticanSpace() != VaticanSpace.NONE) {
            if (!popeTiles.get(track.get(this.playerPosition).getVaticanSpace()).getIsFlipped()) {
                popeTiles.get(track.get(this.playerPosition).getVaticanSpace()).flipMe();
                return true;
            }
        }
        return false;
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
