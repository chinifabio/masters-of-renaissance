package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.MarbleColor;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.view.cli.printer.FaithTrackPrinter;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

public class CLI implements View {

    public static Map<ResourceType, String> colorResource = new EnumMap<ResourceType, String>(ResourceType.class){{
        put(ResourceType.COIN, TextColors.colorText(TextColors.YELLOW_BRIGHT,"©"));
        put(ResourceType.SHIELD, TextColors.colorText(TextColors.BLUE_BRIGHT,"▼"));
        put(ResourceType.SERVANT, TextColors.colorText(TextColors.PURPLE,"Õ"));
        put(ResourceType.STONE, TextColors.colorText(TextColors.WHITE,"■"));
        put(ResourceType.EMPTY," ");
        put(ResourceType.UNKNOWN,TextColors.colorText(TextColors.WHITE_BRIGHT,"?"));
        put(ResourceType.FAITHPOINT, TextColors.colorText(TextColors.RED,"┼"));
    }};

    public static Map<MarbleColor, String> colorMarbles = new EnumMap<MarbleColor, String>(MarbleColor.class){{
        put(MarbleColor.YELLOW, TextColors.colorText(TextColors.YELLOW_BRIGHT,"●"));
        put(MarbleColor.BLUE, TextColors.colorText(TextColors.BLUE_BRIGHT,"●"));
        put(MarbleColor.PURPLE, TextColors.colorText(TextColors.PURPLE,"●"));
        put(MarbleColor.WHITE, TextColors.colorText(TextColors.WHITE_BRIGHT,"●"));
        put(MarbleColor.GRAY,"●");
        put(MarbleColor.RED, TextColors.colorText(TextColors.RED,"●"));
    }};

    @Override
    public void showFaithTrack(){
    }

    @Override
    public void showMarketTray() {

    }

    @Override
    public void showPersonalBoard(Player player) {

    }

    @Override
    public void showDevSetup() {

    }

    @Override
    public void askToPlayer() {

    }

    @Override
    public void clearScreen() {

    }


}
