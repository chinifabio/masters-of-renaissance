package it.polimi.ingsw.view.cli.printer;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.litemodel.litemarkettray.LiteMarble;
import it.polimi.ingsw.litemodel.litemarkettray.LiteMarketTray;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;
import it.polimi.ingsw.model.match.markettray.MarketTray;
import it.polimi.ingsw.view.cli.CLI;

import java.io.IOException;

public class MarketTrayPrinter {

    private static final int HEIGHT = 12;
    private  static final int WIDTH = 26;

    private static void createMarketTray(LiteMarketTray tray, String[][] display, int x, int y){

        LiteMarble[][] marbles = new LiteMarble[3][4];

        for (int i = 0; i < 3; i++) {
            System.arraycopy(tray.getMarbles()[i], 0, marbles[i], 0, 4);
        }

        LiteMarble slideMarble = new LiteMarble(tray.getSlideMarble().getColor(), tray.getSlideMarble().getToResource());

        final String traySample =
                            "╔════════════════════╗    " +
                            "║                    ║    " +
                            "║    ╔═0═╦═1═╦═2═╦═3═╣    " +
                            "║    ║   ║   ║   ║   ║ ←1─" +
                            "║    ╠═4═╬═5═╬═6═╬═7═╣    " +
                            "║    ║   ║   ║   ║   ║ ←2─" +
                            "║    ╠═8═╬═9═╬10═╬11═╣    " +
                            "║    ║   ║   ║   ║   ║ ←3─" +
                            "╚════╩═══╩═══╩═══╩═══╝    " +
                            "       ↑   ↑   ↑   ↑      " +
                            "       1   2   3   4      " +
                            "       │   │   │   │      " ;


        int i = 0;
        for (char c : traySample.toCharArray()){
            display[x+i/WIDTH][y+i%WIDTH] = String.valueOf(c);
            i++;
        }

        int indexX = 3;
        int indexY = 7;
        for (int z = 0; z < 3; z++){
            for (int j = 0; j < 4; j++){
                display[indexX][indexY] = CLI.colorMarbles.get(marbles[z][j].getColor());
                indexY = indexY + 4;
            }
            indexY = 7;
            indexX = indexX + 2;
        }
        display[1][WIDTH-7] = CLI.colorMarbles.get(slideMarble.getColor());

    }

    public static void printMarketTray(LiteMarketTray tray){
        String[][] marketTray = new String[HEIGHT][WIDTH];

        createMarketTray(tray, marketTray,0,0);

        for (int i = 0; i < HEIGHT; i++){
            System.out.println();
            for (int j = 0; j< WIDTH; j++){
                System.out.print(marketTray[i][j]);
            }
        }
        System.out.println();

    }

    public static void main(String[] args) throws EmptyDeckException, IOException {
        MarketTray marketTray = new ObjectMapper()
                .readerFor(MarketTray.class)
                .readValue(MarketTrayPrinter.class.getResourceAsStream("/MarketTray.json"));
        LiteMarketTray liteTray = marketTray.liteVersion();
        printMarketTray(liteTray);
    }




}
