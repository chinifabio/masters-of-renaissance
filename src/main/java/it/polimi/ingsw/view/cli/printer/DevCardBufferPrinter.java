package it.polimi.ingsw.view.cli.printer;

import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.litemodel.litewarehouse.LiteDepot;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.litemodel.LiteResource;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.cli.Colors;

import java.util.ArrayList;
import java.util.List;

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

    /*
    public static void main(String[] args){
        LiteModel model = new LiteModel();
        model.createPlayer("gino");

        LiteResource coin = new LiteResource(ResourceType.COIN, 2);
        LiteResource shield = new LiteResource(ResourceType.SHIELD, 1);
        LiteResource stone = new LiteResource(ResourceType.STONE, 3);
        LiteResource servant = new LiteResource(ResourceType.SERVANT, 5);

        List<LiteResource> resourcesTop = new ArrayList<>();
        resourcesTop.add(shield);
        List<LiteResource> resourcesMiddle = new ArrayList<>();
        resourcesMiddle.add(coin);
        List<LiteResource> resourcesBottom = new ArrayList<>();
        resourcesBottom.add(stone);

        List<LiteResource> resourceSpecial = new ArrayList<>();
        resourceSpecial.add(ResourceBuilder.buildServant(1).liteVersion());

        List<LiteResource> resourcesStrongbox = new ArrayList<>();
        resourcesStrongbox.add(new LiteResource(ResourceType.COIN, 20));
        resourcesStrongbox.add(servant);
        resourcesStrongbox.add(new LiteResource(ResourceType.STONE, 3));
        resourcesStrongbox.add(new LiteResource(ResourceType.SHIELD, 15));

        LiteDepot depotTop = new LiteDepot(resourcesTop);
        LiteDepot depotBottom = new LiteDepot(resourcesBottom);
        LiteDepot depotMiddle = new LiteDepot(resourcesMiddle);
        LiteDepot strongbox = new LiteDepot(resourcesStrongbox);
        LiteDepot buffer = new LiteDepot(resourcesStrongbox);
        LiteDepot special1 = new LiteDepot(resourcesMiddle);
        LiteDepot special2 = new LiteDepot(resourceSpecial);

        model.setDepot("gino", DepotSlot.TOP, depotTop);
        model.setDepot("gino", DepotSlot.MIDDLE, depotMiddle);
        model.setDepot("gino", DepotSlot.BOTTOM, depotBottom);
        model.setDepot("gino", DepotSlot.STRONGBOX, strongbox);
        model.setDepot("gino", DepotSlot.BUFFER, buffer);
        model.setDepot("gino", DepotSlot.DEVBUFFER, buffer);
        model.setDepot("gino", DepotSlot.SPECIAL1, special1);
        model.setDepot("gino", DepotSlot.SPECIAL2, special2);

        printDevCardPhase(model, "gino");
    }

     */

}
