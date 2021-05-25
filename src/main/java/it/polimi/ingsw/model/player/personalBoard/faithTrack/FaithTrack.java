package it.polimi.ingsw.model.player.personalBoard.faithTrack;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.communication.packet.updates.FaithTrackUpdater;
import it.polimi.ingsw.litemodel.litefaithtrack.LiteFaithTrack;
import it.polimi.ingsw.model.Dispatcher;
import it.polimi.ingsw.model.MappableToLiteVersion;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.match.PlayerToMatch;


import java.io.IOException;
import java.util.*;

/**
 * This class represents the FaithTrack of each player that is composed by 25 cells which some of them have some special
 * effects like activating the Vatican Report or letting the Player receives some VictoryPoints
 */
public class FaithTrack implements MappableToLiteVersion {

    /**
     * This attribute is the representation of the FaithTrack, composed by several cells
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
     * the owner of the faith track
     */
    private final Dispatcher view;

    /**
     * Nickname of the owner of the faith track
     */
    private final String nickname;

    /**
     * This method is the constructor of the class, it reads from a file the details of the track and creates a list of cells,
     * also it initialize the Player position to 0.
     */
    public FaithTrack(Dispatcher view, String nickname) throws IOException {

        this.track = new ObjectMapper().readValue(
                    getClass().getResourceAsStream("/json/FaithTrack.json"),
                    new TypeReference<List<Cell>>(){});

        this.playerPosition = 0;

        this.view = view;
        this.nickname = nickname;

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
     * This method moves the FaithMarker of the player in the FaithTrack
     * @param points is the value of how far the player's marker must go
     * @param pm is the Player that receive the faithPoints
     * @throws EndGameException if the FaithMarker is in the last cell
     */
    public void movePlayer(int points, PlayerToMatch pm) throws EndGameException {
        if(playerPosition >= (track.size()-1)) return;
        for (int i = 0; i< points; i++){
            this.playerPosition++;
            this.track.get(playerPosition).onPlayerCross(pm);
            if (this.playerPosition >= track.size()-1) {
                throw new EndGameException();
            }
        }
        updateFaithTrack();
    }


    /**
     * This method indicates in which vaticanSpace the cell is located
     * @return VaticanSpace
     */
    public VaticanSpace vaticanSpaceCell(){
        return track.get(playerPosition).getVaticanSpace();
    }

    /**
     * This method flips the PopeTile if someone activated the PopeSpace and the Player is before the VaticanSpace section activated
     * @param toCheck is the VaticanSpace that has been activated
     */
    public void flipPopeTile(VaticanSpace toCheck){
        if (track.get(this.playerPosition).getVaticanSpace().ordinal >= toCheck.ordinal) {
            if (popeTiles.containsKey(toCheck)) popeTiles.get(toCheck).flipMe();
            updateFaithTrack();
        }
    }

    /**
     * This method counts the victory points earned by the player
     * @return the value of the VictoryPoints
     */
    public int countingFaithTrackVictoryPoints(){
        int points = 0;
        int position = getPlayerPosition();

        try {
            while (track.get(position).getVictoryPoint() == 0) {
                position--;
            }
        } catch (IndexOutOfBoundsException e){
            return 0;
        }

        for (Map.Entry<VaticanSpace, PopeTile> entry : popeTiles.entrySet()) {
            if (entry.getValue().isFlipped()){
                points = points + entry.getValue().getVictoryPoint();
            }
        }

        return points + track.get(position).getVictoryPoint();
    }

    /**
     * Tells you if a pope tile linked to the passed vatican space is flipper or not
     * @param vs the vatican space on which check the pope tile
     * @return the flip value
     */
    public boolean isFlipped(VaticanSpace vs){
        if(vs == VaticanSpace.NONE) return false;
        return this.popeTiles.get(vs).isFlipped();
    }

    /**
     * Create a lite version of the class and serialize it in json
     * @return the json representation of the lite version of the class
     */
    @Override
    public LiteFaithTrack liteVersion() {
        LiteFaithTrack lite = new LiteFaithTrack();

        lite.movePlayer(this.playerPosition);

        for (Map.Entry<VaticanSpace, PopeTile> e : this.popeTiles.entrySet()) {
            if (e.getValue().isFlipped()) lite.flipPopeTile(e.getKey().name());
        }

        return lite;
    }

    protected void updateFaithTrack() {
        this.view.publish(new FaithTrackUpdater(this.nickname, this.liteVersion()));
    }

    //Only for testing
    public int victoryPointCellPlayer(){
        return track.get(playerPosition).getVictoryPoint();
    }
}
