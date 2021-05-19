package it.polimi.ingsw.cards;

import it.polimi.ingsw.model.Dispatcher;
import it.polimi.ingsw.model.cards.effects.*;
import it.polimi.ingsw.model.exceptions.warehouse.WrongDepotException;
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
    Match game = new MultiplayerMatch(2, view);

    @BeforeEach
    public void initialization() {
        assertDoesNotThrow(()->player1 = new Player("gino", game, view));
        assertTrue(game.playerJoin(player1));
        assertDoesNotThrow(()->player2 = new Player("pino", game, view));
        assertTrue(game.playerJoin(player2));

        //assertTrue(game.startGame());

        assertDoesNotThrow(()-> game.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> game.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> game.test_getCurrPlayer().endThisTurn());

        assertDoesNotThrow(()-> game.test_getCurrPlayer().chooseResource(DepotSlot.BOTTOM, ResourceType.COIN));
        assertDoesNotThrow(()-> game.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> game.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(() -> assertEquals(game.test_getCurrPlayer().test_getPB().getDepots().get(DepotSlot.BOTTOM).viewResources().get(0), ResourceBuilder.buildCoin()));
        assertDoesNotThrow(()-> game.test_getCurrPlayer().endThisTurn());
    }

    @Test
    public void testAddDepot() {
        Effect test = new AddDepotEffect(ResourceType.COIN);

        assertNull(game.test_getCurrPlayer().test_getPB().getDepots().get(DepotSlot.SPECIAL1));
        test.use(game.test_getCurrPlayer());

        assertTrue(game.test_getCurrPlayer().test_getPB().getDepots().containsKey(DepotSlot.SPECIAL1));
    }

    @Test
    public void testAddProduction() {
        AtomicReference<Effect> test = new AtomicReference<>();

        assertDoesNotThrow(()-> test.set(new AddProductionEffect(new NormalProduction(
                Arrays.asList(buildCoin(), buildServant()),
                Collections.singletonList(buildStone())
        ))));

        game.test_getCurrPlayer().test_setDest(DevCardSlot.CENTER);

        assertFalse(game.test_getCurrPlayer().test_getPB().test_getProduction().containsKey(ProductionID.CENTER));
        test.get().use(game.test_getCurrPlayer());

        assertTrue(game.test_getCurrPlayer().test_getPB().test_getProduction().containsKey(ProductionID.CENTER));
    }

    @Test
    public void testAddExtraProduction() {
        AtomicReference<Effect> test = new AtomicReference<>();

        assertDoesNotThrow(()-> test.set(new AddExtraProductionEffect(new NormalProduction(
               Arrays.asList(buildCoin(), buildServant()),
                Collections.singletonList(buildStone())
       ))));

        assertFalse(game.test_getCurrPlayer().test_getPB().test_getProduction().containsKey(ProductionID.LEADER1));

        test.get().use(game.test_getCurrPlayer());

        assertTrue(game.test_getCurrPlayer().test_getPB().test_getProduction().containsKey(ProductionID.LEADER1));

        test.get().use(game.test_getCurrPlayer());
        assertTrue(game.test_getCurrPlayer().test_getPB().test_getProduction().containsKey(ProductionID.LEADER2));


    }

    @Test
    public void testAddDiscount() {
        Effect test = new AddDiscountEffect(ResourceType.SERVANT);

        assertTrue(game.test_getCurrPlayer().test_getDiscount().isEmpty());
        test.use(game.test_getCurrPlayer());
        assertTrue(game.test_getCurrPlayer().test_getDiscount().contains(buildServant()));
    }

    @RepeatedTest(10)
    public void testAddConversion() throws WrongDepotException {
        // find the first white marble
        List<Marble> list = game.viewMarketTray();
        int i = 0;
        while(list.get(i).color() != MarbleColor.WHITE) i++;
        int finalI = i;

        // adding marble conversion
        Marble conv = MarbleBuilder.buildBlue();
        Effect test = new WhiteMarbleEffect(conv);

        assertTrue(game.test_getCurrPlayer().test_getConv().isEmpty());
        test.use(game.test_getCurrPlayer());
        assertTrue(game.test_getCurrPlayer().test_getConv().contains(conv));

        // paint it
        assertDoesNotThrow(()->game.test_getCurrPlayer().paintMarbleInTray(0, finalI));

        // counting the right resources that should be in the buffer
        List<Resource> check = ResourceBuilder.buildListOfStorable();
        list = game.viewMarketTray();

        for (int j = 4*(i/4); j < 4*(i/4)+4; j++) { // counting only the marbles in the ROW of the white marble painted
            int finalJ = j;
            List<Marble> finalList = list;
            check.stream()
                    .filter(x-> x.equalsType(finalList.get(finalJ).toResource()))   // find resource that matches the marble resource
                    .findAny().orElse(buildFaithPoint())                            // if the resource doesn't exits it means it is a faith point
                    .merge(list.get(finalJ).toResource());                          // merging the .toResource of the marble whit the storable resource
        }

        assertDoesNotThrow(()->game.test_getCurrPlayer().useMarketTray(RowCol.ROW, finalI / 4));
        //TODO a volte dà errore
        assertArrayEquals(check.toArray(), game.test_getCurrPlayer().test_getPB().getWH_forTest().viewResourcesInDepot(DepotSlot.BUFFER).toArray());

    }
}
