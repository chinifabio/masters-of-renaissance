package it.polimi.ingsw.view.cli.printer.effectprint;

import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.model.resource.ResourceType;

import java.util.EnumMap;
import java.util.Map;

public class AddDepotPrinter implements EffectPrinter{

    private final ResourceType resourceType;

    private final Map<ResourceType, String> colors;

    public AddDepotPrinter(ResourceType type) {
        this.resourceType = type;
        this.colors = new EnumMap<>(ResourceType.class);
        colors.put(ResourceType.COIN, TextColors.colorText(TextColors.YELLOW_BRIGHT,"©"));
        colors.put(ResourceType.SHIELD, TextColors.colorText(TextColors.BLUE_BRIGHT,"▼"));
        colors.put(ResourceType.SERVANT, TextColors.colorText(TextColors.PURPLE,"Õ"));
        colors.put(ResourceType.STONE, TextColors.colorText(TextColors.WHITE,"■"));
        colors.put(ResourceType.EMPTY," ");
    }

    @Override
    public void printEffect(String[][] leaderCard) {
        int initcol = 1;
        for (int i = 0; i < 2; i++){
            leaderCard[4][initcol] = "[";
            leaderCard[4][initcol+1] = " ";
            leaderCard[4][initcol+2] = colors.get(resourceType);
            leaderCard[4][initcol+3] = " ";
            leaderCard[4][initcol+4] = "]";
            initcol = initcol+5;
        }

    }
}
