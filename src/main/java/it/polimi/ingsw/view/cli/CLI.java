package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.communication.Disconnectable;
import it.polimi.ingsw.communication.VirtualSocket;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.*;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.litemodel.LiteModelUpdater;
import it.polimi.ingsw.litemodel.litecards.LiteLeaderCard;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.MarbleColor;
import it.polimi.ingsw.model.match.markettray.RowCol;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.NormalProduction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.cli.printer.*;
import it.polimi.ingsw.view.cli.printer.cardprinter.DevSetupPrinter;
import it.polimi.ingsw.view.cli.printer.cardprinter.ShowLeaderCards;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.*;

public class CLI implements View, Disconnectable {

    public static Map<ResourceType, String> colorResource = new EnumMap<ResourceType, String>(ResourceType.class) {{
        put(ResourceType.COIN, Colors.color(Colors.YELLOW_BRIGHT, "©"));
        put(ResourceType.SHIELD, Colors.color(Colors.BLUE_BRIGHT, "▼"));
        put(ResourceType.SERVANT, Colors.color(Colors.PURPLE, "Õ"));
        put(ResourceType.STONE, Colors.color(Colors.WHITE, "■"));
        put(ResourceType.EMPTY, " ");
        put(ResourceType.UNKNOWN, Colors.color(Colors.WHITE_BRIGHT, "?"));
        put(ResourceType.FAITHPOINT, Colors.color(Colors.RED, "┼"));
    }};

    public static Map<MarbleColor, String> colorMarbles = new EnumMap<MarbleColor, String>(MarbleColor.class) {{
        put(MarbleColor.YELLOW, Colors.color(Colors.YELLOW_BRIGHT, "●"));
        put(MarbleColor.BLUE, Colors.color(Colors.BLUE_BRIGHT, "●"));
        put(MarbleColor.PURPLE, Colors.color(Colors.PURPLE, "●"));
        put(MarbleColor.WHITE, Colors.color(Colors.WHITE_BRIGHT, "●"));
        put(MarbleColor.GRAY, Colors.color(Colors.WHITE, "●"));
        put(MarbleColor.RED, Colors.color(Colors.RED, "●"));
    }};

    public final LiteModel model = new LiteModel();
    public final FaithTrackPrinter faithTrackPrinter = new FaithTrackPrinter();
    public final VirtualSocket socket;

    private CliState state = new CliInitialState();

    public final Object printerLock = new Object();

    private boolean connected;

    public CLI(String address, int port) throws IOException {
        titleName();
        System.out.println(Colors.color(Colors.YELLOW_BRIGHT, "\nAt the start of the game, you have to discard two leader cards and, based on your position in the sequence, you can select up to 2 initial resources.\nAfter that, end your turn. You can type \"help\" to see again the possible moves.\n"));

        socket = new VirtualSocket(new Socket(address, port));
        connected = true;
    }

    private static void titleName(){
        String title = "\n" +
                Colors.casualColorYellow("                                                   ███╗   ███╗ █████╗ ███████╗████████╗███████╗██████╗ ███████╗               \n") +
                Colors.casualColorYellow("                                                   ████╗ ████║██╔══██╗██╔════╝╚══██╔══╝██╔════╝██╔══██╗██╔════╝               \n") +
                Colors.casualColorYellow("                                                   ██╔████╔██║███████║███████╗   ██║   █████╗  ██████╔╝███████╗               \n") +
                Colors.casualColorYellow("                                                   ██║╚██╔╝██║██╔══██║╚════██║   ██║   ██╔══╝  ██╔══██╗╚════██║               \n") +
                Colors.casualColorYellow("                                                   ██║ ╚═╝ ██║██║  ██║███████║   ██║   ███████╗██║  ██║███████║               \n") +
                Colors.casualColorYellow("                                                   ╚═╝     ╚═╝╚═╝  ╚═╝╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝╚══════╝               \n") +
                Colors.casualColorYellow("                                                                                                                              \n") +
                Colors.casualColorYellow("                                                                            ██████╗ ███████╗                                  \n") +
                Colors.casualColorYellow("                                                                           ██╔═══██╗██╔════╝                                  \n") +
                Colors.casualColorYellow("                                                                           ██║   ██║█████╗                                    \n") +
                Colors.casualColorYellow("                                                                           ██║   ██║██╔══╝                                    \n") +
                Colors.casualColorYellow("                                                                           ╚██████╔╝██║                                       \n") +
                Colors.casualColorYellow("                                                                            ╚═════╝ ╚═╝                                       \n") +
                Colors.casualColorYellow("                                                                                                                              \n") +
                Colors.casualColorYellow("                                       ██████╗ ███████╗███╗   ██╗ █████╗ ██╗███████╗███████╗ █████╗ ███╗   ██╗ ██████╗███████╗\n") +
                Colors.casualColorYellow("                                       ██╔══██╗██╔════╝████╗  ██║██╔══██╗██║██╔════╝██╔════╝██╔══██╗████╗  ██║██╔════╝██╔════╝\n") +
                Colors.casualColorYellow("                                       ██████╔╝█████╗  ██╔██╗ ██║███████║██║███████╗███████╗███████║██╔██╗ ██║██║     █████╗  \n") +
                Colors.casualColorYellow("                                       ██╔══██╗██╔══╝  ██║╚██╗██║██╔══██║██║╚════██║╚════██║██╔══██║██║╚██╗██║██║     ██╔══╝  \n") +
                Colors.casualColorYellow("                                       ██║  ██║███████╗██║ ╚████║██║  ██║██║███████║███████║██║  ██║██║ ╚████║╚██████╗███████╗\n") +
                Colors.casualColorYellow("                                       ╚═╝  ╚═╝╚══════╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═══╝ ╚═════╝╚══════╝\n") +
                "                                                                                                                              ";
        System.out.println(title);
    }

    /**
     * Tell something to the player
     *
     * @param message the message to show up to the player
     */
    public void notifyPlayer(String message) {
        synchronized (printerLock) {
            System.out.println(message);
        }
    }

    /**
     * Show to the player a server reply
     *
     * @param reply the reply to show to the player
     */
    public void notifyServerReply(String reply) {
        synchronized (printerLock) {
            System.out.println(Colors.color(Colors.CYAN, reply));
        }
    }

    /**
     * show an error to the player
     *
     * @param errorMessage the error message
     */
    public void notifyPlayerError(String errorMessage) {
        synchronized (printerLock) {
            System.out.println(Colors.color(Colors.RED_BRIGHT, errorMessage));
        }
    }

    /**
     * notify a warning message to the player
     *
     * @param s the waring message
     */
    public void notifyPlayerWarning(String s) {
        synchronized (printerLock) {
            System.out.println(Colors.color(Colors.YELLOW_BRIGHT, s));
        }
    }

    /**
     * return the liteModel of the view
     *
     * @return the model of the view
     */
    @Override
    public LiteModel getModel() {
        return this.model;
    }

    /**
     * read data from command line when needed
     */
    public void run() {
        new Thread(this.socket).start();
        new Thread(new LiteModelUpdater(this.socket, this.model, this)).start();
        socket.pinger(this);

        while(connected){
            try {
                state.start(this);
            } catch (InterruptedException e) {
                System.out.println("miseriaccia harry... thread fail");
                return;
            }
        }

        System.out.println(Colors.color(Colors.WHITE, "Quitting..."));
    }

    /**
     * Render a list of available commands
     */
    public void renderHelp() {
        InputStream help = getClass().getResourceAsStream("/cliHelp.txt");

        if (help == null) {
            System.out.println("No help sorry :(");
            return;
        }

        Scanner scanner = new Scanner(help);
        boolean print = true;

        synchronized (printerLock) {
            while (print) {
                try {
                    System.out.println(scanner.nextLine());
                } catch (NoSuchElementException end) {
                    print = false;
                }
            }
        }
    }

    public void setState(CliState state) {
        this.state = state;
    }

    public static void main(String[] args) {
        try {
            CLI cli  = new CLI("localhost", 4444);
            Thread client = new Thread(cli);
            client.setDaemon(true);
            client.start();
            client.join();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void handleDisconnection() {
        this.connected = false;
        notifyPlayerError("disconnected");

    }

    @Override
    public VirtualSocket disconnectableSocket() {
        return this.socket;
    }
}

abstract class CliState {

    /**
     * The next state mapped on the server response
     */
    protected final Map<HeaderTypes, CliState> nextState = new EnumMap<>(HeaderTypes.class);


    /**
     * Do some stuff that generate a packet to send to the server
     * @return packet to send to the server
     */
    protected abstract Packet generatePacket(CLI context);

    public void start(CLI context) throws InterruptedException {
        context.socket.send(generatePacket(context));

        Packet response = context.socket.pollPacketFrom(ChannelTypes.PLAYER_ACTIONS);
        context.notifyServerReply(response.body);

        context.setState(this.nextState.containsKey(response.header) ?
                this.nextState.get(response.header):
                new CliErrorState()
        );
    }
}

class CliErrorState extends CliState {

    /**
     * Do some stuff that generate a packet to send to the server
     *
     * @param context context of the state
     * @return packet to send to the server
     */
    @Override
    protected Packet generatePacket(CLI context) {
        synchronized (context.printerLock) {
            context.notifyPlayer("error");
            System.exit(-1);
        }
        return null;
    }
}

class CliInitialState extends CliState {

    public CliInitialState() {
        super();
        this.nextState.put(HeaderTypes.INVALID, this);
        this.nextState.put(HeaderTypes.RECONNECTED, new CliInGameState());
        this.nextState.put(HeaderTypes.GAME_INIT, new CliInitGameState());
        this.nextState.put(HeaderTypes.SET_PLAYERS_NUMBER, new CliSetPlayersNumberState());
    }

    @Override
    protected Packet generatePacket(CLI context) {
        boolean illegal = true;
        String nick = "";

        while (illegal) {
            try {
                context.notifyPlayer("Choose your nickname:");
                nick = new Scanner(System.in).nextLine();
                illegal = false;
            } catch (NoSuchElementException out) {
                context.notifyPlayerError("You have to insert a nickname");
            }
        }

        context.model.setMyNickname(nick);
        return new Packet(HeaderTypes.HELLO, ChannelTypes.PLAYER_ACTIONS, nick);
    }
}

class CliSetPlayersNumberState extends CliState {

    public CliSetPlayersNumberState() {
        super();
        this.nextState.put(HeaderTypes.RECONNECTED, new CliInGameState());
        this.nextState.put(HeaderTypes.GAME_INIT, new CliInitGameState());
        this.nextState.put(HeaderTypes.INVALID, this);
    }

    /**
     * Do some stuff that generate a packet to send to the server
     *
     * @param context context of the state
     * @return packet to send to the server
     */
    @Override
    protected Packet generatePacket(CLI context) {
        int number = -1;
        boolean illegal = true;

        while (illegal) {
            context.notifyPlayer("How many players?");
            String data = new Scanner(System.in).nextLine();

            try {
                if(data.equals("")) throw new IndexOutOfBoundsException();
                number = Integer.parseInt(data);
                illegal = false;
            }
            catch (IndexOutOfBoundsException | NullPointerException e) {
                context.notifyPlayerError("You have to insert a number");
            }
            catch (NumberFormatException e) {
                context.notifyPlayerError(data + " is not a number");
            }
        }

        return new Packet(HeaderTypes.SET_PLAYERS_NUMBER, ChannelTypes.PLAYER_ACTIONS, String.valueOf(number));
    }
}

class CliInGameState extends CliState {

    public CliInGameState() {
        super();
        this.nextState.put(HeaderTypes.OK, this);
        this.nextState.put(HeaderTypes.INVALID, this);
        this.nextState.put(HeaderTypes.END_GAME, new CliEndGameState());
    }

    /**
     * Do some stuff that generate a packet to send to the server
     *
     * @param context context of the state
     * @return packet to send to the server
     */
    @Override
    protected Packet generatePacket(CLI context) {
        while (true) {
            context.notifyPlayer("Choose an action. Type help for commands.");
            List<String> data = Arrays.asList(new Scanner(System.in).nextLine().split(" "));

            int i = 1;
            switch (data.get(0).toLowerCase()) {
                case "help":
                    context.renderHelp();
                    break;

                case "done":
                    return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new EndTurnCommand().jsonfy());

                case "buycard" :
                    try {
                        LevelDevCard level = LevelDevCard.valueOf(data.get(i++).toUpperCase());
                        ColorDevCard color = ColorDevCard.valueOf(data.get(i++).toUpperCase());
                        DevCardSlot slot = DevCardSlot.valueOf(data.get(i++).toUpperCase());
                        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new BuyDevCardCommand(level, color, slot).jsonfy());
                    } catch (IllegalArgumentException out) {
                        context.notifyPlayerError(data.get(i) + " is not mappable");
                    } catch (IndexOutOfBoundsException arg) {
                        context.notifyPlayerError("You missed some parameter");
                    }
                    break;

                case "chooseresource":
                    i = 0;
                    try {
                        DepotSlot dest = DepotSlot.valueOf(data.get(i++).toUpperCase());
                        ResourceType res = ResourceType.valueOf(data.get(i++).toUpperCase());
                        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new ChooseResourceCommand(dest, res).jsonfy());
                    } catch (IndexOutOfBoundsException e) {
                        context.notifyPlayerError("You missed some parameters");
                    } catch (IllegalArgumentException e) {
                        context.notifyPlayerError(data.get(i) + " is not mappable");
                    }
                    break;

                case "usemarket":
                    try {
                        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new UseMarketTrayCommand(
                                RowCol.valueOf(data.get(i++).toUpperCase()),
                                Integer.parseInt(data.get(i))-1
                        ).jsonfy());
                    } catch (IndexOutOfBoundsException out) {
                        context.notifyPlayerError("you missed some parameter");
                    } catch (NumberFormatException number) {
                        context.notifyPlayerError(data.get(2) + " is not a number!");
                    } catch (IllegalArgumentException arg) {
                        context.notifyPlayerError("some parameter in the command is not correct");
                    }
                    break;

                case "moveinproduction":
                case "movep":
                    try {
                        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new MoveInProductionCommand(
                                DepotSlot.valueOf(data.get(i++).toUpperCase()),
                                ProductionID.valueOf(data.get(i++).toUpperCase()),
                                ResourceBuilder.buildFromType(ResourceType.valueOf(data.get(i++).toUpperCase()), Integer.parseInt(data.get(i)))
                        ).jsonfy());
                    } catch (NumberFormatException number) {
                        context.notifyPlayerError(data.get(4) + " Is not a number");
                    } catch (IllegalArgumentException arg) {
                        context.notifyPlayerError(data.get(i) + " Doesn't exist. Type help for accepted values");
                    } catch (IndexOutOfBoundsException out) {
                        context.notifyPlayerError("You missed some parameter");
                    }
                    break;

                case "moveindepot":
                case "move":
                    try {
                        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new MoveDepotCommand(
                                DepotSlot.valueOf(data.get(i++).toUpperCase()),
                                DepotSlot.valueOf(data.get(i++).toUpperCase()),
                                ResourceBuilder.buildFromType(ResourceType.valueOf(data.get(i++).toUpperCase()), Integer.parseInt(data.get(i)))
                        ).jsonfy());
                    } catch (IndexOutOfBoundsException out) {
                        context.notifyPlayerError("You missed some parameter ...");
                    } catch (NumberFormatException number) {
                        context.notifyPlayerError(data.get(4) + " Is not a number!");
                    } catch (IllegalArgumentException arg) {
                        context.notifyPlayerError(data.get(i) + " Doesn't exist. Type help for accepted values");
                    }
                    break;

                case "activateproduction":
                    return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new ActivateProductionCommand().jsonfy());

                case "discardleader":
                case "discard":
                    try {
                        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new DiscardLeaderCommand("LC" + Integer.parseInt(data.get(1))).jsonfy());
                    } catch (IndexOutOfBoundsException out) {
                        context.notifyPlayerError("you need to insert the leader id");
                    } catch (NumberFormatException number) {
                        context.notifyPlayerError(data.get(1) + " Is not a number!");
                    }
                    break;

                case "activateleader":
                    try {
                        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new ActivateLeaderCommand("LC" + Integer.parseInt(data.get(1))).jsonfy());
                    } catch (IndexOutOfBoundsException out) {
                        context.notifyPlayerError("You need to insert the leader id");
                    } catch (NumberFormatException number) {
                        context.notifyPlayerError(data.get(1) + " Is not a number!");
                    }
                    break;

                case "normalizeproduction":
                    i = 1;
                    try {
                        ProductionID prod = ProductionID.valueOf(data.get(i++).toUpperCase());

                        List<Resource> required = new ArrayList<>();
                        while (!data.get(i).equals("x")) {
                            required.add(ResourceBuilder.buildFromType(
                                    ResourceType.valueOf(data.get(i++).toUpperCase()),
                                    Integer.parseInt(data.get(i++))
                            ));
                        }

                        List<Resource> output = new ArrayList<>();
                        while (!data.get(i).equals("x")) {
                            output.add(ResourceBuilder.buildFromType(
                                    ResourceType.valueOf(data.get(i++).toUpperCase()),
                                    Integer.parseInt(data.get(i++))
                            ));
                        }

                        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new SetNormalProductionCommand(
                                prod,
                                new NormalProduction(required, output)
                        ).jsonfy());
                    } catch (IllegalTypeInProduction ill) {
                        context.notifyPlayerError(ill.getMessage());
                    } catch (IndexOutOfBoundsException out) {
                        context.notifyPlayerError("You missed some parameter ...");
                    } catch (NumberFormatException number) {
                        context.notifyPlayerError(data.get(i - 1) + " Is not a number!");
                    } catch (IllegalArgumentException arg) {
                        context.notifyPlayerError(data.get(i - 1) + " Doesn't exist. Type help for accepted values");
                    }
                    break;

                case "paintmarble":
                    try {
                        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new PaintMarbleCommand(
                                Integer.parseInt(data.get(i++)),
                                Integer.parseInt(data.get(i))
                        ).jsonfy());
                    } catch (IndexOutOfBoundsException out) {
                        context.notifyPlayerError("You missed some parameter ...");
                    } catch (NumberFormatException number) {
                        context.notifyPlayerError(data.get(i) + " Is not a number!");
                    }
                    break;

                case "viewleader":
                case "leadercards":
                case "leader":
                    List<LiteLeaderCard> temp = context.model.getLeader(context.model.getMe());
                    if(temp == null || temp.isEmpty()){
                        context.notifyPlayerError("You don't have any leader cards!");
                        break;
                    }
                    ShowLeaderCards.printLeaderCardsPlayer(temp);
                    break;

                case "viewpersonalboard":
                case "pb":
                case "personalboard":
                    try {
                        PersonalBoardPrinter.printPersonalBoard(context.model, data.get(1));
                    } catch (IndexOutOfBoundsException out) {
                        PersonalBoardPrinter.printPersonalBoard(context.model, context.model.getMe());
                    } catch (NullPointerException n) {
                        context.notifyPlayerError(data.get(i) + " player doesn't exist");
                    }
                    break;

                case "viewcardmarket":
                case "cardmarket":
                case "devsetup":
                    DevSetupPrinter.printDevSetup(context.getModel().getDevSetup());
                    break;

                case "warehouse":
                case "wh":
                case "viewwarehouse":
                    try {
                        WarehousePrinter.printWarehouse(context.model, data.get(1));
                    } catch (IndexOutOfBoundsException out) {
                        WarehousePrinter.printWarehouse(context.model, context.model.getMe());
                    } catch (NullPointerException nul) {
                        context.notifyPlayerError("player doesn't exist");
                    }
                    break;

                case "tray":
                case "market":
                    MarketTrayPrinter.printMarketTray(context.model.getTray());
                    break;

                case "track":
                case "faithtrack":
                    context.faithTrackPrinter.printTrack(context.model);
                    break;

                default:
                    context.notifyPlayerError("Unknown command, try again!");
                    break;
            }
        }
    }
}

class CliInitGameState extends CliState {

    public CliInitGameState() {
        super();
        this.nextState.put(HeaderTypes.INVALID, this);
        this.nextState.put(HeaderTypes.OK, this);
        this.nextState.put(HeaderTypes.GAME_START, new CliInGameState());
    }

    /**
     * Do some stuff that generate a packet to send to the server
     *
     * @param context
     * @return packet to send to the server
     */
    @Override
    protected Packet generatePacket(CLI context) {
        while (true) {
            context.notifyPlayer("You have to discard leader cards and choose resource");
            List<String> data = Arrays.asList(new Scanner(System.in).nextLine().split(" "));

            int i = 1;
            switch (data.get(0).toLowerCase()) {
                case "help":
                    System.out.println("you can " + Colors.color(Colors.GREEN_BRIGHT, "discardleader") + " or " + Colors.color(Colors.GREEN_BRIGHT, "chooseres"));
                    break;

                case "leader":
                case "viewleader":
                case "leadercards":
                    List<LiteLeaderCard> temp = context.model.getLeader(context.model.getMe());
                    if(temp == null || temp.isEmpty()){
                        context.notifyPlayerError("You don't have any leader cards!");
                        break;
                    }
                    ShowLeaderCards.printLeaderCardsPlayer(temp);
                    break;

                case "resource":
                case "chooseres":
                case "chooseresource":
                    i = 0;
                    try {
                        DepotSlot dest = DepotSlot.valueOf(data.get(i++).toUpperCase());
                        ResourceType res = ResourceType.valueOf(data.get(i++).toUpperCase());
                        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new ChooseResourceCommand(dest, res).jsonfy());
                    } catch (IndexOutOfBoundsException e) {
                        context.notifyPlayerError("You missed some parameters");
                    } catch (IllegalArgumentException e) {
                        context.notifyPlayerError(data.get(i) + " is not mappable");
                    }
                    break;

                case "discardleader":
                case "discard":
                    try {
                        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new DiscardLeaderCommand("LC" + Integer.parseInt(data.get(1))).jsonfy());
                    } catch (IndexOutOfBoundsException out) {
                        context.notifyPlayerError("you need to insert the leader id");
                    } catch (NumberFormatException number) {
                        context.notifyPlayerError(data.get(1) + " Is not a number!");
                    }
                    break;

                default:
                    System.out.println(Colors.color(Colors.RED_BRIGHT, "unknown input, type help for available commands"));
            }
        }
    }
}

class CliEndGameState extends CliState {
    /**
     * Do some stuff that generate a packet to send to the server
     *
     * @param context the context of the state
     * @return packet to send to the server
     */
    @Override
    protected Packet generatePacket(CLI context) {
        System.out.println("The game is ended");
        return null;
    }
}
