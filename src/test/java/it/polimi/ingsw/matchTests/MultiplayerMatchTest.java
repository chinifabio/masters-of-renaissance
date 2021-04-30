package it.polimi.ingsw.matchTests;

import it.polimi.ingsw.model.exceptions.warehouse.WrongDepotException;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.MarbleBuilder;
import it.polimi.ingsw.model.match.markettray.RowCol;
import it.polimi.ingsw.model.match.match.Match;
import it.polimi.ingsw.model.match.match.MultiplayerMatch;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;
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

        // initializing player section
        /*
        1st - 0 resource to choose and 0 faith points
        2nd - 1 resource to choose and 0 faith points
        3rd - 1 resource to choose and 1 faith points
        4th - 2 resource to choose and 1 faith points
         */
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().endThisTurn());

        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().chooseResource(DepotSlot.BOTTOM, ResourceType.COIN));
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(() -> assertEquals(multiplayer.test_getCurrPlayer().test_getPB().test_getDepots().get(DepotSlot.BOTTOM).viewResources().get(0), ResourceBuilder.buildCoin()));
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().endThisTurn());

        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().chooseResource(DepotSlot.BOTTOM, ResourceType.COIN));
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().test_discardLeader());
        assertEquals(1, multiplayer.test_getCurrPlayer().test_getPB().getFT_forTest().getPlayerPosition());
        assertDoesNotThrow(() -> assertEquals(multiplayer.test_getCurrPlayer().test_getPB().test_getDepots().get(DepotSlot.BOTTOM).viewResources().get(0), ResourceBuilder.buildCoin()));
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().endThisTurn());

        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().chooseResource(DepotSlot.BOTTOM, ResourceType.COIN));
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().chooseResource(DepotSlot.BOTTOM, ResourceType.STONE));
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().chooseResource(DepotSlot.MIDDLE, ResourceType.STONE));
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().test_discardLeader());
        assertEquals(1, multiplayer.test_getCurrPlayer().test_getPB().getFT_forTest().getPlayerPosition());
        assertDoesNotThrow(() -> assertEquals(multiplayer.test_getCurrPlayer().test_getPB().test_getDepots().get(DepotSlot.BOTTOM).viewResources().get(0), ResourceBuilder.buildCoin()));
        assertDoesNotThrow(() -> assertEquals(multiplayer.test_getCurrPlayer().test_getPB().test_getDepots().get(DepotSlot.MIDDLE).viewResources().get(0), ResourceBuilder.buildStone()));
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().endThisTurn());

        // creating a list of the players in order to have player(0) = inkwell player

        order.add(gino);
        order.add(lino);
        order.add(pino);
        order.add(mino);

        Collections.rotate(order, -order.indexOf(multiplayer.test_getTurn().getCurPlayer()));
    }

    @RepeatedTest(5)
    public void endMatchByEndFaithTrack() throws WrongDepotException {
        for(int i = 0; i < 24; i++ ) {
            order.get(0).obtainResource(DepotSlot.STRONGBOX, MarbleBuilder.buildRed().toResource());
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
    public void buildMultiplayerTest() {

        assertTrue(multiplayer.test_getCurrPlayer().canDoStuff());

        for(int i = 0; i < this.multiplayer.test_getTurn().playerInGame(); i++){
            assertDoesNotThrow(()->assertTrue(multiplayer.test_getCurrPlayer().useMarketTray(RowCol.COL, 2)));
            assertDoesNotThrow(()->assertFalse(multiplayer.test_getCurrPlayer().useMarketTray(RowCol.COL, 2)));
            assertDoesNotThrow(()->assertTrue(multiplayer.test_getCurrPlayer().endThisTurn()));
        }

    }
}
