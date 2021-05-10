package it.polimi.ingsw.matchTests;

import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.model.VirtualView;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.exceptions.warehouse.UnobtainableResourceException;
import it.polimi.ingsw.model.exceptions.warehouse.WrongDepotException;
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

    private Match multiplayer = new MultiplayerMatch(4);

    Player pino;
    Player gino;
    Player dino;
    Player tino;

    List<Player> order = new ArrayList<>();

    VirtualView view = new VirtualView();

    @BeforeEach
    public void initializeMatch() throws IllegalTypeInProduction {
        pino = new Player("pino", multiplayer, view);
        assertTrue(multiplayer.playerJoin(pino));

        gino = new Player("gino", multiplayer, view);
        assertTrue(multiplayer.playerJoin(gino));

        dino = new Player("dino", multiplayer, view);
        assertTrue(multiplayer.playerJoin(dino));

        tino = new Player("tino", multiplayer, view);
        assertTrue(multiplayer.playerJoin(tino));

        assertTrue(multiplayer.startGame());

        // initializing player section
        /*
        1st - 0 resource to choose and 0 faith points
        2nd - 1 resource to choose and 0 faith points
        3rd - 1 resource to choose and 1 faith points
        4th - 2 resource to choose and 1 faith points
         */

        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().test_discardLeader());
        this.order.add(multiplayer.test_getCurrPlayer());
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().endThisTurn());

        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().chooseResource(DepotSlot.BOTTOM, ResourceType.COIN));
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(() -> assertEquals(multiplayer.test_getCurrPlayer().test_getPB().test_getDepots().get(DepotSlot.BOTTOM).viewResources().get(0), ResourceBuilder.buildCoin()));
        this.order.add(multiplayer.test_getCurrPlayer());
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().endThisTurn());

        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().chooseResource(DepotSlot.BOTTOM, ResourceType.COIN));
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().test_discardLeader());
        assertEquals(1, multiplayer.test_getCurrPlayer().test_getPB().getFT_forTest().getPlayerPosition());
        assertDoesNotThrow(() -> assertEquals(multiplayer.test_getCurrPlayer().test_getPB().test_getDepots().get(DepotSlot.BOTTOM).viewResources().get(0), ResourceBuilder.buildCoin()));
        this.order.add(multiplayer.test_getCurrPlayer());
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().endThisTurn());

        assertDoesNotThrow(()-> assertEquals(HeaderTypes.OK, multiplayer.test_getCurrPlayer().chooseResource(DepotSlot.BOTTOM, ResourceType.COIN).header));
        assertDoesNotThrow(()-> assertEquals(HeaderTypes.INVALID, multiplayer.test_getCurrPlayer().chooseResource(DepotSlot.BOTTOM, ResourceType.STONE).header));
        assertDoesNotThrow(()-> assertEquals(HeaderTypes.OK, multiplayer.test_getCurrPlayer().chooseResource(DepotSlot.MIDDLE, ResourceType.STONE).header));
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().test_discardLeader());
        assertEquals(1, multiplayer.test_getCurrPlayer().test_getPB().getFT_forTest().getPlayerPosition());
        assertDoesNotThrow(() -> assertEquals(multiplayer.test_getCurrPlayer().test_getPB().test_getDepots().get(DepotSlot.BOTTOM).viewResources().get(0), ResourceBuilder.buildCoin()));
        assertDoesNotThrow(() -> assertEquals(multiplayer.test_getCurrPlayer().test_getPB().test_getDepots().get(DepotSlot.MIDDLE).viewResources().get(0), ResourceBuilder.buildStone()));
        this.order.add(multiplayer.test_getCurrPlayer());
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().endThisTurn());

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
            catch (WrongDepotException ignore) {fail();}
            catch (UnobtainableResourceException ignore) {fail();}
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

        assertTrue(multiplayer.test_getCurrPlayer().canDoStuff());

        for(int i = 0; i < this.multiplayer.test_getTurn().playerInGame(); i++){
            assertTrue(multiplayer.test_getCurrPlayer().useMarketTray(RowCol.COL, 2).header.equals(HeaderTypes.OK));
            assertTrue(multiplayer.test_getCurrPlayer().useMarketTray(RowCol.COL, 2).header.equals(HeaderTypes.INVALID));
            assertTrue(multiplayer.test_getCurrPlayer().endThisTurn().header.equals(HeaderTypes.END_TURN));
        }

    }
}
