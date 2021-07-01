package it.polimi.ingsw.view.cli.printer.cardprinter;

import it.polimi.ingsw.litemodel.litecards.LiteDevCard;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;

import java.util.*;

/**
 * This class is the Printer of the DevCards slots in the PersonalBoard
 */
public class DevDecksPrinter {

    /**
     * This attribute is the height of the slots
     */
    private static final int HEIGHT = 17; //rows.

    /**
     * This attribute is the width of the slots
     */
    private static final int WIDTH = 46; //cols.

    /**
     * This method prints the Decks of DevCards in the PersonalBoard's slots
     * @param devDecks is the List of DevCards to show
     */
    public static void printDevDecks(HashMap<DevCardSlot, List<LiteDevCard>> devDecks){
        String[][] devCards = new String[HEIGHT][WIDTH];
        for (int i = 0; i< HEIGHT-1; i++){
            for (int j = 0; j<WIDTH-1; j++){
                devCards[i][j] = " ";
            }
        }

        createDevCardSlot(devCards, devDecks, 0, 0);

        for (int i = 0; i< HEIGHT-1; i++){
            System.out.println();
            for (int j = 0; j<WIDTH-1; j++){
                System.out.print(devCards[i][j]);
            }
        }
        System.out.println();
    }

    /**
     * This method creates the DevCards Slots
     * @param display is where the DevCards Decks will be created
     * @param toShow is the List of Cards to show
     * @param x is the horizontal coordinate
     * @param y is the vertical coordinate
     */
    public static void createDevCardSlot(String[][] display,HashMap<DevCardSlot, List<LiteDevCard>>  toShow, int x, int y){
        int indexCols = y;
        int indexRows = x + 5;

        for (DevCardSlot slot : DevCardSlot.values()) {
            List<LiteDevCard> temp = new ArrayList<>(toShow.get(slot));
            Collections.reverse(temp);
            for (LiteDevCard card : temp) {
                DevCardPrinter.createDevCard(display, card, indexRows, indexCols);
                indexRows = indexRows - 2;
            }
            indexCols = indexCols + 14;
            indexRows = x + 5;
        }
    }

}
