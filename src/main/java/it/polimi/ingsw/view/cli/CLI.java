package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.communication.ServerReply;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.MarbleColor;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.cli.printer.FaithTrackPrinter;
import it.polimi.ingsw.view.cli.printer.MarketTrayPrinter;
import it.polimi.ingsw.view.cli.printer.PersonalBoardPrinter;
import it.polimi.ingsw.view.cli.printer.cardprinter.DevSetupPrinter;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Scanner;

public class CLI implements View {

    public static Map<ResourceType, String> colorResource = new EnumMap<ResourceType, String>(ResourceType.class) {{
        put(ResourceType.COIN, TextColors.colorText(TextColors.YELLOW_BRIGHT, "©"));
        put(ResourceType.SHIELD, TextColors.colorText(TextColors.BLUE_BRIGHT, "▼"));
        put(ResourceType.SERVANT, TextColors.colorText(TextColors.PURPLE, "Õ"));
        put(ResourceType.STONE, TextColors.colorText(TextColors.WHITE, "■"));
        put(ResourceType.EMPTY, " ");
        put(ResourceType.UNKNOWN, TextColors.colorText(TextColors.WHITE_BRIGHT, "?"));
        put(ResourceType.FAITHPOINT, TextColors.colorText(TextColors.RED, "┼"));
    }};

    public static Map<MarbleColor, String> colorMarbles = new EnumMap<MarbleColor, String>(MarbleColor.class) {{
        put(MarbleColor.YELLOW, TextColors.colorText(TextColors.YELLOW_BRIGHT, "●"));
        put(MarbleColor.BLUE, TextColors.colorText(TextColors.BLUE_BRIGHT, "●"));
        put(MarbleColor.PURPLE, TextColors.colorText(TextColors.PURPLE, "●"));
        put(MarbleColor.WHITE, TextColors.colorText(TextColors.WHITE_BRIGHT, "●"));
        put(MarbleColor.GRAY, TextColors.colorText(TextColors.WHITE, "●"));
        put(MarbleColor.RED, TextColors.colorText(TextColors.RED, "●"));
    }};

    private LiteModel model;
    private final FaithTrackPrinter faithTrackPrinter;

    public CLI() throws IOException {
        faithTrackPrinter = new FaithTrackPrinter();
    }

    public final Object lock = new Object();

    /**
     * This method prints the current status of the FaithTrack
     */
    @Override
    public void renderFaithTrack() {
        faithTrackPrinter.printTrack(model);
    }

    /**
     * Render the a view of the market tray
     */
    @Override
    public void renderMarketTray() {
        MarketTrayPrinter.printMarketTray(model.getTray());
    }

    /**
     * Render the personal board of a player
     *
     * @param nickname the player to show personal board
     */
    @Override
    public void renderPersonalBoard(String nickname) {
        PersonalBoardPrinter.printPersonalBoard(model, nickname, model.getLeader(nickname), model.getDevelop(nickname));
    }

    /**
     * Render a view of the devSetup
     */
    @Override
    public void renderDevSetup() {
        DevSetupPrinter.printDevSetup(model.getDevSetup());
    }

    /**
     * Ask to the player something
     *
     * @param request the message to show
     * @return the input string submitted by the player
     */
    @Override
    public String askToPlayer(String request) {
        System.out.println(request);
        System.out.print("> ");
        return new Scanner(System.in).nextLine();
    }

    /**
     * Tell something to the player
     *
     * @param message the message to show up to the player
     */
    @Override
    public void notifyPlayer(String message) {
        System.out.println(message);
    }

    /**
     * Show to the player a server reply
     *
     * @param reply the reply to show to the player
     */
    @Override
    public void notifyServerReply(ServerReply reply) {
        System.out.println(reply.obtainMessage());
    }

    /**
     * Save the lite model passed
     *
     * @param model the model to save
     */
    @Override
    public void receiveModel(LiteModel model) {
        this.model = model;
    }

    /**
     * return the locking object on which synchronize to print
     *
     * @return the lock object
     */
    @Override
    public Object obtainLock() {
        return lock;
    }
}
