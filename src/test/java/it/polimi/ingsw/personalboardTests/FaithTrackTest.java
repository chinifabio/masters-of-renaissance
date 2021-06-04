package it.polimi.ingsw.personalboardTests;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.model.Dispatcher;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FaithTrackTest {
    Player player1;
    Player player2;

    Dispatcher view = new Dispatcher();
    Match game;

    List<Player> order = new ArrayList<>();

    @BeforeEach
    public void initialization() {
        try {
            game = new MultiplayerMatch(2, view);
        } catch (IOException e) {
            fail(e.getMessage());
        }

        assertDoesNotThrow(()->player1 = new Player("gino", game, view));
        assertTrue(game.playerJoin(player1));
        order.add(player1);

        assertDoesNotThrow(()->player2 = new Player("pino", game, view));
        assertTrue(game.playerJoin(player2));
        order.add(player2);

        Collections.rotate(order, order.indexOf(game.currentPlayer()));
        assertTrue(game.isGameOnAir());

        assertDoesNotThrow(()-> order.get(0).test_discardLeader());
        assertDoesNotThrow(()-> order.get(0).test_discardLeader());
        assertEquals(HeaderTypes.GAME_START, order.get(0).endThisTurn().header);

        assertEquals(HeaderTypes.OK, order.get(1).chooseResource(DepotSlot.BOTTOM, ResourceType.COIN).header);
        assertDoesNotThrow(()-> order.get(1).test_discardLeader());
        assertDoesNotThrow(()-> order.get(1).test_discardLeader());
        assertDoesNotThrow(() -> assertEquals(order.get(1).test_getPB().getDepots().get(DepotSlot.BOTTOM).viewResources().get(0), ResourceBuilder.buildCoin()));
        assertEquals(HeaderTypes.GAME_START, order.get(1).endThisTurn().header);
    }

    @Test
    public void infoFaithTrack() throws IOException {

        FaithTrack track = new FaithTrack(this.game.currentPlayer().view, this.game.currentPlayer().getNickname());
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
    public void checkPlayerPosition() throws EndGameException, IOException {

        Resource first = ResourceBuilder.buildFaithPoint(1);
        Resource second = ResourceBuilder.buildFaithPoint(2);
        Resource third = ResourceBuilder.buildFaithPoint(3);
        Resource last = ResourceBuilder.buildFaithPoint(20);

        FaithTrack faithTrack = new FaithTrack(this.game.currentPlayer().view, this.game.currentPlayer().getNickname());

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
    public void callExceptionsPlayer() throws IOException {
        FaithTrack track = new FaithTrack(this.game.currentPlayer().view, this.game.currentPlayer().getNickname());

        assertDoesNotThrow(()->{
            track.movePlayer(8, game);
            track.movePlayer(8, game);
            track.movePlayer(7, game);
        });
        assertEquals(23, track.getPlayerPosition());

        try {
            track.movePlayer(2, game);
        } catch (EndGameException ignore) {}
    }

    @Test
    public void flipOtherPopeTile() {

        List<Player> order = new ArrayList<>();

        assertDoesNotThrow(()->{
            game.currentPlayer().moveFaithMarker(4);
            order.add(game.currentPlayer());
            game.currentPlayer().test_endTurnNoMain();

            game.currentPlayer().moveFaithMarker(9);
            order.add(game.currentPlayer());
            game.currentPlayer().test_endTurnNoMain();

            assertFalse(game.currentPlayer().getFT_forTest().isFlipped(VaticanSpace.FIRST));
            game.currentPlayer().test_endTurnNoMain();
            assertTrue(game.currentPlayer().getFT_forTest().isFlipped(VaticanSpace.FIRST));
            game.currentPlayer().test_endTurnNoMain();

            game.currentPlayer().moveFaithMarker(8);
            game.currentPlayer().test_endTurnNoMain();

            game.currentPlayer().moveFaithMarker(7);
            game.currentPlayer().test_endTurnNoMain();

            assertTrue(game.currentPlayer().getFT_forTest().isFlipped(VaticanSpace.SECOND));
            game.currentPlayer().test_endTurnNoMain();
            assertTrue(game.currentPlayer().getFT_forTest().isFlipped(VaticanSpace.SECOND));

            try{
                game.currentPlayer().moveFaithMarker(20);
            } catch (Exception ignore) {}

            assertFalse(order.get(0).getFT_forTest().isFlipped(VaticanSpace.THIRD));
            assertTrue(order.get(1).getFT_forTest().isFlipped(VaticanSpace.THIRD));
        });

    }

    @Test
    public void countingPoints() {
        assertDoesNotThrow(()->{
            assertEquals(0, game.currentPlayer().getFT_forTest().countingFaithTrackVictoryPoints()); //FIRST PLAYER
            game.currentPlayer().test_endTurnNoMain();
            assertEquals(0, game.currentPlayer().getFT_forTest().countingFaithTrackVictoryPoints()); //SECOND PLAYER
            game.currentPlayer().test_endTurnNoMain();

            game.currentPlayer().moveFaithMarker(4);
            game.currentPlayer().test_endTurnNoMain();

            game.currentPlayer().moveFaithMarker(2);
            game.currentPlayer().test_endTurnNoMain();

            assertEquals(1, game.currentPlayer().getFT_forTest().countingFaithTrackVictoryPoints()); //FIRST PLAYER
            game.currentPlayer().test_endTurnNoMain();
            assertEquals(0, game.currentPlayer().getFT_forTest().countingFaithTrackVictoryPoints()); //SECOND PLAYER
            game.currentPlayer().test_endTurnNoMain();

            game.currentPlayer().moveFaithMarker(3);
            game.currentPlayer().test_endTurnNoMain();

            game.currentPlayer().moveFaithMarker(4);
            game.currentPlayer().test_endTurnNoMain();

            assertEquals(2, game.currentPlayer().getFT_forTest().countingFaithTrackVictoryPoints()); //FIRST PLAYER
            game.currentPlayer().test_endTurnNoMain();
            assertEquals(2, game.currentPlayer().getFT_forTest().countingFaithTrackVictoryPoints()); //SECOND PLAYER
            game.currentPlayer().test_endTurnNoMain();


            game.currentPlayer().moveFaithMarker(2);
            game.currentPlayer().test_endTurnNoMain();

            game.currentPlayer().moveFaithMarker(1);
            game.currentPlayer().test_endTurnNoMain();

            assertEquals(4 + 2, game.currentPlayer().getFT_forTest().countingFaithTrackVictoryPoints()); //FIRST PLAYER
            game.currentPlayer().test_endTurnNoMain();
            assertEquals(2 + 2, game.currentPlayer().getFT_forTest().countingFaithTrackVictoryPoints()); //SECOND PLAYER
            game.currentPlayer().test_endTurnNoMain();


            game.currentPlayer().moveFaithMarker(10); //Player1: 19
            game.currentPlayer().test_endTurnNoMain();
            //When the first player reach the second PopeSpace, the second player is in the Cell 7 so he doesn't obtain the
            //victoryPoints of the second PopeTile
            game.currentPlayer().moveFaithMarker(10); //Player2: 17
            game.currentPlayer().test_endTurnNoMain();

            assertEquals(12 + 3 + 2, game.currentPlayer().getFT_forTest().countingFaithTrackVictoryPoints()); //FIRST PLAYER
            game.currentPlayer().test_endTurnNoMain();
            assertEquals(9 + 2, game.currentPlayer().getFT_forTest().countingFaithTrackVictoryPoints()); //SECOND PLAYER
            game.currentPlayer().test_endTurnNoMain();

            game.currentPlayer().moveFaithMarker(1);//Player1: 20
            game.currentPlayer().test_endTurnNoMain();
        });

        try {
            game.currentPlayer().moveFaithMarker(8);//Player2: 24
        } catch (EndGameException ignore) {}


        assertEquals(12 + 4 + 3 + 2, order.get(0).getFT_forTest().countingFaithTrackVictoryPoints()); //FIRST PLAYER
        assertEquals(20 + 4 + 2, order.get(1).getFT_forTest().countingFaithTrackVictoryPoints()); //SECOND PLAYER
    }
}
