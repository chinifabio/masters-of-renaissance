package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.IOException;
import java.util.*;

public class MarketTrayTest {
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

    /**
     * test for each row of the tray if the pushCol works correctly.
     * The test saves the configuration of the tray, invokes the push col method, modifies the saved configuration and than checks
     * if the tray contains the same marbles order of the modified configuration.
     */
    @Test
    public void pushCol() {
        int row = 3;
        int col = 4;

        MarketTray tray = null;
        try {
            tray = new ObjectMapper()
                    .readerFor(MarketTray.class)
                    .readValue(getClass().getResourceAsStream("/MarketTray.json"));
        } catch (IOException e) {
            fail(e.getMessage());
        }

        List<Marble> beforePush;
        Marble slide;

        assertEquals(12,tray.showMarketTray().size());

        beforePush = tray.showMarketTray();
        slide = tray.showSlideMarble();


        int shiftCol = 1;

        try {
            tray.pushCol(shiftCol, this.game.currentPlayer());
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

        MarketTray tray = null;
        try {
            tray = new ObjectMapper()
                    .readerFor(MarketTray.class)
                    .readValue(getClass().getResourceAsStream("/MarketTray.json"));
        } catch (IOException e) {
            fail(e.getMessage());
        }

        List<Marble> beforePush;
        Marble slide;

        beforePush = tray.showMarketTray();
        slide = tray.showSlideMarble();

        int shiftRow = 1;

        try{
            tray.pushRow(shiftRow, this.game.currentPlayer());
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
        MarketTray tray = null;

        try {
            tray = new ObjectMapper()
                    .readerFor(MarketTray.class)
                    .readValue(getClass().getResourceAsStream("/MarketTray.json"));
        } catch (IOException e) {
            fail(e.getMessage());
        }

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
        MarketTray tray = null;
        try {
            tray = new ObjectMapper()
                    .readerFor(MarketTray.class)
                    .readValue(getClass().getResourceAsStream("/MarketTray.json"));
        } catch (IOException e) {
            fail(e.getMessage());
        }

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
            assertEquals(HeaderTypes.INVALID, game.currentPlayer().useMarketTray(RowCol.COL, 6).header);
        }

    }

    @Test
    public void flushBufferTest() {
        assertDoesNotThrow(()->game.currentPlayer().useMarketTray(RowCol.ROW, 0));

        List<Resource> list = this.game.currentPlayer().test_getPB().getWH_forTest().viewResourcesInDepot(DepotSlot.BUFFER);
        int sum = 0;
        for(Resource res : list) sum += res.amount();

        assertDoesNotThrow(()->game.currentPlayer().endThisTurn());

        assertEquals(sum, game.currentPlayer().test_getPB().getFT_forTest().getPlayerPosition());
        assertDoesNotThrow(()->game.currentPlayer().endThisTurn());

        assertEquals(ResourceBuilder.buildListOfStorable(), game.currentPlayer().test_getPB().getWH_forTest().viewResourcesInDepot(DepotSlot.BUFFER));
    }

    @Test
    public void readingDimensions(){
        DimensionReader dimensionReader = new DimensionReader(4,5);
        assertEquals(5, dimensionReader.col);
        assertEquals(4, dimensionReader.row);
    }
}
