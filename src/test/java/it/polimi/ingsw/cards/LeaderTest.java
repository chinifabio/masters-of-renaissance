package it.polimi.ingsw.cards;

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
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static it.polimi.ingsw.model.resource.ResourceBuilder.*;
import static org.junit.jupiter.api.Assertions.*;

public class LeaderTest {

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
        assertDoesNotThrow(()-> game.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> game.test_getCurrPlayer().test_discardLeader());
    }

    @Test
    public void testAddDepot() {
        Effect test = new AddDepotEffect(ResourceType.COIN);

        assertNull(game.test_getCurrPlayer().test_getPB().test_getDepots().get(DepotSlot.SPECIAL1));
        test.use(game.test_getCurrPlayer());

        assertTrue(game.test_getCurrPlayer().test_getPB().test_getDepots().containsKey(DepotSlot.SPECIAL1));
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

    @Test
    public void testAddConversion() throws WrongDepotException {
        // find the first white marble
        List<Marble> list = game.viewMarketTray();
        System.out.println(list);
        int i = 0;
        while(list.get(i).type() != MarbleColor.WHITE) i++;
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

        for (int j = 4*(i/4); j < 4*(i/3)+4; j++) {
            int finalJ = j;
            List<Marble> finalList = list;
            check.stream()
                    .filter(x-> x.equalsType(finalList.get(finalJ).toResource()))
                    .findAny().orElse(buildFaithPoint())
                    .merge(list.get(finalJ).toResource());
        }

        assertDoesNotThrow(()->game.test_getCurrPlayer().useMarketTray(RowCol.ROW, finalI / 4));

        assertArrayEquals(check.toArray(), game.test_getCurrPlayer().test_getPB().getWH_forTest().viewResourcesInBuffer().toArray());

    }
}
