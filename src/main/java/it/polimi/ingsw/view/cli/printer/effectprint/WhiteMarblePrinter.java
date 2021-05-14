package it.polimi.ingsw.view.cli.printer.effectprint;
import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.litemodel.litemarkettray.LiteMarble;
import it.polimi.ingsw.model.resource.ResourceType;

import java.util.EnumMap;
import java.util.Map;

public class WhiteMarblePrinter implements EffectPrinter{

    private final LiteMarble marble;

    private final Map<ResourceType, String> colors;

    public WhiteMarblePrinter(LiteMarble marble) {
        this.marble = marble;
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
        leaderCard[4][3] = "O";
        leaderCard[4][5] = "=";
        leaderCard[4][7] = colors.get(marble.getToResource());
    }

    @Override
    public void printEffect(String[][] leaderCard, int x, int y) {
        leaderCard[x + 4][y + 3] = "O";
        leaderCard[x + 4][y + 5] = "=";
        leaderCard[x + 4][y + 7] = colors.get(marble.getToResource());
    }
}
