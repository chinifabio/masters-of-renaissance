package it.polimi.ingsw;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.exceptions.NoMoreMovesException;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.*;
import it.polimi.ingsw.model.resource.builder.Resource;
import it.polimi.ingsw.model.resource.builder.ResourceDirector;
import org.junit.jupiter.api.Test;

public class FaithTrackTest {

    /**
     * Testing if the FaithTrack is correctly created using the player position to take cells info
     * @throws NoMoreMovesException if the Player or Lorenzo moves when they are in the last cell
     */
    @Test
    public void infoFaithTrack() throws NoMoreMovesException{

        FaithTrack track = new FaithTrack();
        Resource point = ResourceDirector.buildFaithPoint();

        assertEquals(0, track.victoryPointCell());
        assertEquals(VaticanSpace.NONE, track.vaticanSpaceCell());
        track.movePlayer(point);
        track.movePlayer(point);
        track.movePlayer(point);
        assertEquals(VaticanSpace.NONE, track.vaticanSpaceCell());
        assertEquals(1, track.victoryPointCell());
        track.movePlayer(point);
        track.movePlayer(point);
        track.movePlayer(point);
        assertEquals(VaticanSpace.FIRST, track.vaticanSpaceCell());
        assertEquals(2, track.victoryPointCell());
        track.movePlayer(point);
        track.movePlayer(point);
        assertEquals(VaticanSpace.FIRST, track.vaticanSpaceCell());
        assertEquals(0, track.victoryPointCell());

        Resource mid = ResourceDirector.buildFaithPoint(7);
        track.movePlayer(mid);
        assertEquals(VaticanSpace.SECOND, track.vaticanSpaceCell());
        assertEquals(9, track.victoryPointCell());

        Resource last = ResourceDirector.buildFaithPoint(9);
        track.movePlayer(last);
        assertEquals(VaticanSpace.THIRD, track.vaticanSpaceCell());
        assertEquals(20, track.victoryPointCell());

    }

    /**
     * Testing if the PlayerPosition is correctly updated
     * @throws NoMoreMovesException if the Player or Lorenzo moves when they are in the last cell
     */
    @Test
    public void checkPlayerPosition() throws NoMoreMovesException {

        Resource first = ResourceDirector.buildFaithPoint(1);
        Resource second = ResourceDirector.buildFaithPoint(2);
        Resource third = ResourceDirector.buildFaithPoint(3);
        Resource last = ResourceDirector.buildFaithPoint(18);

        FaithTrack faithTrack = new FaithTrack();

        assertEquals(0,faithTrack.getPlayerPosition());
        faithTrack.movePlayer(first);
        assertEquals(1,faithTrack.getPlayerPosition());
        faithTrack.movePlayer(first);
        assertEquals(2,faithTrack.getPlayerPosition());
        faithTrack.movePlayer(second);
        assertEquals(4,faithTrack.getPlayerPosition());
        faithTrack.movePlayer(first);
        assertEquals(5,faithTrack.getPlayerPosition());
        faithTrack.movePlayer(third);
        assertEquals(8,faithTrack.getPlayerPosition());
        faithTrack.movePlayer(last); //Player should be in the cell 26 but the Track is composed with 24 positions
        assertEquals(24,faithTrack.getPlayerPosition());
    }

    /**
     * Testing if the LorenzoPosition is correctly updated
     * @throws NoMoreMovesException if the Player or Lorenzo moves when they are in the last cell
     */
    @Test
    public void checkLorenzoPosition() throws NoMoreMovesException {

        FaithTrack faithTrack = new FaithTrack();

        faithTrack.moveLorenzo(2);
        assertEquals(2,faithTrack.getLorenzoPosition());
        faithTrack.moveLorenzo(1);
        assertEquals(3,faithTrack.getLorenzoPosition());
        faithTrack.moveLorenzo(5);
        assertEquals(8,faithTrack.getLorenzoPosition());
        faithTrack.moveLorenzo(1);
        assertEquals(9,faithTrack.getLorenzoPosition());
        faithTrack.moveLorenzo(5);
        assertEquals(14,faithTrack.getLorenzoPosition());
        faithTrack.moveLorenzo(12);
        assertEquals(24,faithTrack.getLorenzoPosition());
    }

    /**
     * Testing if the Tiles of the VaticanSpace are correctly flipped when the player moves
     * @throws NoMoreMovesException if the Player or Lorenzo moves when they are in the last cell
     */
    @Test
    public void flipPopeTiles() throws NoMoreMovesException {

        FaithTrack faithTrack = new FaithTrack();
        Resource third = ResourceDirector.buildFaithPoint(3);

        faithTrack.getTile(1);
        assertFalse(faithTrack.getTile(2));
        assertFalse(faithTrack.getTile(3));
        assertFalse(faithTrack.getTile(1));
        faithTrack.movePlayer(third);
        faithTrack.movePlayer(third);
        assertTrue(faithTrack.getTile(1));
        assertFalse(faithTrack.getTile(2));
        assertFalse(faithTrack.getTile(3));
        faithTrack.movePlayer(third);
        assertTrue(faithTrack.getTile(1));
        assertFalse(faithTrack.getTile(2));
        assertFalse(faithTrack.getTile(3));
        faithTrack.movePlayer(third);
        assertTrue(faithTrack.getTile(1));
        assertTrue(faithTrack.getTile(2));
        assertFalse(faithTrack.getTile(3));
        faithTrack.movePlayer(third);
        faithTrack.movePlayer(third);
        faithTrack.movePlayer(third);
        assertTrue(faithTrack.getTile(1));
        assertTrue(faithTrack.getTile(2));
        assertTrue(faithTrack.getTile(3));

    }

    /**
     * Testing if the model calls an exception when the player try to pass the last cell of the FaithTrack
     * @throws NoMoreMovesException if the Player or Lorenzo moves when they are in the last cell
     */
    @Test
    public void callExceptionPlayerMove() throws NoMoreMovesException{

        FaithTrack track = new FaithTrack();
        Resource first = ResourceDirector.buildFaithPoint(1);
        Resource last = ResourceDirector.buildFaithPoint(18);

        assertEquals(0,track.getPlayerPosition());
        track.movePlayer(first);
        track.movePlayer(last);
        track.movePlayer(last);
        assertEquals(24, track.getPlayerPosition());
        boolean result = false;

        try {
            track.movePlayer(first);
        } catch (NoMoreMovesException e) {
            result = true;
            System.out.println(e.getMsg());
        }
        assertTrue(result);
    }

    /**
     * Testing if the model calls an exception when Lorenzo try to pass the last cell of the FaithTrack
     * @throws NoMoreMovesException if the Player or Lorenzo moves when they are in the last cell
     */
    @Test
    public void callExceptionLorenzoMove() throws NoMoreMovesException{

        FaithTrack track = new FaithTrack();

        assertEquals(0,track.getLorenzoPosition());
        track.moveLorenzo(28);
        assertEquals(24, track.getLorenzoPosition());
        boolean result = false;

        try {
            track.moveLorenzo(1);
        } catch (NoMoreMovesException e) {
            result = true;
            System.out.println(e.getMsg());
        }
        assertTrue(result);
    }

}
