package it.polimi.ingsw.personalboardTests;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.CustomAssertion;
import it.polimi.ingsw.model.exceptions.PlayerStateException;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.exceptions.warehouse.WrongDepotException;
import it.polimi.ingsw.model.match.match.Match;
import it.polimi.ingsw.model.match.match.MultiplayerMatch;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.*;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FaithTrackTest {

    Match game;

    Player player1;
    Player player2;

    @BeforeEach
    public void initialization() {
        game = new MultiplayerMatch();

        assertDoesNotThrow(()->player1 = new Player("uno", game));
        assertDoesNotThrow(()->player2 = new Player("due", game));

        assertTrue(game.playerJoin(player1));
        assertTrue(game.playerJoin(player2));

        assertDoesNotThrow(()-> game.startGame());

        assertDoesNotThrow(()-> game.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> game.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> game.test_getCurrPlayer().endThisTurn());

        assertDoesNotThrow(()-> game.test_getCurrPlayer().chooseResource(ResourceType.COIN));
        assertDoesNotThrow(()-> game.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> game.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(() -> assertTrue(game.test_getCurrPlayer().test_getPB().test_getDepots().get(DepotSlot.STRONGBOX).viewAllResources().contains(ResourceBuilder.buildCoin())));
        assertDoesNotThrow(()-> game.test_getCurrPlayer().endThisTurn());
    }

    @Test
    public void infoFaithTrack() {

        FaithTrack track = new FaithTrack();
        Resource point = ResourceBuilder.buildFaithPoint();

        assertDoesNotThrow(()->{
            assertEquals(0, track.victoryPointCellPlayer());
            assertEquals(VaticanSpace.NONE, track.vaticanSpaceCell());
            track.movePlayer(point.amount(), game);
            track.movePlayer(point.amount(), game);
            track.movePlayer(point.amount(), game);
            assertEquals(VaticanSpace.NONE, track.vaticanSpaceCell());
            assertEquals(1, track.victoryPointCellPlayer());
            track.movePlayer(point.amount(), game);
            track.movePlayer(point.amount(), game);
            track.movePlayer(point.amount(), game);
            assertEquals(VaticanSpace.FIRST, track.vaticanSpaceCell());
            assertEquals(2, track.victoryPointCellPlayer());
            track.movePlayer(point.amount(), game);
            track.movePlayer(point.amount(), game);
            assertEquals(VaticanSpace.FIRST, track.vaticanSpaceCell());
            assertEquals(0, track.victoryPointCellPlayer());

            Resource mid = ResourceBuilder.buildFaithPoint(7);
            track.movePlayer(mid.amount(), game);
            assertEquals(VaticanSpace.SECOND, track.vaticanSpaceCell());
            assertEquals(9, track.victoryPointCellPlayer());
        });

        Resource last = ResourceBuilder.buildFaithPoint(9);
        try {
            track.movePlayer(last.amount(), game);
            fail();
        } catch (EndGameException ignore){}

        assertEquals(VaticanSpace.THIRD, track.vaticanSpaceCell());
        assertEquals(20, track.victoryPointCellPlayer());

    }

    @Test
    public void checkPlayerPosition() throws EndGameException {

        Resource first = ResourceBuilder.buildFaithPoint(1);
        Resource second = ResourceBuilder.buildFaithPoint(2);
        Resource third = ResourceBuilder.buildFaithPoint(3);
        Resource last = ResourceBuilder.buildFaithPoint(20);

        FaithTrack faithTrack = new FaithTrack();

        assertEquals(0,faithTrack.getPlayerPosition());
        faithTrack.movePlayer(first.amount(), game);
        assertEquals(1,faithTrack.getPlayerPosition());
        faithTrack.movePlayer(first.amount(), game);
        assertEquals(2,faithTrack.getPlayerPosition());
        faithTrack.movePlayer(second.amount(), game);
        assertEquals(4,faithTrack.getPlayerPosition());
        faithTrack.movePlayer(first.amount(), game);
        assertEquals(5,faithTrack.getPlayerPosition());
        faithTrack.movePlayer(third.amount(), game);
        assertEquals(8,faithTrack.getPlayerPosition());
        try {
            faithTrack.movePlayer(last.amount(), game);
            fail();
        } catch (EndGameException e) {
            e.printStackTrace();//Player should be in the cell 28 but the Track is composed with 24 positions
        }
        assertEquals(24,faithTrack.getPlayerPosition());
    }



    /**
     * Testing if the model calls an exception when the player try to pass the last cell of the FaithTrack
     */
    @Test
    public void callExceptionsPlayer() {
        FaithTrack track = new FaithTrack();

        assertDoesNotThrow(()->{
            track.movePlayer(8, game);
            track.movePlayer(8, game);
            track.movePlayer(7, game);
        });
        assertEquals(23, track.getPlayerPosition());

        CustomAssertion.assertThrown(()->track.movePlayer(2, game));
    }

    @Test
    public void flipOtherPopeTile() {

        List<Player> order = new ArrayList<>();
        if (player1.canDoStuff()){
            order.add(player1);
            order.add(player2);
        } else {
            order.add(player2);
            order.add(player1);
        }

        assertDoesNotThrow(()->{
            game.test_getCurrPlayer().moveFaithMarker(4);
            game.test_getCurrPlayer().endThisTurn();

            game.test_getCurrPlayer().moveFaithMarker(9);
            game.test_getCurrPlayer().endThisTurn();

            assertFalse(game.test_getCurrPlayer().getFT_forTest().isFlipped(VaticanSpace.FIRST));
            game.test_getCurrPlayer().endThisTurn();
            assertTrue(game.test_getCurrPlayer().getFT_forTest().isFlipped(VaticanSpace.FIRST));
            game.test_getCurrPlayer().endThisTurn();

            game.test_getCurrPlayer().moveFaithMarker(8);
            game.test_getCurrPlayer().endThisTurn();

            game.test_getCurrPlayer().moveFaithMarker(7);
            game.test_getCurrPlayer().endThisTurn();

            assertTrue(game.test_getCurrPlayer().getFT_forTest().isFlipped(VaticanSpace.SECOND));
            game.test_getCurrPlayer().endThisTurn();
            assertTrue(game.test_getCurrPlayer().getFT_forTest().isFlipped(VaticanSpace.SECOND));

            CustomAssertion.assertThrown(()-> game.test_getCurrPlayer().moveFaithMarker(20));
            // the game is ended

            assertFalse(order.get(0).getFT_forTest().isFlipped(VaticanSpace.THIRD));
            assertTrue(order.get(1).getFT_forTest().isFlipped(VaticanSpace.THIRD));
        });

    }

    @Test
    public void countingPoints() throws PlayerStateException, WrongDepotException {

        //starting the game
        List<Player> orderList = new ArrayList<>();
        orderList.add(player1);
        orderList.add(player2);
        Collections.rotate(orderList, -orderList.indexOf(orderList.stream().filter(Player :: canDoStuff).findAny().get()));



        assertEquals(0, game.test_getCurrPlayer().getFT_forTest().countingFaithTrackVictoryPoints()); //FIRST PLAYER
        game.test_getCurrPlayer().endThisTurn();
        assertEquals(0, game.test_getCurrPlayer().getFT_forTest().countingFaithTrackVictoryPoints()); //SECOND PLAYER
        game.test_getCurrPlayer().endThisTurn();

        game.test_getCurrPlayer().moveFaithMarker(4);
        game.test_getCurrPlayer().endThisTurn();

        game.test_getCurrPlayer().moveFaithMarker(2);
        game.test_getCurrPlayer().endThisTurn();

        assertEquals(1, game.test_getCurrPlayer().getFT_forTest().countingFaithTrackVictoryPoints()); //FIRST PLAYER
        game.test_getCurrPlayer().endThisTurn();
        assertEquals(0, game.test_getCurrPlayer().getFT_forTest().countingFaithTrackVictoryPoints()); //SECOND PLAYER
        game.test_getCurrPlayer().endThisTurn();

        game.test_getCurrPlayer().moveFaithMarker(3);
        game.test_getCurrPlayer().endThisTurn();

        game.test_getCurrPlayer().moveFaithMarker(4);
        game.test_getCurrPlayer().endThisTurn();

        assertEquals(2, game.test_getCurrPlayer().getFT_forTest().countingFaithTrackVictoryPoints()); //FIRST PLAYER
        game.test_getCurrPlayer().endThisTurn();
        assertEquals(2, game.test_getCurrPlayer().getFT_forTest().countingFaithTrackVictoryPoints()); //SECOND PLAYER
        game.test_getCurrPlayer().endThisTurn();


        game.test_getCurrPlayer().moveFaithMarker(2);
        game.test_getCurrPlayer().endThisTurn();

        game.test_getCurrPlayer().moveFaithMarker(1);
        game.test_getCurrPlayer().endThisTurn();

        assertEquals(4 + 2, game.test_getCurrPlayer().getFT_forTest().countingFaithTrackVictoryPoints()); //FIRST PLAYER
        game.test_getCurrPlayer().endThisTurn();
        assertEquals(2 + 2, game.test_getCurrPlayer().getFT_forTest().countingFaithTrackVictoryPoints()); //SECOND PLAYER
        game.test_getCurrPlayer().endThisTurn();


        game.test_getCurrPlayer().moveFaithMarker(10); //Player1: 19
        game.test_getCurrPlayer().endThisTurn();
        //When the first player reach the second PopeSpace, the second player is in the Cell 7 so he doesn't obtain the
        //victoryPoints of the second PopeTile
        game.test_getCurrPlayer().moveFaithMarker(10); //Player2: 17
        game.test_getCurrPlayer().endThisTurn();

        assertEquals(12 + 3 + 2, game.test_getCurrPlayer().getFT_forTest().countingFaithTrackVictoryPoints()); //FIRST PLAYER
        game.test_getCurrPlayer().endThisTurn();
        assertEquals(9 + 2, game.test_getCurrPlayer().getFT_forTest().countingFaithTrackVictoryPoints()); //SECOND PLAYER
        game.test_getCurrPlayer().endThisTurn();

        game.test_getCurrPlayer().moveFaithMarker(1);//Player1: 20
        game.test_getCurrPlayer().endThisTurn();

        game.test_getCurrPlayer().moveFaithMarker(8);//Player2: 24


        assertEquals(12 + 4 + 3 + 2, orderList.get(0).getFT_forTest().countingFaithTrackVictoryPoints()); //FIRST PLAYER
        assertEquals(20 + 4 + 2, orderList.get(1).getFT_forTest().countingFaithTrackVictoryPoints()); //SECOND PLAYER
    }
}
