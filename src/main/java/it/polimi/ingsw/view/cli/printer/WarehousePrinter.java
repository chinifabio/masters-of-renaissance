package it.polimi.ingsw.view.cli.printer;

import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.litemodel.litewarehouse.LiteDepot;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.litemodel.LiteResource;
import it.polimi.ingsw.view.cli.CLI;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class WarehousePrinter {

    private static final int MAX_VERT = 7; //rows.
    private static final int MAX_HORIZ = 29; //cols.

    private static final int MAX_VERT_BUFFER = 3; //cols.


    public static void printWarehouse(LiteModel model, String nickname) {

        String[][] warehouse = new String[MAX_VERT][MAX_HORIZ];
        String[][] buffer = new String[MAX_VERT_BUFFER][MAX_HORIZ];


        //creating Depots
        warehouse[0][0] = "╔";
        for (int i = 1; i < MAX_HORIZ - 1; i++) {
            warehouse[0][i] = "═";
        }
        warehouse[0][MAX_HORIZ - 1] = "╗";

        for (int i = 1; i < 4; i++) {
            warehouse[i][0] = "║";
            for (int r = 1; r < MAX_HORIZ - 1; r++) {
                warehouse[i][r] = " ";
            }
            warehouse[i][MAX_HORIZ - 1] = "║";
        }


        int initR = 1;
        int initC = 12;

        List<LiteDepot> depots = new ArrayList<>();
        depots.add(model.getDepot(nickname, DepotSlot.TOP));
        depots.add(model.getDepot(nickname, DepotSlot.MIDDLE));
        depots.add(model.getDepot(nickname, DepotSlot.BOTTOM));

        for (int depot = 0; depot < 3; depot++) {
            warehouse[initR][initC] = "[";
            for (LiteResource resource : depots.get(depot).getResourcesInside()) {
                warehouse[initR][initC+1] = " ";
                for (int i = initC+2; i < (initC+2) + (resource.getAmount() * 2); i++) {
                    if (initR != 2) {
                        if (i % 2 == 0) {
                            warehouse[initR][i] = CLI.colorResource.get(resource.getType());
                        } else {
                            warehouse[initR][i] = " ";
                        }
                    } else {
                        if (i % 2 == 1) {
                            warehouse[initR][i] = CLI.colorResource.get(resource.getType());
                        } else {
                            warehouse[initR][i] = " ";
                        }
                    }
                }
                int z = resource.getAmount();
                while (z < initR) {
                    warehouse[initR][(z * 2) + (initC+2)] = " ";
                    z++;
                }
                warehouse[initR][(z*2) + (initC+2)] = "]";
            }

            initC--;
            initR++;
        }

        //Adding the lines
        for (int i = 1; i < 4; i++){
            for (int r = 1; r < MAX_HORIZ -1; r++){
                if (warehouse[i][r].equals("[")){
                    while (!warehouse[i][r].equals("]")){
                        r++;
                    }
                } else {
                    warehouse[i][r] = "─";
                }
            }
        }

        int reset = 4;
        warehouse[reset][0] = TextColors.colorText(TextColors.YELLOW,"╔");
        warehouse[reset][MAX_HORIZ - 1] = TextColors.colorText(TextColors.YELLOW,"╗");
        warehouse[MAX_VERT-1][0] = TextColors.colorText(TextColors.YELLOW,"╚");
        warehouse[MAX_VERT-1][MAX_HORIZ-1] = TextColors.colorText(TextColors.YELLOW,"╝");

        for (int i = 1; i < MAX_HORIZ - 1; i++) {
            if (i == ((MAX_HORIZ-1) / 2)){
                warehouse[reset][i] = TextColors.colorText(TextColors.WHITE_BRIGHT,"0");
            } else {
                warehouse[reset][i] = TextColors.colorText(TextColors.YELLOW,"═");
            }
            warehouse[MAX_VERT-1][i] = TextColors.colorText(TextColors.YELLOW,"═");
        }
        for (int i = reset+1; i < MAX_VERT-1; i++) {
            warehouse[i][0] = TextColors.colorText(TextColors.YELLOW,"║");
            for (int r = 1; r < MAX_HORIZ - 1; r++) {
                warehouse[i][r] = " ";
            }
            warehouse[i][MAX_HORIZ - 1] = TextColors.colorText(TextColors.YELLOW,"║");
        }

        //Insert resources in Strongbox
        reset++;
        int init = 4;
        for (LiteResource resource : model.getDepot(nickname, DepotSlot.STRONGBOX).getResourcesInside()){
            warehouse[reset][init] = CLI.colorResource.get(resource.getType());
            init = init+1;
            warehouse[reset][init] = "x";
            init = init+1;
            warehouse[reset][init] = String.valueOf(resource.getAmount());
            if (resource.getAmount() > 9 && resource.getAmount() < 100){
                warehouse[reset][init+1] = "";
            } else if (resource.getAmount() >= 100) {
                warehouse[reset][init+1] = "";
                warehouse[reset][init+2] = "";
            }

            init = init + 4;
        }

        //Printing Legend
        String legend = TextColors.colorText(TextColors.YELLOW_BRIGHT, "©") + " = " + TextColors.colorText(TextColors.YELLOW_BRIGHT, "Coin\n") +
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
        printBuffer(model, nickname);

    }

    public static void printBuffer(LiteModel model, String nickname){
        String[][] buffer = new String[MAX_VERT_BUFFER][MAX_HORIZ];

        buffer[0][0] = TextColors.colorText(TextColors.CYAN,"╔");
        buffer[0][MAX_HORIZ - 1] = TextColors.colorText(TextColors.CYAN,"╗");
        buffer[MAX_VERT_BUFFER-1][0] = TextColors.colorText(TextColors.CYAN,"╚");
        buffer[MAX_VERT_BUFFER-1][MAX_HORIZ-1] = TextColors.colorText(TextColors.CYAN,"╝");

        for (int i = 1; i < MAX_HORIZ - 1; i++) {
            if (i == 12){
                buffer[0][i] = TextColors.colorText(TextColors.CYAN,"BUFFER");
            } else {
                buffer[0][i] = TextColors.colorText(TextColors.CYAN,"═");
            }
            buffer[MAX_VERT_BUFFER-1][i] = TextColors.colorText(TextColors.CYAN,"═");
        }
        int j = 13;
        while (j != 18){
            buffer[0][j] = "";
            j++;
        }
        for (int i = 1; i < MAX_VERT_BUFFER-1; i++) {
            buffer[i][0] = TextColors.colorText(TextColors.CYAN,"║");
            for (int r = 1; r < MAX_HORIZ - 1; r++) {
                buffer[i][r] = " ";
            }
            buffer[i][MAX_HORIZ - 1] = TextColors.colorText(TextColors.CYAN,"║");
        }

        int initBuffer = 4;
        for (LiteResource resource : model.getDepot(nickname, DepotSlot.BUFFER).getResourcesInside()){
            buffer[1][initBuffer] = CLI.colorResource.get(resource.getType());
            initBuffer = initBuffer+1;
            buffer[1][initBuffer] = "x";
            initBuffer = initBuffer+1;
            buffer[1][initBuffer] = String.valueOf(resource.getAmount());
            if (resource.getAmount() > 9 && resource.getAmount() < 100){
                buffer[1][initBuffer+1] = "";
            } else if (resource.getAmount() >= 100) {
                buffer[1][initBuffer+1] = "";
                buffer[1][initBuffer+2] = "";
            }

            initBuffer = initBuffer + 4;
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

        LiteResource coin = new LiteResource(ResourceType.EMPTY, 0);
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
