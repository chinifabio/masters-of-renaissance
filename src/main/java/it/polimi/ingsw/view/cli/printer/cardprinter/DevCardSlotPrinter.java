package it.polimi.ingsw.view.cli.printer.cardprinter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.litemodel.litecards.LiteDevCard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DevCardSlotPrinter {

    private static final int HEIGHT = 11; //rows.
    private static final int WIDTH = 46; //cols.

    public static void printDevCardSlots(List<LiteDevCard> toShow){
        String[][] devCards = new String[HEIGHT][WIDTH];
        for (int i = 0; i< HEIGHT-1; i++){
            for (int j = 0; j<WIDTH-1; j++){
                devCards[i][j] = " ";
            }
        }
        createDevCardSlot(devCards, toShow, 0, 0);

        for (int i = 0; i< HEIGHT-1; i++){
            System.out.println();
            for (int j = 0; j<WIDTH-1; j++){
                System.out.print(devCards[i][j]);
            }
        }
        System.out.println();
    }

    public static void createDevCardSlot(String[][] display, List<LiteDevCard> toShow, int x, int y){
        int index = y;
        for (LiteDevCard card : toShow) {
            DevCardPrinter.createDevCard(display, card, x, index);
            index = index + 14;
        }
    }

    public static void main(String[] args) throws IOException {
        List<LiteDevCard> dev = new ArrayList<>();
        List<List<LiteDevCard>> cardFile;
        ObjectMapper objectMapper = new ObjectMapper();
        Random rd = new Random();

        cardFile = objectMapper.readValue(
                DevCardSlotPrinter.class.getResourceAsStream("/json/DevCards.json"),
                new TypeReference<List<List<LiteDevCard>>>(){});

        dev.add(cardFile.get(rd.nextInt(12)).get(rd.nextInt(4)));
        dev.add(cardFile.get(rd.nextInt(12)).get(rd.nextInt(4)));
        dev.add(cardFile.get(rd.nextInt(12)).get(rd.nextInt(4)));


        printDevCardSlots(dev);
    }


}
