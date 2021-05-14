package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.model.Dispatcher;
import it.polimi.ingsw.model.exceptions.tray.UnpaintableMarbleException;
import it.polimi.ingsw.model.match.markettray.DimensionReader;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.Marble;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.MarbleBuilder;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.MarbleColor;
import it.polimi.ingsw.model.match.markettray.MarketTray;
import it.polimi.ingsw.model.match.markettray.RowCol;
import it.polimi.ingsw.model.match.match.Match;
import it.polimi.ingsw.model.match.match.MultiplayerMatch;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class MarketTrayTest {
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
        assertDoesNotThrow(() -> assertEquals(game.test_getCurrPlayer().test_getPB().test_getDepots().get(DepotSlot.BOTTOM).viewResources().get(0), ResourceBuilder.buildCoin()));
        assertDoesNotThrow(()-> game.test_getCurrPlayer().endThisTurn());

    }

    /**
     * test for each row of the tray if the pushCol works correctly.
     * The test saves the configuration of the tray, invokes the push col method, modifies the saved configuration and than checks
     * if the tray contains the same marbles order of the modified configuration.
     */
    @Test
    public void pushCol() {
        int row = 3;
        int col = 4;

        MarketTray tray = new MarketTray();

        List<Marble> beforePush;
        Marble slide;

        assertEquals(12,tray.showMarketTray().size());

        beforePush = tray.showMarketTray();
        slide = tray.showSlideMarble();

        int shiftCol = 1;

        try {
            tray.pushCol(shiftCol, this.game.test_getCurrPlayer());
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

    /**
     * test for each row of the tray if the pushRow works correctly.
     * The test saves the configuration of the tray, invokes the push row method, modifies the saved configuration and than checks
     * if the tray contains the same marbles order of the modified configuration.
     */
    @Test
    public void pushRow() {
        int row = 3;
        int col = 4;

        MarketTray tray = new MarketTray();

        List<Marble> beforePush;
        Marble slide;

        beforePush = tray.showMarketTray();
        slide = tray.showSlideMarble();

        int shiftRow = 1;

        try{
            tray.pushRow(shiftRow, this.game.test_getCurrPlayer());
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

    /**
     * test if the constructor of the market tray initialize the correct number of marble in the game
     */
    @Test
    public void viewTray() {
        MarketTray tray = new MarketTray();

        Map<MarbleColor, Integer> map = new EnumMap<>(MarbleColor.class);

        List<Marble> initConfig = tray.showMarketTray();

        initConfig.forEach((Marble m) -> {
            if(map.containsKey(m.color())){
                int j = map.get(m.color());
                map.put(m.color(), ++j);
            } else {
                map.put(m.color(), 1);
            }
        });

        for(MarbleColor marbleColor : MarbleColor.values()){
            if(!map.containsKey(marbleColor)){
                map.put(marbleColor,0);
            }
        }

        int j = map.get(tray.showSlideMarble().color());
        map.put(tray.showSlideMarble().color(), ++j);

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
        while (marbles.get(i).color() != MarbleColor.WHITE) i++;
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
            assertEquals(HeaderTypes.INVALID, game.test_getCurrPlayer().useMarketTray(RowCol.COL, 6).header);
        }

    }

    @Test
    public void flushBufferTest() {
        assertDoesNotThrow(()->game.test_getCurrPlayer().useMarketTray(RowCol.ROW, 0));

        List<Resource> list = this.game.test_getCurrPlayer().test_getPB().getWH_forTest().viewResourcesInDepot(DepotSlot.BUFFER);
        int sum = 0;
        for(Resource res : list) sum += res.amount();

        assertDoesNotThrow(()->game.test_getCurrPlayer().endThisTurn());

        assertEquals(sum, game.test_getCurrPlayer().test_getPB().getFT_forTest().getPlayerPosition());
        assertDoesNotThrow(()->game.test_getCurrPlayer().endThisTurn());

        assertEquals(ResourceBuilder.buildListOfStorable(), game.test_getCurrPlayer().test_getPB().getWH_forTest().viewResourcesInDepot(DepotSlot.BUFFER));
    }

    @Test
    public void readingDimensions(){
        DimensionReader dimensionReader = new DimensionReader(4,5);
        assertEquals(5, dimensionReader.col);
        assertEquals(4, dimensionReader.row);
    }
}
