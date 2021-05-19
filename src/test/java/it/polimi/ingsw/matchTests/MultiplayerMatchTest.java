package it.polimi.ingsw.matchTests;

import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.model.Dispatcher;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MultiplayerMatchTest {
    Player pino;
    Player gino;
    Player dino;
    Player tino;

    List<Player> order = new ArrayList<>();

    Dispatcher view = new Dispatcher();
    private final Match multiplayer = new MultiplayerMatch(4, view);

    @BeforeEach
    public void initializeMatch() throws IllegalTypeInProduction {
        pino = new Player("pino", multiplayer, view);
        assertTrue(multiplayer.playerJoin(pino));
        order.add(pino);

        gino = new Player("gino", multiplayer, view);
        assertTrue(multiplayer.playerJoin(gino));
        order.add(gino);

        dino = new Player("dino", multiplayer, view);
        assertTrue(multiplayer.playerJoin(dino));
        order.add(dino);

        tino = new Player("tino", multiplayer, view);
        assertTrue(multiplayer.playerJoin(tino));
        order.add(tino);

        Collections.rotate(order, -order.indexOf(multiplayer.test_getCurrPlayer()));

        // initializing player section
        /*
        1st - 0 resource to choose and 0 faith points
        2nd - 1 resource to choose and 0 faith points
        3rd - 1 resource to choose and 1 faith points
        4th - 2 resource to choose and 1 faith points
         */

        assertDoesNotThrow(()-> order.get(0).test_discardLeader());
        assertDoesNotThrow(()-> order.get(0).test_discardLeader());
        Packet p = order.get(0).endThisTurn();
        System.out.println(p.body);
        assertEquals(HeaderTypes.END_TURN, p.header);

        assertDoesNotThrow(()-> order.get(1).chooseResource(DepotSlot.BOTTOM, ResourceType.COIN));
        assertDoesNotThrow(()-> order.get(1).test_discardLeader());
        assertDoesNotThrow(()-> order.get(1).test_discardLeader());
        assertDoesNotThrow(() -> assertEquals(order.get(1).test_getPB().getDepots().get(DepotSlot.BOTTOM).viewResources().get(0), ResourceBuilder.buildCoin()));
        assertEquals(HeaderTypes.END_TURN, order.get(1).endThisTurn().header);

        assertDoesNotThrow(()-> order.get(2).chooseResource(DepotSlot.BOTTOM, ResourceType.COIN));
        assertDoesNotThrow(()-> order.get(2).test_discardLeader());
        assertDoesNotThrow(()-> order.get(2).test_discardLeader());
        assertEquals(1, order.get(2).test_getPB().getFT_forTest().getPlayerPosition());
        assertDoesNotThrow(() -> assertEquals(order.get(2).test_getPB().getDepots().get(DepotSlot.BOTTOM).viewResources().get(0), ResourceBuilder.buildCoin()));
        assertEquals(HeaderTypes.END_TURN, order.get(2).endThisTurn().header);

        assertDoesNotThrow(()-> assertEquals(HeaderTypes.OK, order.get(3).chooseResource(DepotSlot.BOTTOM, ResourceType.COIN).header));
        assertDoesNotThrow(()-> assertEquals(HeaderTypes.INVALID, order.get(3).chooseResource(DepotSlot.BOTTOM, ResourceType.STONE).header));
        assertDoesNotThrow(()-> assertEquals(HeaderTypes.OK, order.get(3).chooseResource(DepotSlot.MIDDLE, ResourceType.STONE).header));
        assertDoesNotThrow(()-> order.get(3).test_discardLeader());
        assertDoesNotThrow(()-> order.get(3).test_discardLeader());
        assertEquals(1, order.get(3).test_getPB().getFT_forTest().getPlayerPosition());
        assertDoesNotThrow(() -> assertEquals(order.get(3).test_getPB().getDepots().get(DepotSlot.BOTTOM).viewResources().get(0), ResourceBuilder.buildCoin()));
        assertDoesNotThrow(() -> assertEquals(order.get(3).test_getPB().getDepots().get(DepotSlot.MIDDLE).viewResources().get(0), ResourceBuilder.buildStone()));
        assertEquals(HeaderTypes.END_TURN, order.get(3).endThisTurn().header);

        // creating a list of the players in order to have player(0) = inkwell player
        Collections.rotate(order, -order.indexOf(multiplayer.test_getTurn().getCurPlayer()));
    }

    @RepeatedTest(5)
    public void endMatchByEndFaithTrack() {
        for(int i = 0; i < 24; i++ ) {
            try {
                order.get(0).obtainResource(DepotSlot.STRONGBOX, MarbleBuilder.buildRed().toResource());
            } catch (EndGameException e) {
                multiplayer.startEndGameLogic();
                assertEquals(HeaderTypes.END_TURN, order.get(0).endThisTurn().header);
            }
            catch (Exception e) {fail();}
        }

        assertEquals(HeaderTypes.END_TURN, order.get(1).endThisTurn().header);
        assertEquals(HeaderTypes.END_TURN, order.get(2).endThisTurn().header);
        assertEquals(HeaderTypes.END_TURN, order.get(3).endThisTurn().header);

        assertFalse(multiplayer.test_getGameOnAir());
    }

    /**
     * create a single player match, a lorenzo player and some other test player.
     * Then try to do some operation to the match and with assertion check if the return value of it is right,
     * so you can know if the operation is succeed of failed.
     */
    @Test
    public void buildMultiplayerTest() {
        for(int i = 0; i < this.multiplayer.test_getTurn().playerInGame(); i++){
            assertEquals(order.get(i).useMarketTray(RowCol.COL, 2).header, HeaderTypes.OK);
            assertEquals(order.get(i).useMarketTray(RowCol.COL, 2).header, HeaderTypes.INVALID);
            assertEquals(order.get(i).endThisTurn().header, HeaderTypes.END_TURN);
        }
    }
}
