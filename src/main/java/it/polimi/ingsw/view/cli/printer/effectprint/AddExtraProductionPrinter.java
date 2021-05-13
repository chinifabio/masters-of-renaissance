package it.polimi.ingsw.view.cli.printer.effectprint;

import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.litemodel.litecards.literequirements.LiteResourceTypeResquisite;
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
    public void printEffect(String[][] leaderCard) {
        for (int i = 1; i < leaderCard[3].length-1; i++){
            leaderCard[3][i] = "-";
        }
        int initcol = 1;
        for (LiteResourceTypeResquisite type : production.getRequired()){
           leaderCard[4][initcol] = (String.valueOf(type.getAmount()));
           initcol++;
           leaderCard[4][initcol] = (colors.get(type.getResourceType()));
           initcol++;
        }
        leaderCard[4][initcol] = "}";
        initcol++;
        initcol++;
        for (LiteResourceTypeResquisite type : production.getOutput()){
            leaderCard[4][initcol] = (String.valueOf(type.getAmount()));
            initcol++;
            leaderCard[4][initcol] = (colors.get(type.getResourceType()));
            initcol++;
            initcol++;
        }

        for (int i = 1; i < leaderCard[5].length-1; i++){
            leaderCard[5][i] = "-";
        }

    }
}
