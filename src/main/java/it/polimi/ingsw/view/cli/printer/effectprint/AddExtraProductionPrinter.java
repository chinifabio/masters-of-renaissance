package it.polimi.ingsw.view.cli.printer.effectprint;

import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.litemodel.LiteResource;
import it.polimi.ingsw.litemodel.litewarehouse.LiteProduction;
import it.polimi.ingsw.model.resource.ResourceType;

import java.util.EnumMap;
import java.util.Map;

public class AddExtraProductionPrinter implements EffectPrinter{

    private final LiteProduction production;

    private final Map<ResourceType, String> colors;

    public AddExtraProductionPrinter(LiteProduction prod) {
        this.production = prod;
        this.colors = new EnumMap<>(ResourceType.class);
        colors.put(ResourceType.COIN, TextColors.colorText(TextColors.YELLOW_BRIGHT,"©"));
        colors.put(ResourceType.SHIELD, TextColors.colorText(TextColors.BLUE_BRIGHT,"▼"));
        colors.put(ResourceType.SERVANT, TextColors.colorText(TextColors.PURPLE,"Õ"));
        colors.put(ResourceType.STONE, TextColors.colorText(TextColors.WHITE,"■"));
        colors.put(ResourceType.UNKNOWN,TextColors.colorText(TextColors.WHITE_BRIGHT,"?"));
        colors.put(ResourceType.FAITHPOINT, TextColors.colorText(TextColors.RED,"┼"));
    }

    @Override
    public void printEffect(String[][] card) {

        int initcol = 1;
        if (production.getRequired().size() == 1){
            initcol = 5;
        } else if (production.getRequired().size() == 2){
            initcol = 3;
        }
        for (LiteResource type : production.getRequired()){
            card[3][initcol] = (String.valueOf(type.getAmount()));
            initcol++;
            card[3][initcol] = (colors.get(type.getType()));
            initcol++;
            initcol++;
        }
        card[4][(card[1].length/2) -1] = "↓";

        if (production.getOutput().size() == 1){
            initcol = 5;
        } else if (production.getOutput().size() == 2){
            initcol = 3;
        } else {
            initcol = 2;
        }

        for (LiteResource type : production.getOutput()){
            card[5][initcol] = (String.valueOf(type.getAmount()));
            initcol++;
            card[5][initcol] = (colors.get(type.getType()));
            initcol++;
            initcol++;
        }
    }

    @Override
    public void printEffect(String[][] card, int x, int y) {

        int initcol = y + 1;
        if (this.production.getRequired().size() == 1){
            initcol = y + 5;
        } else if (this.production.getRequired().size() == 2){
            initcol = y + 3;
        }
        for (LiteResource type : this.production.getRequired()){
            card[x + 3][initcol] = (String.valueOf(type.getAmount()));
            initcol++;
            card[x + 3][initcol] = (colors.get(type.getType()));
            initcol++;
            initcol++;
        }
        card[x + 4][y+5] = "↓";

        if (this.production.getOutput().size() == 1){
            initcol = y + 5;
        } else if (this.production.getOutput().size() == 2){
            initcol = y + 3;
        } else {
            initcol = y + 2;
        }

        for (LiteResource type : this.production.getOutput()){
            card[x + 5][initcol] = (String.valueOf(type.getAmount()));
            initcol++;
            card[x + 5][initcol] = (colors.get(type.getType()));
            initcol++;
            initcol++;
        }
    }
}
