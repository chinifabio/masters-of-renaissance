package it.polimi.ingsw.matchTests;

import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.model.exceptions.PlayerStateException;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.exceptions.tray.OutOfBoundMarketTrayException;
import it.polimi.ingsw.model.exceptions.warehouse.UnobtainableResourceException;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.MarbleBuilder;
import it.polimi.ingsw.model.match.markettray.RowCol;
import it.polimi.ingsw.model.match.match.Match;
import it.polimi.ingsw.model.match.match.MultiplayerMatch;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MultiplayerMatchTest {

    private Match multiplayer;

    private Player gino;
    private Player lino;
    private Player pino;
    private Player mino;
    private Player dino;

    List<Player> order = new LinkedList<>();

    @BeforeEach
    public void initializeMatch() {
        this.multiplayer = new MultiplayerMatch();

        assertDoesNotThrow(()->gino = new Player("gino", this.multiplayer));
        assertDoesNotThrow(()->lino = new Player("lino", this.multiplayer));
        assertDoesNotThrow(()->pino = new Player("pino", this.multiplayer));
        assertDoesNotThrow(()->mino = new Player("mino", this.multiplayer));
        assertDoesNotThrow(()->dino = new Player("dino", this.multiplayer));

        assertDoesNotThrow(()->assertFalse(multiplayer.startGame()));

        assertTrue(multiplayer.playerJoin(gino));
        assertDoesNotThrow(()->assertFalse(multiplayer.startGame()));

        assertTrue(multiplayer.playerJoin(lino));
        assertTrue(multiplayer.playerJoin(pino));
        assertTrue(multiplayer.playerJoin(mino));

        assertFalse(multiplayer.playerJoin(dino));

        assertDoesNotThrow(()->assertTrue(multiplayer.startGame()));
        assertTrue(multiplayer.test_getTurn().getCurPlayer().canDoStuff());

        // creating a list of the players in order to have player(0) = inkwell player

        order.add(gino);
        order.add(lino);
        order.add(pino);
        order.add(mino);

        Collections.rotate(order, -order.indexOf(multiplayer.test_getTurn().getCurPlayer()));

        // discarding the leader cards
        for (int i = 0; i < order.size(); i++) {
            int in = i;
            assertDoesNotThrow(()->order.get(in).test_discardLeader());
            assertDoesNotThrow(()->order.get(in).test_discardLeader());
        }
    }

    @Test
    @RepeatedTest(5)
    public void endMatchByEndFaithTrack() {
        for(int i = 0; i < 24; i++ ) {
            try {
                order.get(0).obtainResource(MarbleBuilder.buildRed());
            } catch (UnobtainableResourceException e) {
                fail();
            }
        }

        assertDoesNotThrow(()->{
            order.get(1).endThisTurn();
            order.get(2).endThisTurn();
            order.get(3).endThisTurn();
        });

        assertFalse(multiplayer.test_getGameOnAir());
    }

    /**
     * create a single player match, a lorenzo player and some other test player.
     * Then try to do some operation to the match and with assertion check if the return value of it is right,
     * so you can know if the operation is succeed of failed.
     */
    @Test
    public void buildMultiplayerTest() throws OutOfBoundMarketTrayException, UnobtainableResourceException, IllegalTypeInProduction, PlayerStateException {

        assertTrue(multiplayer.test_getCurrPlayer().canDoStuff());

        for(int i = 0; i < this.multiplayer.test_getTurn().playerInGame(); i++){
            assertTrue(multiplayer.test_getCurrPlayer().useMarketTray(RowCol.COL, 2));
            assertFalse(multiplayer.test_getCurrPlayer().useMarketTray(RowCol.COL, 2));
            assertTrue(multiplayer.test_getCurrPlayer().endThisTurn());
        }

    }

    @Test
    public void turnTest() {

    }
}
