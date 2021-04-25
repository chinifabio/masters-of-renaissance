package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.exceptions.PlayerStateException;
import it.polimi.ingsw.model.exceptions.tray.OutOfBoundMarketTrayException;
import it.polimi.ingsw.model.exceptions.tray.UnpaintableMarbleException;
import it.polimi.ingsw.model.exceptions.warehouse.UnobtainableResourceException;
import it.polimi.ingsw.model.exceptions.warehouse.WrongDepotException;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
import it.polimi.ingsw.model.match.markettray.DimensionReader;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.Marble;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.MarbleBuilder;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.MarbleColor;
import it.polimi.ingsw.model.match.markettray.MarketTray;
import it.polimi.ingsw.model.match.markettray.RowCol;
import it.polimi.ingsw.model.match.match.Match;
import it.polimi.ingsw.model.match.match.MultiplayerMatch;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayableCardReaction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class MarketTrayTest {

    Match game = new MultiplayerMatch();

    Player player1 = null;
    Player player2 = null;

    List<Player> order = new ArrayList<>();

    @BeforeEach
    public void init() {

        assertDoesNotThrow(()->{
            player1 = new Player("pino", game);
            player2 = new Player("gino", game);
        });

        game.playerJoin(player1);
        game.playerJoin(player2);

        assertTrue(game.startGame());

        assertDoesNotThrow(()-> game.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> game.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> game.test_getCurrPlayer().endThisTurn());

        assertDoesNotThrow(()-> game.test_getCurrPlayer().chooseResource(ResourceType.COIN));
        assertDoesNotThrow(()-> game.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> game.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(() -> assertTrue(game.test_getCurrPlayer().test_getPB().test_getDepots().get(DepotSlot.STRONGBOX).viewAllResources().contains(ResourceBuilder.buildCoin())));
        assertDoesNotThrow(()-> game.test_getCurrPlayer().endThisTurn());

        if(player1.canDoStuff()){
            order.add(player1);
            order.add(player2);
        } else {
            order.add(player2);
            order.add(player1);
        }
    }

    /**
     * test for each row of the tray if the pushCol works correctly.
     * The test saves the configuration of the tray, invokes the push col method, modifies the saved configuration and than checks
     * if the tray contains the same marbles order of the modified configuration.
     */
    @Test
    public void pushCol() throws IllegalTypeInProduction {
        int row = 3;
        int col = 4;

        MarketTray tray = new MarketTray();
        PlayableCardReaction player = new Player("dummy", null);

        List<Marble> beforePush;
        Marble slide;

        assertEquals(12,tray.showMarketTray().size());

        for(int shiftCol = 0; shiftCol < col; shiftCol++) {
            beforePush = tray.showMarketTray();
            slide = tray.showSlideMarble();

            try {
                tray.pushCol(shiftCol, player);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }

            Marble temp = slide;

            slide = beforePush.get(shiftCol);
            for(int i = shiftCol; i < shiftCol+(row-1)*col; i += col) {
                beforePush.set(i, beforePush.get(i+col));
            }
            beforePush.set(shiftCol+(row-1)*col, temp);

            assertArrayEquals(beforePush.toArray(), tray.showMarketTray().toArray());
            assertEquals(slide,tray.showSlideMarble());
        }
    }

    /**
     * test for each row of the tray if the pushRow works correctly.
     * The test saves the configuration of the tray, invokes the push row method, modifies the saved configuration and than checks
     * if the tray contains the same marbles order of the modified configuration.
     */
    @Test
    public void pushRow() throws IllegalTypeInProduction {
        int row = 3;
        int col = 4;

        MarketTray tray = new MarketTray();
        PlayableCardReaction player = new Player("dummy", null);

        List<Marble> beforePush;
        Marble slide;

        for(int shiftRow = 0; shiftRow < row; shiftRow++) {
            beforePush = tray.showMarketTray();
            slide = tray.showSlideMarble();

            try{
                tray.pushRow(shiftRow, player);
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }

            Marble temp = slide;
            int startPos = shiftRow*col;

            slide = beforePush.get(startPos);
            for (int i = 0; i < col - 1; i++) {
                beforePush.set(startPos+i,(beforePush.get(startPos+i+1)));
            }
            beforePush.set(startPos+col-1, temp);

            assertArrayEquals(beforePush.toArray(), tray.showMarketTray().toArray());
            assertEquals(slide, tray.showSlideMarble());
        }
    }

    /**
     * test if the constructor of the market tray initialize the correct number of marble in the game
     */
    @Test
    public void viewTray() {
        MarketTray tray = new MarketTray();

        Map<MarbleColor, Integer> map = new EnumMap<>(MarbleColor.class);

        List<Marble> initConfig = tray.showMarketTray();

        initConfig.forEach((Marble m) -> {
            if(map.containsKey(m.type())){
                int j = map.get(m.type());
                map.put(m.type(), ++j);
            } else {
                map.put(m.type(), 1);
            }
        });

        for(MarbleColor marbleColor : MarbleColor.values()){
            if(!map.containsKey(marbleColor)){
                map.put(marbleColor,0);
            }
        }

        int j = map.get(tray.showSlideMarble().type());
        map.put(tray.showSlideMarble().type(), ++j);

        assertEquals(4, map.get(MarbleColor.WHITE));
        assertEquals(2, map.get(MarbleColor.BLUE));
        assertEquals(2, map.get(MarbleColor.GRAY));
        assertEquals(2, map.get(MarbleColor.YELLOW));
        assertEquals(2, map.get(MarbleColor.PURPLE));
        assertEquals(1, map.get(MarbleColor.RED));

    }

    @Test
    public void testMarblePainting() {
        MarketTray tray = new MarketTray();

        Marble conversion = MarbleBuilder.buildGray();

        List<Marble> marbles = tray.showMarketTray();
        marbles.add(tray.showSlideMarble());

        Map<ResourceType, Integer> map = new EnumMap<>(ResourceType.class);

        for (Marble marble : marbles) {
            if (map.containsKey(marble.toResource().type())) {
                System.out.println(marble);
                int j = map.get(marble.toResource().type());
                map.put(marble.toResource().type(), ++j);
            } else {
                map.put(marble.toResource().type(), 1);
            }
        }

        assertEquals(4, map.get(ResourceType.EMPTY));
        assertEquals(2, map.get(ResourceType.SERVANT));
        assertEquals(2, map.get(ResourceType.SHIELD));
        assertEquals(2, map.get(ResourceType.STONE));
        assertEquals(2, map.get(ResourceType.COIN));
        assertEquals(1, map.get(ResourceType.FAITHPOINT));

        int i = 0;
        while (marbles.get(i).type() != MarbleColor.WHITE) i++;
        try {
            tray.paintMarble(conversion, i);
        } catch (UnpaintableMarbleException e) {
            e.printStackTrace();
            fail();
        }

        marbles = tray.showMarketTray();
        marbles.add(tray.showSlideMarble());

        Map<ResourceType, Integer> map2 = new EnumMap<>(ResourceType.class);
        for (Marble x : marbles) {
            if (map2.containsKey(x.toResource().type())) {
                int j = map2.get(x.toResource().type());
                map2.put(x.toResource().type(), ++j);
            } else {
                map2.put(x.toResource().type(), 1);
            }
        }

        assertEquals(3, map2.get(ResourceType.EMPTY));
        assertEquals(2, map2.get(ResourceType.SERVANT));
        assertEquals(2, map2.get(ResourceType.SHIELD));
        assertEquals(3, map2.get(ResourceType.STONE));
        assertEquals(2, map2.get(ResourceType.COIN));
        assertEquals(1, map2.get(ResourceType.FAITHPOINT));
    }

    @Test
    public void outOfBound() {

        for (int i = 0; i < 5; i++) {
            // player 1 turn
            if (order.get(0).canDoStuff()) {
                try {
                    order.get(0).useMarketTray(RowCol.COL, 5);
                    fail();
                } catch (OutOfBoundMarketTrayException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                    fail();
                }
                try {
                    order.get(0).endThisTurn();
                } catch (Exception e) {
                    e.printStackTrace();
                    fail();
                }
                // player 2 turn
            } else if (order.get(1).canDoStuff()) {
                try {
                    order.get(1).useMarketTray(RowCol.COL, 1);
                } catch (OutOfBoundMarketTrayException e) {
                    e.printStackTrace();
                    fail();
                } catch (Exception e) {
                    fail();
                    e.printStackTrace();
                }
                try {
                    order.get(1).endThisTurn();
                }  catch (Exception e) {
                    e.printStackTrace();
                    fail();
                }
            } else fail();
        }

    }

    @Test
    public void flushBufferTest() throws WrongDepotException {
        assertDoesNotThrow(()->game.test_getCurrPlayer().useMarketTray(RowCol.ROW, 0));

        List<Resource> list = this.game.test_getCurrPlayer().test_getPB().getWH_forTest().viewResourcesInBuffer();
        int sum = 0;
        for(Resource res : list) sum += res.amount();

        assertDoesNotThrow(()->game.test_getCurrPlayer().endThisTurn());

        assertEquals(sum, game.test_getCurrPlayer().test_getPB().getFT_forTest().getPlayerPosition());
        assertDoesNotThrow(()->game.test_getCurrPlayer().endThisTurn());

        assertEquals(ResourceBuilder.buildListOfStorable(), game.test_getCurrPlayer().test_getPB().getWH_forTest().viewResourcesInBuffer());
    }

    @Test
    public void readingDimensions(){
        DimensionReader dimensionReader = new DimensionReader(4,5);
        assertEquals(5, dimensionReader.col);
        assertEquals(4, dimensionReader.row);
    }
}
