package it.polimi.ingsw.view.cli.printer;

import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.litemodel.litewarehouse.LiteDepot;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.litemodel.LiteResource;
import it.polimi.ingsw.view.cli.CLI;

import java.util.ArrayList;
import java.util.List;

public class WarehousePrinter {

    private static final int MAX_VERT = 7; //rows.
    private static final int MAX_HORIZ = 29; //cols.

    private static final int MAX_VERT_BUFFER = 3; //cols.


    public static void createWarehouse(String[][] display, LiteModel model, String nickname, int x, int y) {



        //creating Depots
        display[x][y] = "╔";
        for (int i = y + 1; i < y + MAX_HORIZ - 1; i++) {
            display[x][i] = "═";
        }
        display[x][y + MAX_HORIZ - 1] = "╗";

        for (int i = x + 1; i < x + 4; i++) {
            display[i][y] = "║";
            for (int r = y + 1; r < y + MAX_HORIZ - 1; r++) {
                display[i][r] = " ";
            }
            display[i][y + MAX_HORIZ - 1] = "║";
        }


        int initR = x + 1;
        int initC = y + 12;

        List<LiteDepot> depots = new ArrayList<>();
        depots.add(model.getDepot(nickname, DepotSlot.TOP));
        depots.add(model.getDepot(nickname, DepotSlot.MIDDLE));
        depots.add(model.getDepot(nickname, DepotSlot.BOTTOM));

        for (int depot = 0; depot < 3; depot++) {
            display[initR][initC] = "[";
            for (LiteResource resource : depots.get(depot).getResourcesInside()) {
                display[initR][initC+1] = " ";
                for (int i = initC+2; i < (initC+2) + (resource.getAmount() * 2); i++) {
                    if (initR != x + 2) {
                        if (i % 2 == 0) {
                            display[initR][i] = CLI.colorResource.get(resource.getType());
                        } else {
                            display[initR][i] = " ";
                        }
                    } else {
                        if (i % 2 == 1) {
                            display[initR][i] = CLI.colorResource.get(resource.getType());
                        } else {
                            display[initR][i] = " ";
                        }
                    }
                }

                int z = resource.getAmount();
                if (resource.getType() == ResourceType.EMPTY){
                   z = initR - x;
                }
                while (z < (initR - x)) {
                    display[initR][(z * 2) + (initC+2)] = " ";
                    z++;
                }
                display[initR][(z*2) + (initC+2)] = "]";
            }

            initC = initC - 1;
            initR = initR + 1;
        }


        //Adding the lines
        for (int i = x + 1; i < x + 4; i++){
            for (int r = y + 1; r < y + MAX_HORIZ -1; r++){
                if (display[i][r].equals("[")){
                    while (!display[i][r].equals("]")){
                        r++;
                    }
                } else {
                    display[i][r] = "─";
                }
            }
        }


        int reset = x + 4;
        display[reset][y] = TextColors.colorText(TextColors.YELLOW,"╔");
        display[reset][y + MAX_HORIZ - 1] = TextColors.colorText(TextColors.YELLOW,"╗");
        display[x + MAX_VERT-1][y] = TextColors.colorText(TextColors.YELLOW,"╚");
        display[x + MAX_VERT-1][y + MAX_HORIZ-1] = TextColors.colorText(TextColors.YELLOW,"╝");

        for (int i = y + 1; i < y + MAX_HORIZ - 1; i++) {
            if (i == (y + (MAX_HORIZ - 1) / 2)){
                display[reset][i] = TextColors.colorText(TextColors.WHITE_BRIGHT,"0");
            } else {
                display[reset][i] = TextColors.colorText(TextColors.YELLOW,"═");
            }
            display[x + MAX_VERT-1][i] = TextColors.colorText(TextColors.YELLOW,"═");
        }
        for (int i = reset+1; i < x + MAX_VERT-1; i++) {
            display[i][y] = TextColors.colorText(TextColors.YELLOW,"║");
            for (int r = y + 1; r < y + MAX_HORIZ - 1; r++) {
                display[i][r] = " ";
            }
            display[i][y + MAX_HORIZ - 1] = TextColors.colorText(TextColors.YELLOW,"║");
        }

        //Insert resources in Strongbox
        reset++;
        int init = y + 4;
        for (LiteResource resource : model.getDepot(nickname, DepotSlot.STRONGBOX).getResourcesInside()){
            display[reset][init] = CLI.colorResource.get(resource.getType());
            init = init+1;
            display[reset][init] = "x";
            init = init+1;
            display[reset][init] = String.valueOf(resource.getAmount());
            if (resource.getAmount() > 9 && resource.getAmount() < 100){
                display[reset][init+1] = "";
            } else if (resource.getAmount() >= 100) {
                display[reset][init+1] = "";
                display[reset][init+2] = "";
            }

            init = init + 4;
        }


    }

    public static void createBuffer(String[][] buffer, LiteModel model, String nickname, int x, int y){


        buffer[x][y] = TextColors.colorText(TextColors.CYAN,"╔");
        buffer[x][y + MAX_HORIZ - 1] = TextColors.colorText(TextColors.CYAN,"╗");
        buffer[x + MAX_VERT_BUFFER-1][y] = TextColors.colorText(TextColors.CYAN,"╚");
        buffer[x + MAX_VERT_BUFFER-1][y + MAX_HORIZ-1] = TextColors.colorText(TextColors.CYAN,"╝");

        for (int i = y + 1; i < y + MAX_HORIZ - 1; i++) {
            if (i == y + 12){
                buffer[x][i] = TextColors.colorText(TextColors.CYAN,"BUFFER");
            } else {
                buffer[x][i] = TextColors.colorText(TextColors.CYAN,"═");
            }
            buffer[x + MAX_VERT_BUFFER-1][i] = TextColors.colorText(TextColors.CYAN,"═");
        }
        int j = y + 13;
        while (j != y + 18){
            buffer[x][j] = "";
            j++;
        }
        for (int i = x + 1; i < x + MAX_VERT_BUFFER-1; i++) {
            buffer[i][y] = TextColors.colorText(TextColors.CYAN,"║");
            for (int r = y + 1; r < y + MAX_HORIZ - 1; r++) {
                buffer[i][r] = " ";
            }
            buffer[i][y + MAX_HORIZ - 1] = TextColors.colorText(TextColors.CYAN,"║");
        }

        int initBuffer = y + 4;
        for (LiteResource resource : model.getDepot(nickname, DepotSlot.BUFFER).getResourcesInside()){
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

    public static void printWarehouse(LiteModel model, String nickname){

        String[][] warehouse = new String[MAX_VERT][MAX_HORIZ];
        String[][] buffer = new String[MAX_VERT_BUFFER][MAX_HORIZ];

        createWarehouse(warehouse, model, nickname, 0, 0);
        createBuffer(buffer, model, nickname, 0 ,0);


        //Printing Legend
        String legend =
                TextColors.colorText(TextColors.YELLOW_BRIGHT, "©") + " = " + TextColors.colorText(TextColors.YELLOW_BRIGHT, "Coin\n") +
                TextColors.colorText(TextColors.BLUE_BRIGHT, "▼") + " = " + TextColors.colorText(TextColors.BLUE_BRIGHT, "Shield\n") +
                TextColors.colorText(TextColors.PURPLE, "Õ") + " = " + TextColors.colorText(TextColors.PURPLE, "Servant\n") +
                TextColors.colorText(TextColors.WHITE, "■") + " = " + TextColors.colorText(TextColors.WHITE, "Stone\n");
        System.out.println(legend);

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

        model.setDepot("gino", DepotSlot.TOP, depotTop);
        model.setDepot("gino", DepotSlot.MIDDLE, depotMiddle);
        model.setDepot("gino", DepotSlot.BOTTOM, depotBottom);
        model.setDepot("gino", DepotSlot.STRONGBOX, strongbox);
        model.setDepot("gino", DepotSlot.BUFFER, buffer);

        printWarehouse(model, "gino");
    }

}
