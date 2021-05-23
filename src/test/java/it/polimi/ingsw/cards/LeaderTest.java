package it.polimi.ingsw.cards;

import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.model.Dispatcher;
import it.polimi.ingsw.model.cards.effects.*;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.Marble;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.MarbleBuilder;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.MarbleColor;
import it.polimi.ingsw.model.match.markettray.RowCol;
import it.polimi.ingsw.model.match.match.Match;
import it.polimi.ingsw.model.match.match.MultiplayerMatch;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.NormalProduction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static it.polimi.ingsw.model.resource.ResourceBuilder.*;
import static org.junit.jupiter.api.Assertions.*;

public class LeaderTest {
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
        assertEquals(HeaderTypes.END_TURN, order.get(0).endThisTurn().header);

        assertEquals(HeaderTypes.OK, order.get(1).chooseResource(DepotSlot.BOTTOM, ResourceType.COIN).header);
        assertDoesNotThrow(()-> order.get(1).test_discardLeader());
        assertDoesNotThrow(()-> order.get(1).test_discardLeader());
        assertDoesNotThrow(() -> assertEquals(order.get(1).test_getPB().getDepots().get(DepotSlot.BOTTOM).viewResources().get(0), ResourceBuilder.buildCoin()));
        assertEquals(HeaderTypes.END_TURN, order.get(1).endThisTurn().header);
    }

    @Test
    public void testAddDepot() {
        Effect test = new AddDepotEffect(ResourceType.COIN);

        assertNull(game.currentPlayer().test_getPB().getDepots().get(DepotSlot.SPECIAL1));
        test.use(game.currentPlayer());

        assertTrue(game.currentPlayer().test_getPB().getDepots().containsKey(DepotSlot.SPECIAL1));
    }

    @Test
    public void testAddProduction() {
        AtomicReference<Effect> test = new AtomicReference<>();

        assertDoesNotThrow(()-> test.set(new AddProductionEffect(new NormalProduction(
                Arrays.asList(buildCoin(), buildServant()),
                Collections.singletonList(buildStone())
        ))));

        game.currentPlayer().test_setDest(DevCardSlot.CENTER);

        assertFalse(game.currentPlayer().test_getPB().test_getProduction().containsKey(ProductionID.CENTER));
        test.get().use(game.currentPlayer());

        assertTrue(game.currentPlayer().test_getPB().test_getProduction().containsKey(ProductionID.CENTER));
    }

    @Test
    public void testAddExtraProduction() {
        AtomicReference<Effect> test = new AtomicReference<>();

        assertDoesNotThrow(()-> test.set(new AddExtraProductionEffect(new NormalProduction(
               Arrays.asList(buildCoin(), buildServant()),
                Collections.singletonList(buildStone())
       ))));

        assertFalse(game.currentPlayer().test_getPB().test_getProduction().containsKey(ProductionID.LEADER1));

        test.get().use(game.currentPlayer());

        assertTrue(game.currentPlayer().test_getPB().test_getProduction().containsKey(ProductionID.LEADER1));

        test.get().use(game.currentPlayer());
        assertTrue(game.currentPlayer().test_getPB().test_getProduction().containsKey(ProductionID.LEADER2));


    }

    @Test
    public void testAddDiscount() {
        Effect test = new AddDiscountEffect(ResourceType.SERVANT);

        assertTrue(game.currentPlayer().test_getDiscount().isEmpty());
        test.use(game.currentPlayer());
        assertTrue(game.currentPlayer().test_getDiscount().contains(buildServant()));
    }

    @RepeatedTest(10)
    public void testAddConversion() {
        // find the first white marble
        List<Marble> list = game.viewMarketTray();
        int whiteMarble = 0;
        while(list.get(whiteMarble).color() != MarbleColor.WHITE) whiteMarble++;

        // adding marble conversion
        Marble conv = MarbleBuilder.buildBlue();
        Effect test = new WhiteMarbleEffect(conv);

        assertTrue(game.currentPlayer().test_getConv().isEmpty());
        test.use(game.currentPlayer());
        assertTrue(game.currentPlayer().test_getConv().contains(conv));

        // paint it
        assertEquals(HeaderTypes.OK, game.currentPlayer().paintMarbleInTray(0, whiteMarble).header);

        // counting the right resources that should be in the buffer
        List<Resource> check = ResourceBuilder.buildListOfStorable();
        list = game.viewMarketTray();

        for (int j = 4*(whiteMarble/4); j < 4*(whiteMarble/4)+4; j++) { // counting only the marbles in the ROW of the white marble painted
            int finalJ = j;
            List<Marble> finalList = list;
            check.stream()
                    .filter(x-> x.equalsType(finalList.get(finalJ).toResource()))   // find resource that matches the marble resource
                    .findAny().orElse(buildFaithPoint())                            // if the resource doesn't exits it means it is a faith point
                    .merge(list.get(finalJ).toResource());                          // merging the .toResource of the marble whit the storable resource
        }

        assertEquals(HeaderTypes.OK, game.currentPlayer().useMarketTray(RowCol.ROW, whiteMarble / 4).header);

        assertArrayEquals(check.toArray(), game.currentPlayer().test_getPB().getWH_forTest().viewResourcesInDepot(DepotSlot.BUFFER).toArray());

    }
}
