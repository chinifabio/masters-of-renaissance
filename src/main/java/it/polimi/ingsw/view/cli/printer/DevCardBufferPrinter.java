package it.polimi.ingsw.view.cli.printer;

import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.litemodel.LiteResource;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.Colors;

import static it.polimi.ingsw.view.cli.printer.WarehousePrinter.createWarehouse;

/**
 * This class is the Printer of the Development Cards buffer
 */
public class DevCardBufferPrinter {

    /**
     * This attribute is the vertical size of the warehouse
     */
    private static final int MAX_VERT = 7; //rows.

    /**
     * This attribute is the horizontal size of the buffer
     */
    private static final int MAX_HORIZ = 40; //cols.

    /**
     * This attribute is vertical size of the buffer
     */
    private static final int MAX_VERT_BUFFER = 3; //rows.

    /**
     * This method creates the buffer of the Development Cards
     * @param buffer is the Development Cards' Buffer
     * @param model is the Model of the match
     * @param nickname is the Nickname of the Player
     * @param x is the horizontal coordinate
     * @param y is the vertical coordinate
     */
    public static void createDevBuffer(String[][] buffer, LiteModel model, String nickname, int x, int y){

        for (int i = x; i< MAX_VERT_BUFFER; i++){
            for (int j = y; j<MAX_HORIZ; j++){
                buffer[i][j] = " ";
            }
        }

        buffer[x][y] = Colors.color(Colors.GREEN,"╔");
        buffer[x][y + 28] = Colors.color(Colors.GREEN,"╗");
        buffer[x + MAX_VERT_BUFFER-1][y] = Colors.color(Colors.GREEN,"╚");
        buffer[x + MAX_VERT_BUFFER-1][y + 28] = Colors.color(Colors.GREEN,"╝");

        for (int i = y + 1; i < y + 28; i++) {
            if (i == y + 9){
                buffer[x][i] = Colors.color(Colors.GREEN,"DEVBUFFER");
            } else {
                buffer[x][i] = Colors.color(Colors.GREEN,"═");
            }
            buffer[x + MAX_VERT_BUFFER-1][i] = Colors.color(Colors.GREEN,"═");
        }
        int j = y + 10;
        while (j != y + 18){
            buffer[x][j] = "";
            j++;
        }
        for (int i = x + 1; i < x + MAX_VERT_BUFFER-1; i++) {
            buffer[i][y] = Colors.color(Colors.GREEN,"║");
            for (int r = y + 1; r < y + 28; r++) {
                buffer[i][r] = " ";
            }
            buffer[i][y + 28] = Colors.color(Colors.GREEN,"║");
        }

        int initBuffer = y + 4;
        for (LiteResource resource : model.getDepot(nickname, DepotSlot.DEVBUFFER).getResourcesInside()){
            buffer[x + 1][initBuffer] = CLI.colorResource.get(resource.getType());
            initBuffer = initBuffer+1;
            buffer[x + 1][initBuffer] = "x";
            initBuffer = initBuffer+1;
            buffer[x + 1][initBuffer] = String.valueOf(resource.getAmount());
            if (resource.getAmount() > 9 && resource.getAmount() < 100){
                buffer[x + 1][initBuffer+1] = "";
            } else if (resource.getAmount() >= 100) {
                buffer[x + 1][initBuffer+1] = "";
                buffer[x + 1][initBuffer+2] = "";
            }

            initBuffer = initBuffer + 4;
        }
    }

    /**
     * This method prints the BuyDevCard phase
     * @param model is the model of the Match
     * @param nickname is the Nickname of the Player
     */
    public static void printDevCardPhase(LiteModel model, String nickname){

        String[][] warehouse = new String[MAX_VERT][MAX_HORIZ];
        String[][] buffer = new String[MAX_VERT_BUFFER][MAX_HORIZ];

        createWarehouse(warehouse, model, nickname, 0, 0);
        createDevBuffer(buffer, model, nickname, 0 ,0);

        for (int r = 0; r < (MAX_VERT); r++) {
            System.out.println();
            for (int c = 0; c < (MAX_HORIZ); c++) {
                System.out.print(warehouse[r][c]);
            }
        }

        for (int r = 0; r<  MAX_VERT_BUFFER ; r++){
            System.out.println();
            for (int c = 0; c < (MAX_HORIZ); c++) {
                System.out.print(buffer[r][c]);
            }
        }
        System.out.println();
    }

    public static void printResourcesLegend(){
        String legend =
                Colors.color(Colors.YELLOW_BRIGHT, "©") + " = " + Colors.color(Colors.YELLOW_BRIGHT, "Coin\n") +
                        Colors.color(Colors.BLUE_BRIGHT, "▼") + " = " + Colors.color(Colors.BLUE_BRIGHT, "Shield\n") +
                        Colors.color(Colors.PURPLE, "Õ") + " = " + Colors.color(Colors.PURPLE, "Servant\n") +
                        Colors.color(Colors.WHITE, "■") + " = " + Colors.color(Colors.WHITE, "Stone\n");
        System.out.println(legend);
    }

}
