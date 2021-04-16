package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.exceptions.faithtrack.IllegalMovesException;
import it.polimi.ingsw.model.exceptions.game.LorenzoMovesException;
import it.polimi.ingsw.model.exceptions.tray.OutOfBoundMarketTrayException;
import it.polimi.ingsw.model.exceptions.tray.UnpaintableMarbleException;
import it.polimi.ingsw.model.exceptions.game.movesexception.MainActionDoneException;
import it.polimi.ingsw.model.exceptions.warehouse.UnobtainableResourceException;
import it.polimi.ingsw.model.exceptions.warehouse.WrongPointsException;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.Marble;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.MarbleBuilder;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.MarbleColor;
import it.polimi.ingsw.model.match.markettray.MarketTray;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerReactEffect;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class MarketTrayTest {
    //TODO DA TESTARE OUT OF BOUND
    /**
     * checks if two lists contain the same elements trough the equals method of the element
     * @param one the first list
     * @param two the second list
     * @return true if the lists are equivalent, otherwise return false
     */
    public boolean equalsList(List<?> one, List<?> two) {
        if (one.size() != two.size()) return false;
        else {
            for(int i = 0; i < one.size(); i++) {
                if(!one.get(i).equals(two.get(i))) return false;
            }
        }
        return true;
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
        PlayerReactEffect player = new Player("dummy", null);

        List<Marble> beforePush;
        Marble slide;

        for(int shiftCol = 0; shiftCol < col; shiftCol++) {
            beforePush = tray.showMarketTray();
            slide = tray.showSlideMarble();

            try {
                tray.pushCol(shiftCol, player);
            } catch (OutOfBoundMarketTrayException | MainActionDoneException | UnobtainableResourceException | LorenzoMovesException e) {
                e.printStackTrace();
            } catch (WrongPointsException e) {
                e.printStackTrace();
            } catch (IllegalMovesException e) {
                e.printStackTrace();
            } finally {
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
        PlayerReactEffect player = new Player("dummy", null);

        List<Marble> beforePush;
        Marble slide;

        for(int shiftRow = 0; shiftRow < row; shiftRow++) {
            beforePush = tray.showMarketTray();
            slide = tray.showSlideMarble();

            try{
                tray.pushRow(shiftRow, player);
            } catch (OutOfBoundMarketTrayException | MainActionDoneException e) {
                e.printStackTrace();
            } catch (UnobtainableResourceException e) {
                e.printStackTrace();
            } catch (LorenzoMovesException e) {
                e.printStackTrace();
            } catch (WrongPointsException e) {
                e.printStackTrace();
            } catch (IllegalMovesException e) {
                e.printStackTrace();
            } finally {
                Marble temp = slide;
                int startPos = shiftRow*col;

                slide = beforePush.get(startPos);
                for (int i = 0; i < col - 1; i++) {
                    beforePush.set(startPos+i,(beforePush.get(startPos+i+1)));
                }
                beforePush.set(startPos+col-1, temp);

                assertTrue(equalsList(beforePush, tray.showMarketTray()));
                assertEquals(slide, tray.showSlideMarble());
            }
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
        marbles.forEach(x-> System.out.println(x.toResource()));

        System.out.println("1- "+marbles);
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
        while (marbles.get(i).type() != MarbleColor.WHITE) i++;
        try {
            tray.paintMarble(conversion, i);
        } catch (UnpaintableMarbleException e) {
            e.printStackTrace();
            fail();
        }

        marbles = tray.showMarketTray();
        marbles.add(tray.showSlideMarble());
        marbles.forEach(x-> System.out.println(x.toResource().type()));

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
}
