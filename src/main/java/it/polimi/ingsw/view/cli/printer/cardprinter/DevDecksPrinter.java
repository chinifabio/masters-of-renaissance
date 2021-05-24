package it.polimi.ingsw.view.cli.printer.cardprinter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.litemodel.litecards.LiteDevCard;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DevDecksPrinter {

    private static final int HEIGHT = 17; //rows.
    private static final int WIDTH = 46; //cols.

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

    public static void createDevCardSlot(String[][] display,HashMap<DevCardSlot, List<LiteDevCard>>  toShow, int x, int y){
        int indexCols = y;
        int indexRows = x + 5;

        for (DevCardSlot slot : DevCardSlot.values()) {
            Collections.reverse(toShow.get(slot));
            for (LiteDevCard card : toShow.get(slot)) {
                DevCardPrinter.createDevCard(display, card, indexRows, indexCols);
                indexRows = indexRows - 2;
            }
            indexCols = indexCols + 14;
            indexRows = x + 5;
        }
    }

    public static void main(String[] args) throws IOException {
        List<List<LiteDevCard>> cardFile;
        HashMap<DevCardSlot, List<LiteDevCard>> deck = new HashMap<DevCardSlot, List<LiteDevCard>>(){{
            put(DevCardSlot.LEFT, new ArrayList<>());
            put(DevCardSlot.CENTER, new ArrayList<>());
            put(DevCardSlot.RIGHT, new ArrayList<>());
        }};
        ObjectMapper objectMapper = new ObjectMapper();
        Random rd = new Random();

        cardFile = objectMapper.readValue(
                DevDecksPrinter.class.getResourceAsStream("/DevCards.json"),
                new TypeReference<List<List<LiteDevCard>>>(){});


        deck.get(DevCardSlot.LEFT).add(cardFile.get(rd.nextInt(12)).get(rd.nextInt(4)));
        deck.get(DevCardSlot.LEFT).add(cardFile.get(rd.nextInt(12)).get(rd.nextInt(4)));
        deck.get(DevCardSlot.CENTER).add(cardFile.get(rd.nextInt(12)).get(rd.nextInt(4)));
        deck.get(DevCardSlot.CENTER).add(cardFile.get(rd.nextInt(12)).get(rd.nextInt(4)));
        deck.get(DevCardSlot.RIGHT).add(cardFile.get(rd.nextInt(12)).get(rd.nextInt(4)));
        deck.get(DevCardSlot.RIGHT).add(cardFile.get(rd.nextInt(12)).get(rd.nextInt(4)));




        printDevDecks(deck);
    }

}
