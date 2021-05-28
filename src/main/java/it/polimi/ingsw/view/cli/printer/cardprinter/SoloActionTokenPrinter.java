package it.polimi.ingsw.view.cli.printer.cardprinter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.litemodel.litecards.LiteSoloActionToken;
import it.polimi.ingsw.model.cards.SoloActionToken;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;
import it.polimi.ingsw.view.cli.Colors;

import java.io.IOException;
import java.util.List;

public class SoloActionTokenPrinter {

    private static final int HEIGHT = 7;
    private  static final int WIDTH = 13;

    private static void createToken(LiteSoloActionToken token, String[][] display, int x, int y){
        final String squareToken =
                "╔Solo Action╗" +
                "║ ╔═Token═╗ ║" +
                "║ ║       ║ ║" +
                "║ ║       ║ ║" +
                "║ ║       ║ ║" +
                "║ ╚═══════╝ ║" +
                "╚═══════════╝";
        int i = 0;
        for (char c : squareToken.toCharArray()){
            display[x+i/WIDTH][y+i%WIDTH] = String.valueOf(c);
            i++;
        }

        for (int z = 1; z < HEIGHT-1; z++){
            for (int j = 2; j< WIDTH-1; j++){
                display[z][j] = Colors.color(Colors.RED, display[z][j]);
            }
        }

        for (int z = 0; z < HEIGHT; z++){
            for (int j = 0; j< WIDTH; j++){
                display[z][j] = Colors.color(Colors.YELLOW, display[z][j]);
            }
        }


        int initCol = x + 3;
        int initRow = y + 3;
        token.getEffect().printEffect(display, initCol, initRow);


    }

    public static void printSoloActionToken(LiteSoloActionToken token){
        String[][] toPrint = new String[HEIGHT][WIDTH];
        createToken(token, toPrint, 0, 0);

        for (int i = 0; i < HEIGHT; i++){
            System.out.println();
            for (int j = 0; j< WIDTH; j++){
                System.out.print(toPrint[i][j]);
            }
        }
        System.out.println();
    }

    public static void main(String[] args) throws EmptyDeckException, IOException {
        List<SoloActionToken> tokens;

        ObjectMapper objectMapper = new ObjectMapper();

        tokens = objectMapper.readValue(
                SoloActionTokenPrinter.class.getResourceAsStream("/json/SoloActionTokens.json"),
                new TypeReference<List<SoloActionToken>>(){});
        for (SoloActionToken token : tokens){
            printSoloActionToken(token.liteVersion());
        }
    }
}
