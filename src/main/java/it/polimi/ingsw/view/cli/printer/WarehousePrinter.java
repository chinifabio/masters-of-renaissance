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

public class WarehousePrinter {

    private static final int MAX_VERT = 7; //rows.
    private static final int MAX_HORIZ = 40; //cols.

    private static final int MAX_VERT_BUFFER = 3; //cols.


    public static void createWarehouse(String[][] display, LiteModel model, String nickname, int x, int y) {

        for (int i = x; i< MAX_VERT; i++){
            for (int j = y; j<MAX_HORIZ; j++){
                display[i][j] = " ";
            }
        }


        //creating Depots
        display[x][y] = "╔";
        for (int i = y + 1; i < y + 28; i++) {
            display[x][i] = "═";
        }
        display[x][y + 28] = "╗";

        for (int i = x + 1; i < x + 4; i++) {
            display[i][y] = "║";
            for (int r = y + 1; r < y + 28; r++) {
                display[i][r] = " ";
            }
            display[i][y + 28] = "║";
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
            for (int r = y + 1; r < y + 28; r++){
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
        display[reset][y] = Colors.color(Colors.YELLOW,"╔");
        display[reset][y + 28] = Colors.color(Colors.YELLOW,"╗");
        display[x + MAX_VERT-1][y] = Colors.color(Colors.YELLOW,"╚");
        display[x + MAX_VERT-1][y + 28] = Colors.color(Colors.YELLOW,"╝");

        for (int i = y + 1; i < y + 28; i++) {
            if (i == (y + (28 / 2))){
                display[reset][i] = Colors.color(Colors.WHITE_BRIGHT,"0");
            } else {
                display[reset][i] = Colors.color(Colors.YELLOW,"═");
            }
            display[x + MAX_VERT-1][i] = Colors.color(Colors.YELLOW,"═");
        }
        for (int i = reset+1; i < x + MAX_VERT-1; i++) {
            display[i][y] = Colors.color(Colors.YELLOW,"║");
            for (int r = y + 1; r < y + 28; r++) {
                display[i][r] = " ";
            }
            display[i][y + 28] = Colors.color(Colors.YELLOW,"║");
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

        int initSpecialY = y + 29;
        int initSpecialX = x;
        //Inserting special Depot
        List<LiteDepot> special = new ArrayList<>();
        if (model.getDepot(nickname, DepotSlot.SPECIAL1) != null) {
            special.add(model.getDepot(nickname, DepotSlot.SPECIAL1));
        }
        if (model.getDepot(nickname, DepotSlot.SPECIAL2) != null) {
            special.add(model.getDepot(nickname, DepotSlot.SPECIAL2));
        }
        int index = 1;
        int initString = 0;
        String special1 = "SPECIAL";
        char firstletter;
        for (LiteDepot depot : special){
            display[initSpecialX][initSpecialY] = "╔";
            display[initSpecialX][initSpecialY+10] = "╗";
            display[initSpecialX+2][initSpecialY] = "╚";
            display[initSpecialX+2][initSpecialY+10] = "╝";
            display[initSpecialX][initSpecialY+1] = CLI.colorResource.get(depot.getResourcesInside().get(0).getType());
            display[initSpecialX+2][initSpecialY+1] = "═";
            for (int i = initSpecialY + 2; i < initSpecialY+9 ; i++){
                firstletter = special1.charAt(initString);
                display[initSpecialX][i] = Character.toString(firstletter);
                display[initSpecialX+2][i] = "═";
                initString++;
            }
            display[initSpecialX][initSpecialY+9] = CLI.colorResource.get(depot.getResourcesInside().get(0).getType());
            display[initSpecialX+2][initSpecialY+9] = "═";
            for (int j = initSpecialX + 1; j < initSpecialX + 2; j++){
                display[j][initSpecialY] = "║";
                display[j][initSpecialY+10] = "║";
            }

            initSpecialX++;
            initSpecialY = initSpecialY + 2;
            //Adding special Resources
            display[initSpecialX][initSpecialY] = "[";
            for (LiteResource resource : depot.getResourcesInside()) {
                display[initSpecialX][initSpecialY+1] = " ";
                for (int i = initSpecialY+2; i < (initSpecialY+2) + (resource.getAmount() * 2); i++) {
                    if (initSpecialX != x + 2) {
                        if (i % 2 == 1) {
                            display[initSpecialX][i] = CLI.colorResource.get(resource.getType());
                        } else {
                            display[initSpecialX][i] = " ";
                        }
                    } else {
                        if (i % 2 == 0) {
                            display[initSpecialX][i] = CLI.colorResource.get(resource.getType());
                        } else {
                            display[initSpecialX][i] = " ";
                        }
                    }
                }

                int z = resource.getAmount();
                if (resource.getType() == ResourceType.EMPTY){
                    z = initSpecialX - x;
                }

                while (z < (initSpecialX - x)) {
                    z++;
                }
                display[initSpecialX][initSpecialY + 6] = "]";
            }
            initString = 0;
            index++;
            initSpecialY = y + 29;
            initSpecialX = x + 3;
        }



    }

    public static void createBuffer(String[][] buffer, LiteModel model, String nickname, int x, int y){

        for (int i = x; i< MAX_VERT_BUFFER; i++){
            for (int j = y; j<MAX_HORIZ; j++){
                buffer[i][j] = " ";
            }
        }

        buffer[x][y] = Colors.color(Colors.CYAN,"╔");
        buffer[x][y + 28] = Colors.color(Colors.CYAN,"╗");
        buffer[x + MAX_VERT_BUFFER-1][y] = Colors.color(Colors.CYAN,"╚");
        buffer[x + MAX_VERT_BUFFER-1][y + 28] = Colors.color(Colors.CYAN,"╝");

        for (int i = y + 1; i < y + 28; i++) {
            if (i == y + 12){
                buffer[x][i] = Colors.color(Colors.CYAN,"BUFFER");
            } else {
                buffer[x][i] = Colors.color(Colors.CYAN,"═");
            }
            buffer[x + MAX_VERT_BUFFER-1][i] = Colors.color(Colors.CYAN,"═");
        }
        int j = y + 13;
        while (j != y + 18){
            buffer[x][j] = "";
            j++;
        }
        for (int i = x + 1; i < x + MAX_VERT_BUFFER-1; i++) {
            buffer[i][y] = Colors.color(Colors.CYAN,"║");
            for (int r = y + 1; r < y + 28; r++) {
                buffer[i][r] = " ";
            }
            buffer[i][y + 28] = Colors.color(Colors.CYAN,"║");
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
        model.setDepot("gino", DepotSlot.SPECIAL1, special1);
        model.setDepot("gino", DepotSlot.SPECIAL2, special2);

        printWarehouse(model, "gino");
    }

}
