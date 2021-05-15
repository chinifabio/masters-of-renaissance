package it.polimi.ingsw.view.cli.printer.cardprinter;
import it.polimi.ingsw.litemodel.litecards.LiteDevCard;
import it.polimi.ingsw.litemodel.litecards.LiteDevSetup;
import it.polimi.ingsw.model.cards.DevSetup;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;
import java.io.IOException;

public class DevSetupPrinter {

    private static final int HEIGHT = 10*3; //rows.
    private static final int WIDTH = 14*4; //cols.

    public static void printDevSetup(LiteDevSetup Setup){
        String[][] devSetup = new String[HEIGHT][WIDTH];
        LiteDevCard[][] toShow = new LiteDevCard[3][4];

        for (int i = 0; i < 3; i++) {
            System.arraycopy(Setup.getDevSetup()[i], 0, toShow[i], 0, 4);
        }

        for (int i = 0; i< HEIGHT-1; i++){
            for (int j = 0; j<WIDTH-1; j++){
                devSetup[i][j] = " ";
            }
        }
        int initX = 0;
        int initY = 0;
        for (int i = 0; i < toShow[0].length; i++){
            for (LiteDevCard[] liteDevCards : toShow) {
                DevCardPrinter.createDevCard(devSetup, liteDevCards[i], initX, initY);
                initX = initX + 10;
            }
            initY = initY + 13;
            initX = 0;
        }

        for (int i = 0; i< HEIGHT-1; i++){
            System.out.println();
            for (int j = 0; j<WIDTH-1; j++){
                System.out.print(devSetup[i][j]);
            }
        }
        System.out.println();


    }

    public static void main(String[] args) throws EmptyDeckException, IOException {
        DevSetup set = new DevSetup();
        LiteDevSetup devSetup = set.liteVersion();

        printDevSetup(devSetup);
    }
}
