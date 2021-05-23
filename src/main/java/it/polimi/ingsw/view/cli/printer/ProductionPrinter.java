package it.polimi.ingsw.view.cli.printer;

import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.litemodel.LiteResource;
import it.polimi.ingsw.litemodel.litewarehouse.LiteProduction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.view.cli.CLI;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ProductionPrinter {

    private static final int HEIGHT = 15; //rows.
    private static final int WIDTH = 100; //cols.

    public static void createProductions(String[][] display, Map<ProductionID, LiteProduction> productions, int x, int y){


        String prod = "PRODUCTIONS";
        String add = "ADDED";
        char prodFirstLetter;
        int initProdname = 0;
        for (int i = x; i< x + HEIGHT; i++){
            for (int j = y; j< y + WIDTH; j++){
                display[i][j] = " ";
            }
        }
        display[x][y] = TextColors.colorText(TextColors.GREEN, "╔");
        for (int i = x + 1; i < x + HEIGHT-2; i++){
            display[i][y] =TextColors.colorText(TextColors.GREEN,"║");
            display[i][y + WIDTH-2] = TextColors.colorText(TextColors.GREEN,"║");
        }
        display[x + HEIGHT-2][y] =TextColors.colorText(TextColors.GREEN,"╚");
        display[x + HEIGHT-2][y + WIDTH-2] = TextColors.colorText(TextColors.GREEN,"╝");
        display[x][y + WIDTH-2] = TextColors.colorText(TextColors.GREEN,"╗");
        for (int i = y + 1; i < y + WIDTH-2; i++) {
            if (i > y + 3 && initProdname <= prod.length() - 1) {
                prodFirstLetter = prod.charAt(initProdname);
                display[x][i] = Character.toString(prodFirstLetter);
                initProdname++;
            } else {
                display[x][i] = TextColors.colorText(TextColors.GREEN, "═");
            }
            display[x + HEIGHT-2][i] = TextColors.colorText(TextColors.GREEN,"═");
        }

        int reset = y+3;
        for (Map.Entry<ProductionID, LiteProduction> entry : productions.entrySet()) {

            int initrow = x + 3;
            int initcol = reset + 1;
            String name = entry.getKey().name();
            char firstLetter;
            int initname = 0;


            display[initrow-1][initcol] = "╔";
            for (int i = initcol+1; i < initcol + 11; i++ ){
                if (i > initcol + 2 && initname <= entry.getKey().name().length() -1) {
                    firstLetter = name.charAt(initname);
                    display[initrow - 1][i] = TextColors.colorText(TextColors.GREEN_BRIGHT,Character.toString(firstLetter));
                    initname++;
                } else {
                    display[initrow - 1][i] = "═";
                }
                display[initrow + 3][i] = "═";
            }
            display[initrow-1][initcol+11] = "╗";
            display[initrow + 3][initcol] ="╚";
            display[initrow + 3][initcol + 11] = "╝";

            for (int i = initrow; i < initrow + 3; i++ ){
                display[i][initcol] = "║";
                display[i][initcol+11] = "║";
            }

            if (entry.getValue() != null) {

                if (entry.getValue().getRequired().size() == 1) {
                    initcol = reset + 6;
                } else if (entry.getValue().getRequired().size() == 2) {
                    initcol = reset + 4;
                } else {
                    initcol = reset + 3;
                }

                for (LiteResource type : entry.getValue().getRequired()) {
                    display[initrow][initcol] = (String.valueOf(type.getAmount()));
                    initcol++;
                    display[initrow][initcol] = (CLI.colorResource.get(type.getType()));
                    initcol++;
                    initcol++;
                }

                display[initrow + 1][reset + 6] = "↓";

                if (entry.getValue().getOutput().size() == 1) {
                    initcol = reset + 6;
                } else if (entry.getValue().getOutput().size() == 2) {
                    initcol = reset + 4;
                } else {
                    initcol = reset + 3;
                }

                for (LiteResource type : entry.getValue().getOutput()) {
                    display[initrow + 2][initcol] = (String.valueOf(type.getAmount()));
                    initcol++;
                    display[initrow + 2][initcol] = (CLI.colorResource.get(type.getType()));
                    initcol++;
                    initcol++;
                }
            }

            initname = 0;
            initrow = y + 8;
            initcol = reset + 1;
            display[initrow-1][initcol] = "╔";
            for (int i = initcol+1; i < initcol + 11; i++ ){
                if (i > initcol + 2 && initname <= add.length() -1) {
                    firstLetter = add.charAt(initname);
                    display[initrow - 1][i] = TextColors.colorText(TextColors.GREEN_BRIGHT,Character.toString(firstLetter));
                    initname++;
                } else {
                    display[initrow - 1][i] = "═";
                }
                display[initrow + 2][i] = "═";
            }
            display[initrow-1][initcol+11] = "╗";
            display[initrow + 2][initcol] ="╚";
            display[initrow + 2][initcol + 11] = "╝";

            for (int i = initrow; i < initrow + 2; i++ ){
                display[i][initcol] = "║";
                display[i][initcol+11] = "║";
            }

            //TODO Da riguardare, trovare un modo di crare una lista di risorse con amount 0 e modificare l'amount in base a quello presente nella lista della added


            initrow = x + 8;
            initcol = reset + 3;
            int num = 0;
            for (LiteResource resource: entry.getValue().getAdded()){
                num++;
                display[initrow][initcol] = CLI.colorResource.get(resource.getType());
                display[initrow][initcol+1] = "x";
                display[initrow][initcol+2] = String.valueOf(resource.getAmount());
                if (resource.getAmount() > 9){
                    display[initrow][initcol+3] = "";
                }
                initcol = initcol + 4;
                if (num == 2){
                    initcol = reset + 3;
                    initrow = initrow + 1;
                }

            }

            if (entry.getKey() == ProductionID.BASIC){
                reset = reset + 20;
            } else if (entry.getKey() == ProductionID.RIGHT) {
                reset = reset + 20;
            } else {
                reset = reset + 13;
            }
        }



    }

    public static void printProductions(Map<ProductionID, LiteProduction> productions){
        String[][] display = new String[HEIGHT][WIDTH];

        createProductions(display, productions, 0 ,0);

        for (int i = 0; i< HEIGHT-1; i++){
            System.out.println();
            for (int j = 0; j<WIDTH-1; j++){
                System.out.print(display[i][j]);
            }
        }
        System.out.println();

    }


    public  static void main(String[] args){
        Map<ProductionID, LiteProduction> productionMap = new EnumMap<ProductionID, LiteProduction>(ProductionID.class);
        List<LiteResource> required = new ArrayList<>();
        List<LiteResource> output = new ArrayList<>();

        List<LiteResource> added = new ArrayList<>();
        added.add(new LiteResource(ResourceType.COIN, 2));
        added.add(new LiteResource(ResourceType.SERVANT, 2));
        added.add(new LiteResource(ResourceType.SHIELD, 2));

        required.add(new LiteResource(ResourceType.COIN, 2));
        output.add(new LiteResource(ResourceType.SERVANT, 1));
        output.add(new LiteResource(ResourceType.FAITHPOINT, 1));

        productionMap.put(ProductionID.BASIC, new LiteProduction(required, added, output));

        required.add(new LiteResource(ResourceType.STONE, 2));
        productionMap.put(ProductionID.LEFT, new LiteProduction(required, added, output));

        required.add(new LiteResource(ResourceType.SHIELD, 3));
        output.add(new LiteResource(ResourceType.COIN, 3));
        productionMap.put(ProductionID.CENTER, new LiteProduction(required, added, output));


        printProductions(productionMap);



    }


}
