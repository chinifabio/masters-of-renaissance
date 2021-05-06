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

    public void printLegend(){

        String legend = TextColors.colorText(TextColors.YELLOW_BRIGHT, "©") + " = " + TextColors.colorText(TextColors.YELLOW_BRIGHT, "Coin\n") +
                TextColors.colorText(TextColors.BLUE_BRIGHT, "▼") + " = " + TextColors.colorText(TextColors.BLUE_BRIGHT, "Shield\n") +
                TextColors.colorText(TextColors.PURPLE, "Õ") + " = " + TextColors.colorText(TextColors.PURPLE, "Servant\n") +
                TextColors.colorText(TextColors.WHITE, "■") + " = " + TextColors.colorText(TextColors.WHITE, "Stone\n");
        System.out.println(legend);

    }

    private void printBottomDepot(){
        System.out.print("║         [ ");
        for (LiteResource resource : warehouse.getDepots().get(2).getResourcesInside()){
            for (int i = 0; i< resource.getAmount(); i++){
                System.out.print(colors.get(resource.getType()));
                System.out.print(" ");
            }
            int z = resource.getAmount();
            while (z < 3){
                System.out.print("  ");
                z++;
            }
        }

        System.out.println("]         ║");
    }

    private void printMiddleDepot(){
        System.out.print("║          [ ");
        for (LiteResource resource : warehouse.getDepots().get(1).getResourcesInside()){
            for (int i = 0; i< resource.getAmount(); i++){
                System.out.print(colors.get(resource.getType()));
                System.out.print(" ");
            }
            int z = resource.getAmount();
            while (z < 2){
                System.out.print("  ");
                z++;
            }
        }

        System.out.println("]          ║");
    }

    private void printTopDepot(){

        System.out.print("║            [");
        for (LiteResource resource : warehouse.getDepots().get(0).getResourcesInside()){
            for (int i = 0; i< resource.getAmount(); i++){
                System.out.print(colors.get(resource.getType()));
            }
            int z = resource.getAmount();
            while (z < 1){
                System.out.print(" ");
                z++;
            }
        }
        System.out.println("]            ║");
    }

    private void printStrongbox(){
        StringBuilder strongbox = new StringBuilder();
        strongbox.append("╔═════════════0═════════════╗\n");
        strongbox.append("║  ");
        for (LiteResource resource : warehouse.getDepots().get(3).getResourcesInside()){
            strongbox.append(colors.get(resource.getType())).append("x").append((resource.getAmount()));
            if (resource.getAmount() < 10) {
                strongbox.append("   ");
            } else if (resource.getAmount() > 9 && resource.getAmount() < 100){
                strongbox.append("  ");
            } else {
                strongbox.append(" ");
            }
        }
        strongbox.append(" ║\n");
        strongbox.append("╚═══════════════════════════╝");
        System.out.println(strongbox);
    }



    public void printWarehouse(){
        printLegend();
        System.out.println("╔═══════════════════════════╗");
        printTopDepot();
        printMiddleDepot();
        printBottomDepot();
        printStrongbox();
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
        resourcesStrongbox.add(coin);
        resourcesStrongbox.add(servant);
        resourcesStrongbox.add(stone);
        resourcesStrongbox.add(shield);

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
