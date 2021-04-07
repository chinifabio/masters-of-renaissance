package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.exceptions.OutOfBoundMarketTrayException;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.Marble;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.MarbleColor;
import it.polimi.ingsw.model.match.markettray.MarketTray;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerModifier;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class MarketTrayTest {
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
    public void pushCol() {
        int row = 3;
        int col = 4;

        MarketTray tray = new MarketTray();
        PlayerModifier player = new Player("dummy");

        List<Marble> beforePush;
        Marble slide;

        for(int shiftCol = 0; shiftCol < col; shiftCol++) {
            beforePush = tray.showMarketTray();
            slide = tray.showSlideMarble();

            try {
                tray.pushCol(shiftCol, player);
            } catch (OutOfBoundMarketTrayException e) {
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
    public void pushRow() {
        int row = 3;
        int col = 4;

        MarketTray tray = new MarketTray();
        PlayerModifier player = new Player("dummy");

        List<Marble> beforePush;
        Marble slide;

        for(int shiftRow = 0; shiftRow < row; shiftRow++) {
            beforePush = tray.showMarketTray();
            slide = tray.showSlideMarble();

            try{
                tray.pushRow(shiftRow, player);
            } catch (OutOfBoundMarketTrayException e) {
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

        int j = map.get(tray.showSlideMarble().type());
        map.put(tray.showSlideMarble().type(), ++j);

        assertEquals(map.get(MarbleColor.WHITE), 4);
        assertEquals(map.get(MarbleColor.BLUE), 2);
        assertEquals(map.get(MarbleColor.GRAY), 2);
        assertEquals(map.get(MarbleColor.YELLOW), 2);
        assertEquals(map.get(MarbleColor.PURPLE), 2);
        assertEquals(map.get(MarbleColor.RED), 1);

    }
}
