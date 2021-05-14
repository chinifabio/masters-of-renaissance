package it.polimi.ingsw.view.cli.printer.effectprint;

import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.model.resource.ResourceType;

import java.util.EnumMap;
import java.util.Map;

public class AddDiscountPrinter implements EffectPrinter{

    private final ResourceType resourceType;

    private final Map<ResourceType, String> colors;

    public AddDiscountPrinter(ResourceType type) {
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
        leaderCard[4][4] = "";
        leaderCard[4][5] = "-1";
        leaderCard[4][6] = colors.get(resourceType);

    }

    @Override
    public void printEffect(String[][] leaderCard, int x, int y) {
        leaderCard[x + 4][y + 4] = "";
        leaderCard[x + 4][y + 5] = "-1";
        leaderCard[x + 4][y + 6] = colors.get(resourceType);

    }


}
