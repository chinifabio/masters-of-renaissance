package it.polimi.ingsw;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.exceptions.WrongPointsException;
import it.polimi.ingsw.model.exceptions.IllegalMovesException;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.*;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import org.junit.jupiter.api.Test;

public class FaithTrackTest {

    /**
     * Testing if the FaithTrack is correctly created using the player position to take cells info
     * @throws IllegalMovesException if the Player or Lorenzo moves when they are in the last cell
     * @throws WrongPointsException if the Player or Lorenzo receives negative points
     */
    @Test
    public void infoFaithTrack() throws IllegalMovesException, WrongPointsException {

        FaithTrack track = new FaithTrack();
        Resource point = ResourceBuilder.buildFaithPoint();

        assertEquals(0, track.victoryPointCell());
        assertEquals(VaticanSpace.NONE, track.vaticanSpaceCell());
        track.movePlayer(point.amount());
        track.movePlayer(point.amount());
        track.movePlayer(point.amount());
        assertEquals(VaticanSpace.NONE, track.vaticanSpaceCell());
        assertEquals(1, track.victoryPointCell());
        track.movePlayer(point.amount());
        track.movePlayer(point.amount());
        track.movePlayer(point.amount());
        assertEquals(VaticanSpace.FIRST, track.vaticanSpaceCell());
        assertEquals(2, track.victoryPointCell());
        track.movePlayer(point.amount());
        track.movePlayer(point.amount());
        assertEquals(VaticanSpace.FIRST, track.vaticanSpaceCell());
        assertEquals(0, track.victoryPointCell());

        Resource mid = ResourceBuilder.buildFaithPoint(7);
        track.movePlayer(mid.amount());
        assertEquals(VaticanSpace.SECOND, track.vaticanSpaceCell());
        assertEquals(9, track.victoryPointCell());

        Resource last = ResourceBuilder.buildFaithPoint(9);
        track.movePlayer(last.amount());
        assertEquals(VaticanSpace.THIRD, track.vaticanSpaceCell());
        assertEquals(20, track.victoryPointCell());

    }

    /**
     * Testing if the PlayerPosition is correctly updated
     * @throws IllegalMovesException if the Player or Lorenzo moves when they are in the last cell
     * @throws WrongPointsException if the Player or Lorenzo receives negative points
     */
    @Test
    public void checkPlayerPosition() throws IllegalMovesException, WrongPointsException {

        Resource first = ResourceBuilder.buildFaithPoint(1);
        Resource second = ResourceBuilder.buildFaithPoint(2);
        Resource third = ResourceBuilder.buildFaithPoint(3);
        Resource last = ResourceBuilder.buildFaithPoint(20);

        FaithTrack faithTrack = new FaithTrack();

        assertEquals(0,faithTrack.getPlayerPosition());
        faithTrack.movePlayer(first.amount());
        assertEquals(1,faithTrack.getPlayerPosition());
        faithTrack.movePlayer(first.amount());
        assertEquals(2,faithTrack.getPlayerPosition());
        faithTrack.movePlayer(second.amount());
        assertEquals(4,faithTrack.getPlayerPosition());
        faithTrack.movePlayer(first.amount());
        assertEquals(5,faithTrack.getPlayerPosition());
        faithTrack.movePlayer(third.amount());
        assertEquals(8,faithTrack.getPlayerPosition());
        faithTrack.movePlayer(last.amount()); //Player should be in the cell 28 but the Track is composed with 24 positions
        assertEquals(24,faithTrack.getPlayerPosition());
    }

    /**
     * Testing if the LorenzoPosition is correctly updated
     * @throws IllegalMovesException if the Player or Lorenzo moves when they are in the last cell
     * @throws WrongPointsException if the Player or Lorenzo receives negative points
     */
    @Test
    public void checkLorenzoPosition() throws IllegalMovesException, WrongPointsException {

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

    //**
    // * Testing if the Tiles of the VaticanSpace are correctly flipped when the player moves
    // * @throws IllegalMovesException if the Player or Lorenzo moves when they are in the last cell
    // * @throws WrongPointsException if the Player or Lorenzo receives negative points
     //*/
   // @Test
  // public void flipPopeTiles() throws IllegalMovesException, WrongPointsException {

  //     FaithTrack faithTrack = new FaithTrack();
  //     Resource third = ResourceBuilder.buildFaithPoint(3);

  //     faithTrack.getTile(1);
  //     assertFalse(faithTrack.getTile(2));
  //     assertFalse(faithTrack.getTile(3));
  //     assertFalse(faithTrack.getTile(1));
  //     faithTrack.movePlayer(third.amount());
  //     faithTrack.movePlayer(third.amount());
  //     assertTrue(faithTrack.getTile(1));
  //     assertFalse(faithTrack.getTile(2));
  //     assertFalse(faithTrack.getTile(3));
  //     faithTrack.movePlayer(third.amount());
  //     assertTrue(faithTrack.getTile(1));
  //     assertFalse(faithTrack.getTile(2));
  //     assertFalse(faithTrack.getTile(3));
  //     faithTrack.movePlayer(third.amount());
  //     assertTrue(faithTrack.getTile(1));
  //     assertTrue(faithTrack.getTile(2));
  //     assertFalse(faithTrack.getTile(3));
  //     faithTrack.movePlayer(third.amount());
  //     faithTrack.movePlayer(third.amount());
  //     faithTrack.movePlayer(third.amount());
  //     assertTrue(faithTrack.getTile(1));
  //     assertTrue(faithTrack.getTile(2));
  //     assertTrue(faithTrack.getTile(3));

  // }

    /**
     * Testing if the model calls an exception when the player try to pass the last cell of the FaithTrack
     * @throws IllegalMovesException if the Player or Lorenzo moves when they are in the last cell
     * @throws WrongPointsException if the Player or Lorenzo receives negative points
     */
    @Test
    public void callExceptionsPlayer() throws IllegalMovesException, WrongPointsException {

        FaithTrack track = new FaithTrack();
        Resource negative = ResourceBuilder.buildFaithPoint(-3);
        Resource first = ResourceBuilder.buildFaithPoint(1);
        Resource last = ResourceBuilder.buildFaithPoint(18);
        Resource error = ResourceBuilder.buildServant(3);

        assertEquals(0,track.getPlayerPosition());
        track.movePlayer(first.amount());
        track.movePlayer(last.amount());
        track.movePlayer(last.amount());
        assertEquals(24, track.getPlayerPosition());
        boolean move = false;
        boolean result = false;
        boolean otherRes = false;

        try {
            track.movePlayer(first.amount());
        } catch (IllegalMovesException e) {
            move = true;
            System.out.println(e.getMsg());
        }
        assertTrue(move);

        try {
            track.movePlayer(negative.amount());
        } catch (WrongPointsException e) {
            result = true;
            System.out.println(e.getMsg());
        }
        assertTrue(result);

        try{
            track.movePlayer(error.amount());
        } catch (IllegalMovesException e){
            otherRes = true;
        }
        assertTrue(otherRes);
    }

    /**
     * Testing if the model calls an exception when Lorenzo try to pass the last cell of the FaithTrack
     * @throws IllegalMovesException if the Player or Lorenzo moves when they are in the last cell
     * @throws WrongPointsException if the Player or Lorenzo receives negative points
     */
    @Test
    public void callExceptionsLorenzo() throws IllegalMovesException, WrongPointsException {

        FaithTrack track = new FaithTrack();

        assertEquals(0,track.getLorenzoPosition());
        track.moveLorenzo(28);
        assertEquals(24, track.getLorenzoPosition());
        boolean move = false;
        boolean negative = false;

        try {
            track.moveLorenzo(1);
        } catch (IllegalMovesException e) {
            move = true;
            System.out.println(e.getMsg());
        }
        assertTrue(move);

        try {
            track.moveLorenzo(-2);
        } catch (WrongPointsException e) {
            negative = true;
            System.out.println(e.getMsg());
        }
        assertTrue(negative);
    }

}
