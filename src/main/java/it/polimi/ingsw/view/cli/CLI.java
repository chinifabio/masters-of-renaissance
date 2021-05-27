package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.client.Actions;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.InputHandler;
import it.polimi.ingsw.communication.ServerReply;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.MarbleColor;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.cli.printer.FaithTrackPrinter;
import it.polimi.ingsw.view.cli.printer.MarketTrayPrinter;
import it.polimi.ingsw.view.cli.printer.PersonalBoardPrinter;
import it.polimi.ingsw.view.cli.printer.WarehousePrinter;
import it.polimi.ingsw.view.cli.printer.cardprinter.DevSetupPrinter;
import it.polimi.ingsw.view.cli.printer.cardprinter.LeaderCardPrinter;
import it.polimi.ingsw.view.cli.printer.cardprinter.ShowLeaderCards;

import java.io.IOException;
import java.util.*;

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
    private List<String> data;

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
    public List<String> pollData(String request) throws InterruptedException {
        synchronized (lock) {
            System.out.println(request);
            lock.notifyAll();
            lock.wait();
        }

        return data;
    }

    /**
     * Tell something to the player
     *
     * @param message the message to show up to the player
     */
    @Override
    public void notifyPlayer(String message) {
        synchronized (this.lock) {
            System.out.println(message);
        }
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
     * Render the homepage of the cli
     */
    @Override
    public void renderHomePage() {
        PersonalBoardPrinter.printPersonalBoard(model, model.getMe(), model.getLeader(model.getMe()), model.getDevelop(model.getMe()));
    }

    /**
     * read data from command line when needed
     */
    @Override
    public void run() {
        while (true) {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    System.out.println("miseriaccia harry");
                    return;
                }

                System.out.print("> ");
                data = Arrays.asList(new Scanner(System.in).nextLine().split(" "));

                lock.notifyAll();
            }
        }
    }

    /**
     * Render a view of the leader cards of the player
     */
    @Override
    public void renderLeaderCards() {
        ShowLeaderCards.printLeaderCardsPlayer(model.getLeader(model.getMe()));
    }

    /**
     * show an error to the player
     *
     * @param errorMessage the error message
     */
    @Override
    public void notifyPlayerError(String errorMessage) {
        synchronized (this.lock) {
            System.out.println(TextColors.colorText(TextColors.RED_BRIGHT, errorMessage));
        }
    }

    /**
     * notify a warning message to the player
     *
     * @param s the waring message
     */
    @Override
    public void notifyPlayerWarning(String s) {
        synchronized (this.lock) {
            System.out.println(TextColors.colorText(TextColors.YELLOW_BRIGHT, s));
        }
    }

    /**
     * Render a view of the warehouse
     */
    @Override
    public void renderWarehouse(String nickname) {
        WarehousePrinter.printWarehouse(model, nickname);
    }

    /**
     * Render a list of available commands
     */
    @Override
    public void renderHelp() {
        synchronized (this.lock) {
            Scanner scanner = null;
            try {
                scanner = new Scanner(getClass().getResourceAsStream("/cliHelp.txt"));
            } catch (NullPointerException e) {
                System.out.println("no help sorry");
                return;
            }

            String line = "";
            boolean print = true;
            while (print) {
                try {
                    line = scanner.nextLine();
                } catch (NoSuchElementException end) {
                    print = false;
                } finally {
                    System.out.println(line);
                }
            }
        }
    }

    @Override
    public String askUser(String request) throws InterruptedException {
        return null;
    }

    public static void main(String[] args) {
        try {
            CLI cli  = new CLI();
            Thread client = new Thread(new Client(cli));
            client.setDaemon(true);
            client.start();
            client.join();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
