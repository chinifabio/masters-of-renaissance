package it.polimi.ingsw.view.cli.printer;

import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.view.litemodel.LiteDepot;
import it.polimi.ingsw.view.litemodel.LiteModel;
import it.polimi.ingsw.view.litemodel.LiteResource;
import it.polimi.ingsw.view.litemodel.LiteWarehouse;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class WarehousePrinter {

    private static final int MAX_VERT = 7; //rows.
    private static final int MAX_HORIZ = 29; //cols.

    private final LiteWarehouse warehouse;

    private final Map<ResourceType, String> colors;

    public WarehousePrinter(LiteModel model) {
        this.warehouse = model.getWarehouse();
        this.colors = new EnumMap<>(ResourceType.class);
        colors.put(ResourceType.COIN, TextColors.colorText(TextColors.YELLOW_BRIGHT,"©"));
        colors.put(ResourceType.SHIELD, TextColors.colorText(TextColors.BLUE_BRIGHT,"▼"));
        colors.put(ResourceType.SERVANT, TextColors.colorText(TextColors.PURPLE,"Õ"));
        colors.put(ResourceType.STONE, TextColors.colorText(TextColors.WHITE,"■"));
        colors.put(ResourceType.EMPTY," ");

    }


    public void printWarehouse() {

        String[][] warehouse = new String[MAX_VERT][MAX_HORIZ];

        createDepots(this.warehouse, warehouse);
        createStrongbox(this.warehouse, warehouse);

        printLegend();

        for (int r = 0; r < (MAX_VERT); r++) {
            System.out.println();
            for (int c = 0; c < (MAX_HORIZ); c++) {
                System.out.print(warehouse[r][c]);
            }
        }
        System.out.println();
    }

    private void createDepots(LiteWarehouse liteWarehouse, String[][] warehouse) {

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

        for (int depot = 0; depot < 3; depot++) {
            warehouse[initR][initC] = "[";
            for (LiteResource resource : liteWarehouse.getDepots().get(depot).getResourcesInside()) {
                for (int i = initC+2; i < (initC+2) + (resource.getAmount() * 2); i++) {
                    if (initR != 2) {
                        if (i % 2 == 0) {
                            warehouse[initR][i] = colors.get(resource.getType());
                        } else {
                            warehouse[initR][i] = " ";
                        }
                    } else {
                        if (i % 2 == 1) {
                            warehouse[initR][i] = colors.get(resource.getType());
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

    }


    private void createStrongbox(LiteWarehouse liteWarehouse, String[][] warehouse){

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
        for (LiteResource resource : liteWarehouse.getDepots().get(3).getResourcesInside()){
            warehouse[reset][init] = colors.get(resource.getType());
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


    }

    public void printLegend(){

        String legend = TextColors.colorText(TextColors.YELLOW_BRIGHT, "©") + " = " + TextColors.colorText(TextColors.YELLOW_BRIGHT, "Coin\n") +
                TextColors.colorText(TextColors.BLUE_BRIGHT, "▼") + " = " + TextColors.colorText(TextColors.BLUE_BRIGHT, "Shield\n") +
                TextColors.colorText(TextColors.PURPLE, "Õ") + " = " + TextColors.colorText(TextColors.PURPLE, "Servant\n") +
                TextColors.colorText(TextColors.WHITE, "■") + " = " + TextColors.colorText(TextColors.WHITE, "Stone\n");
        System.out.println(legend);

    }

    public static void main(String[] args){
        LiteModel model = new LiteModel();

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

        List<LiteDepot> depots = new ArrayList<>();
        depots.add(depotTop);
        depots.add(depotMiddle);
        depots.add(depotBottom);
        depots.add(strongbox);

        LiteWarehouse warehouse = new LiteWarehouse(depots);

        model.setWarehouse(warehouse);
        WarehousePrinter printer = new WarehousePrinter(model);

        printer.printWarehouse();
    }

}
