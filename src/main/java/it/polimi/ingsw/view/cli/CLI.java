package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.communication.SocketListener;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.*;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.litemodel.Scoreboard;
import it.polimi.ingsw.litemodel.litecards.LiteLeaderCard;
import it.polimi.ingsw.litemodel.litecards.LiteSoloActionToken;
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
import it.polimi.ingsw.view.ClientPacketHandler;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.cli.printer.*;
import it.polimi.ingsw.view.cli.printer.cardprinter.DevSetupPrinter;
import it.polimi.ingsw.view.cli.printer.cardprinter.ShowLeaderCards;
import it.polimi.ingsw.view.cli.printer.cardprinter.SoloActionTokenPrinter;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.*;

/**
 * This is the class that manages the Command Line Interface
 */
public class CLI implements View {

    /**
     * This attribute maps the ResourceType to a ASCII symbol
     */
    public static Map<ResourceType, String> colorResource = new EnumMap<>(ResourceType.class) {{
        put(ResourceType.COIN, Colors.color(Colors.YELLOW_BRIGHT, "©"));
        put(ResourceType.SHIELD, Colors.color(Colors.BLUE_BRIGHT, "▼"));
        put(ResourceType.SERVANT, Colors.color(Colors.PURPLE, "Õ"));
        put(ResourceType.STONE, Colors.color(Colors.WHITE, "■"));
        put(ResourceType.EMPTY, " ");
        put(ResourceType.UNKNOWN, Colors.color(Colors.WHITE_BRIGHT, "?"));
        put(ResourceType.FAITHPOINT, Colors.color(Colors.RED, "┼"));
    }};

    /**
     * This attribute maps the MarbleColor to a ASCII symbol
     */
    public static Map<MarbleColor, String> colorMarbles = new EnumMap<>(MarbleColor.class) {{
        put(MarbleColor.YELLOW, Colors.color(Colors.YELLOW_BRIGHT, "●"));
        put(MarbleColor.BLUE, Colors.color(Colors.BLUE_BRIGHT, "●"));
        put(MarbleColor.PURPLE, Colors.color(Colors.PURPLE, "●"));
        put(MarbleColor.WHITE, Colors.color(Colors.WHITE_BRIGHT, "●"));
        put(MarbleColor.GRAY, Colors.color(Colors.WHITE, "●"));
        put(MarbleColor.RED, Colors.color(Colors.RED, "●"));
    }};

    /**
     * This attribute is the model
     */
    public final LiteModel model = new LiteModel();

    /**
     * This attribute is the Printer of the FaithTrack
     */
    public final FaithTrackPrinter faithTrackPrinter = new FaithTrackPrinter();

    /**
     * This attribute is the SocketListener that manage the packets
     */
    public final SocketListener socket;

    /**
     * This attribute is the state of the Client in CLI mode
     */
    private CliState state;

    /**
     * This attribute is the lock to synchronize the Printers
     */
    public final Object printerLock = new Object();

    /**
     * This is the constructor of the class
     * @param address is the IP address
     * @param port is the port of the Server
     * @throws IOException if an I/O error occurs when creating the socket.
     */
    public CLI(String address, int port) throws IOException {
        titleName();
        System.out.println(Colors.color(Colors.YELLOW_BRIGHT, "\nAt the start of the game, you have to discard two leader cards and, based on your position in the sequence, you can select up to 2 initial resources.\nAfter that, end your turn. You can type \"help\" to see again the possible moves.\n"));

        socket = new SocketListener(new Socket(address, port));
        setState(new ChooseNicknameCLI(this));
    }

    /**
     * This method prints the title of the Game
     */
    private static void titleName(){
        String title = "\n" +
                Colors.casualColorYellow("                                             ███╗   ███╗ █████╗ ███████╗████████╗███████╗██████╗ ███████╗               \n") +
                Colors.casualColorYellow("                                             ████╗ ████║██╔══██╗██╔════╝╚══██╔══╝██╔════╝██╔══██╗██╔════╝               \n") +
                Colors.casualColorYellow("                                             ██╔████╔██║███████║███████╗   ██║   █████╗  ██████╔╝███████╗               \n") +
                Colors.casualColorYellow("                                             ██║╚██╔╝██║██╔══██║╚════██║   ██║   ██╔══╝  ██╔══██╗╚════██║               \n") +
                Colors.casualColorYellow("                                             ██║ ╚═╝ ██║██║  ██║███████║   ██║   ███████╗██║  ██║███████║               \n") +
                Colors.casualColorYellow("                                             ╚═╝     ╚═╝╚═╝  ╚═╝╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝╚══════╝               \n") +
                Colors.casualColorYellow("                                                                                                                        \n") +
                Colors.casualColorYellow("                                                                      ██████╗ ███████╗                                  \n") +
                Colors.casualColorYellow("                                                                     ██╔═══██╗██╔════╝                                  \n") +
                Colors.casualColorYellow("                                                                     ██║   ██║█████╗                                    \n") +
                Colors.casualColorYellow("                                                                     ██║   ██║██╔══╝                                    \n") +
                Colors.casualColorYellow("                                                                     ╚██████╔╝██║                                       \n") +
                Colors.casualColorYellow("                                                                      ╚═════╝ ╚═╝                                       \n") +
                Colors.casualColorYellow("                                                                                                                        \n") +
                Colors.casualColorYellow("                                 ██████╗ ███████╗███╗   ██╗ █████╗ ██╗███████╗███████╗ █████╗ ███╗   ██╗ ██████╗███████╗\n") +
                Colors.casualColorYellow("                                 ██╔══██╗██╔════╝████╗  ██║██╔══██╗██║██╔════╝██╔════╝██╔══██╗████╗  ██║██╔════╝██╔════╝\n") +
                Colors.casualColorYellow("                                 ██████╔╝█████╗  ██╔██╗ ██║███████║██║███████╗███████╗███████║██╔██╗ ██║██║     █████╗  \n") +
                Colors.casualColorYellow("                                 ██╔══██╗██╔══╝  ██║╚██╗██║██╔══██║██║╚════██║╚════██║██╔══██║██║╚██╗██║██║     ██╔══╝  \n") +
                Colors.casualColorYellow("                                 ██║  ██║███████╗██║ ╚████║██║  ██║██║███████║███████║██║  ██║██║ ╚████║╚██████╗███████╗\n") +
                Colors.casualColorYellow("                                 ╚═╝  ╚═╝╚══════╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═══╝ ╚═════╝╚══════╝\n") +
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
     * return the liteModel of the view
     *
     * @return the model of the view
     */
    @Override
    public LiteModel getModel() {
        return this.model;
    }

    /**
     * This method prints the SoloActionToken in singleplayer
     * @param token token to show
     */
    @Override
    public void popUpLorenzoMoves(LiteSoloActionToken token) {
        try {
            synchronized (printerLock) {
                SoloActionTokenPrinter.printSoloActionToken(token);
            }
        } catch (NullPointerException nul) {
            notifyPlayerError("No token to visualize");
        }
    }

    /**
     * This method closes the application
     * @param message is the error message
     */
    @Override
    public void emergencyExit(String message) {
        notifyPlayerError(message);
        System.exit(0);
    }

    /**
     * read data from command line when needed
     */
    @Override
    public void start() {
        // start socket
        new Thread(socket).start();

        // keyboard listener
        new Thread(() -> {
            while (true) handleUserInput(new Scanner(System.in).nextLine());
        }).start();

        // start packet handler
        new ClientPacketHandler(this, socket).start();
    }

    /**
     * This method manages the input of the User
     * @param nextLine is the message of the User
     */
    private void handleUserInput(String nextLine) {
        state.handleInput(nextLine);
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

    /**
     * This method do nothing
     */
    @Override
    public void refresh() {
        //notifyPlayer(state.entryMessage);
    }

    /**
     * This method sets the Client State
     * @param state is the next state of the Player
     */
    public void setState(CliState state) {
        this.state = state;
        notifyPlayer("");
        state.onEntry();
    }

    /**
     * This method sets the state of the Client in CliSetPlayersNumberState
     */
    @Override
    public void fireGameCreator() {
        setState(new CliSetPlayersNumberState(this));
    }

    /**
     * This method sets the state of the Client in CliLobbyWaitState
     */
    @Override
    public void fireLobbyWait() {
        setState(new CliLobbyWaitState(this));
    }

    /**
     * This method sets the state of the Client in CliInitGameState
     */
    @Override
    public void fireGameInit() {
        setState(new CliInitGameState(this));
    }

    /**
     * This method sets the state of the Client in CliInGameState
     */
    @Override
    public void fireGameSession() {
        setState(new CliInGameState(this));
    }

    /**
     * This method sets the state of the Client in CliWaitResultState
     */
    @Override
    public void fireGameEnded() {
        setState(new CliWaitResultState(this));
    }

    /**
     * This method sets the state of the Client in CliResultState
     */
    @Override
    public void fireGameResult() {
        setState(new CliResultState(this));
    }

    public static void main(String[] args) {
        try {
            new CLI("localhost", 4444).start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

/**
 * This class manages the Client States in CLI mode
 */
abstract class CliState {

    /**
     * This attribute is the CLI
     */
    protected final CLI context;

    /**
     * This method manages the input of the user
     * @param userInput is the user's input
     */
    public abstract void handleInput(String userInput);

    /**
     * Function called on the entry of the state
     */
    protected abstract void onEntry();

    /**
     * This method is the constructor of the class
     * @param context is the CLI that will change its state
     */
    protected CliState(CLI context) {
        this.context = context;
    }
}

/**
 * This class is the State of the CLI where the Player waits the end of the game
 */
class CliWaitResultState extends CliState {

    /**
     * This is the constructor of the class
     * @param context is the CLI that will changes its state
     */
    CliWaitResultState(CLI context) {
        super(context);
    }

    @Override
    public void onEntry() {
        context.notifyPlayer("You ended you game, wait other player do their moves.");
    }

    /**
     * This method manages the user's input
     * @param userInput is the user's input
     */
    @Override
    public void handleInput(String userInput) {
        List<String> data = Arrays.asList(userInput.split(" "));

        int i = 1;
        switch (data.get(0).toLowerCase()) {
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

            case "track":
            case "faithtrack":
                context.faithTrackPrinter.printTrack(context.model);
                break;

            default:
                context.notifyPlayerError("Unknown command! You can only view personal boards or the faith track");
                break;
        }
    }
}

/**
 * This class is the State of the CLI where the Player choose his nickname
 */
class ChooseNicknameCLI extends CliState {

    /**
     * This is the constructor of the class
     * @param context is the CLI that will changes its state
     */
    ChooseNicknameCLI(CLI context) {
        super(context);
    }

    @Override
    public void onEntry() {
        context.notifyPlayer("Type your nickname");
    }

    /**
     * This method manages the user's input
     * @param userInput is the user's input
     */
    @Override
    public void handleInput(String userInput) {
        context.model.setMyNickname(userInput);
        context.socket.send(new Packet(HeaderTypes.HELLO, ChannelTypes.PLAYER_ACTIONS, userInput));
    }
}

/**
 * This class is the State of the CLI where the Player choose the number of players of the match
 */
class CliSetPlayersNumberState extends CliState {

    /**
     * This is the constructor of the class
     * @param context is the CLI that will changes its state
     */
    CliSetPlayersNumberState(CLI context) {
        super(context);
    }

    @Override
    public void onEntry() {
        context.notifyPlayer("How many players?");
    }

    /**
     * This method manages the user's input
     * @param userInput is the user's input
     */
    @Override
    public void handleInput(String userInput) {
        int number;

        try {
            if(userInput.equals("")) throw new IndexOutOfBoundsException();
            number = Integer.parseInt(userInput);
            context.socket.send(new Packet(HeaderTypes.SET_PLAYERS_NUMBER, ChannelTypes.PLAYER_ACTIONS, String.valueOf(number)));
        }
        catch (IndexOutOfBoundsException | NullPointerException e) {
            context.notifyPlayerError("You have to insert a number");
        }
        catch (NumberFormatException e) {
            context.notifyPlayerError(userInput + " is not a number");
        }
    }
}

/**
 * This class is the State of the CLI where the Player waits for other players to connect to the match
 */
class CliLobbyWaitState extends CliState {

    /**
     * This is the constructor of the class
     * @param context is the CLI that will changes its state
     */
    CliLobbyWaitState(CLI context) {
        super(context);
    }

    @Override
    public void onEntry() {
        context.notifyPlayer("waiting for other players...");
    }

    /**
     * This method manages the user's input
     * @param userInput is the user's input
     */
    @Override
    public void handleInput(String userInput) {
        context.notifyPlayerError("Please wait for other players!");
    }
}

/**
 * This class is the State of the CLI where the Player can do all the main actions
 */
class CliInGameState extends CliState {

    /**
     * This is the constructor of the class
     * @param context is the CLI that will changes its state
     */
    CliInGameState(CLI context) {
        super(context);
    }

    @Override
    public void onEntry() {
        context.notifyPlayer("Choose an action. Type help for commands.");
    }

    /**
     * This method manages the user's input
     * @param userInput is the user's input
     */
    @Override
    public void handleInput(String userInput) {
        List<String> data = Arrays.asList(userInput.split(" "));

        int i = 1;
        switch (data.get(0).toLowerCase()) {
            case "help":
                context.renderHelp();
                break;

            case "done":
                context.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new EndTurnCommand().jsonfy()));
                break;

            case "buycard":
            case "buy" :
                try {
                    LevelDevCard level = LevelDevCard.valueOf(data.get(i++).toUpperCase());
                    ColorDevCard color = ColorDevCard.valueOf(data.get(i++).toUpperCase());
                    DevCardSlot slot = DevCardSlot.valueOf(data.get(i++).toUpperCase());
                    context.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new BuyDevCardCommand(level, color, slot).jsonfy()));
                } catch (IllegalArgumentException out) {
                    context.notifyPlayerError(data.get(--i) + " is not mappable");
                } catch (IndexOutOfBoundsException arg) {
                    DevCardBufferPrinter.printDevCardPhase(context.getModel(), context.getModel().getMe());
                    context.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new BuyCardCommand().jsonfy()));
                }
                break;

            case "devbuffer":
            case "db":
                DevCardBufferPrinter.printDevCardPhase(context.getModel(), context.getModel().getMe());
                break;

            case "rollback":
            case "rb":
            case "return":
                context.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new ReturnCommand().jsonfy()));
                break;

            case "usemarket":
                try {
                    context.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new UseMarketTrayCommand(
                            RowCol.valueOf(data.get(i++).toUpperCase()),
                            Integer.parseInt(data.get(i))-1
                    ).jsonfy()));
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
                    context.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new MoveInProductionCommand(
                            DepotSlot.valueOf(data.get(i++).toUpperCase()),
                            ProductionID.valueOf(data.get(i++).toUpperCase()),
                            ResourceBuilder.buildFromType(ResourceType.valueOf(data.get(i++).toUpperCase()), Integer.parseInt(data.get(i)))
                    ).jsonfy()));
                } catch (NumberFormatException number) {
                    context.notifyPlayerError(data.get(4) + " Is not a number");
                } catch (IllegalArgumentException arg) {
                    context.notifyPlayerError(data.get(--i) + " Doesn't exist. Type help for accepted values");
                } catch (IndexOutOfBoundsException out) {
                    context.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new ProductionCommand().jsonfy()));
                }
                break;

            case "moveindepot":
            case "move":
                try {
                    context.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new MoveDepotCommand(
                            DepotSlot.valueOf(data.get(i++).toUpperCase()),
                            DepotSlot.valueOf(data.get(i++).toUpperCase()),
                            ResourceBuilder.buildFromType(ResourceType.valueOf(data.get(i++).toUpperCase()), Integer.parseInt(data.get(i)))
                    ).jsonfy()));
                } catch (IndexOutOfBoundsException out) {
                    context.notifyPlayerError("You missed some parameter ...");
                } catch (NumberFormatException number) {
                    context.notifyPlayerError(data.get(4) + " is not a number!");
                } catch (IllegalArgumentException arg) {
                    context.notifyPlayerError(data.get(--i) + " Doesn't exist. Type help for accepted values");
                }
                break;

            case "production":
                context.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new ProductionCommand().jsonfy()));
                break;

            case "activateproduction":
            case "actprod":
                context.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new ActivateProductionCommand().jsonfy()));
                break;

            case "discardleader":
            case "discard":
                try {
                    context.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new DiscardLeaderCommand("LC" + Integer.parseInt(data.get(1))).jsonfy()));
                } catch (IndexOutOfBoundsException out) {
                    context.notifyPlayerError("you need to insert the leader id");
                } catch (NumberFormatException number) {
                    context.notifyPlayerError(data.get(1) + " is not a number!");
                }
                break;

            case "activateleader":
            case "actleader":
                try {
                    context.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new ActivateLeaderCommand("LC" + Integer.parseInt(data.get(1))).jsonfy()));
                } catch (IndexOutOfBoundsException out) {
                    context.notifyPlayerError("You need to insert the leader id");
                } catch (NumberFormatException number) {
                    context.notifyPlayerError(data.get(1) + " is not a number!");
                }
                break;

            case "normalizeproduction":
            case "normalize":
                try {
                    ProductionID prod = ProductionID.valueOf(data.get(i++).toUpperCase());
                    if(!data.get(i++).equals("x")){ throw new IndexOutOfBoundsException();}
                    List<Resource> required = new ArrayList<>();
                    while (!data.get(i).equals("x")) {
                        required.add(ResourceBuilder.buildFromType(
                                ResourceType.valueOf(data.get(i++).toUpperCase()),
                                Integer.parseInt(data.get(i++))
                        ));
                    }

                    i++; // without this output will never be calculated
                    List<Resource> output = new ArrayList<>();
                    while (!data.get(i).equals("x")) {
                        output.add(ResourceBuilder.buildFromType(
                                ResourceType.valueOf(data.get(i++).toUpperCase()),
                                Integer.parseInt(data.get(i++))
                        ));
                    }
                    context.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new SetNormalProductionCommand(
                            prod,
                            new NormalProduction(required, output)
                    ).jsonfy()));
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
            case "paint":
                try {
                    context.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new PaintMarbleCommand(
                            Integer.parseInt(data.get(i++)),
                            Integer.parseInt(data.get(i))
                    ).jsonfy()));
                } catch (IndexOutOfBoundsException out) {
                    context.notifyPlayerError("You missed some parameter ...");
                } catch (NumberFormatException number) {
                    context.notifyPlayerError(data.get(--i) + " is not a number!");
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

            case "productions":
            case "prod":
                try {
                    ProductionPrinter.printProductions(context.model, data.get(1));
                } catch (IndexOutOfBoundsException out) {
                    ProductionPrinter.printProductions(context.model, context.model.getMe());
                } catch (NullPointerException nul) {
                    context.notifyPlayerError("player doesn't exist");
                }
                break;

            case "discounts":
            case "disc":
                CasualPrinter.printDiscounts(context.getModel().getDiscounts(context.getModel().getMe()));
                break;
            case "conversion":
            case "conv":
                CasualPrinter.printConversion(context.getModel().getConversion(context.getModel().getMe()));
                break;
            case "players":
                CasualPrinter.printPlayers(context.model);
                break;

            //----------------------CHEAT---------------------------
            case "rescheat":
                context.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new ResourceCheatCommand().jsonfy()));
                break;
            case "fpcheat":
                try {
                    context.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new FaithPointCheatCommand(Integer.parseInt(data.get(i))).jsonfy()));
                } catch (Exception e){
                    System.out.println("Insert a valid number!");
                }
                break;
            default:
            context.notifyPlayerError("Unknown command, try again!");
            break;
        }
    }
}

/**
 * This class is the State of the CLI where the Player choose LeaderCards and Resources
 */
class CliInitGameState extends CliState {

    /**
     * This is the constructor of the class
     * @param context is the CLI that will changes its state
     */
    CliInitGameState(CLI context) {
        super(context);
    }

    /**
     * This method show the LeaderCards of the Player when the game starts
     */
    @Override
    public void onEntry() {
        ShowLeaderCards.printLeaderCardsPlayer(context.model.getLeader(context.model.getMe()));
    }

    /**
     * This method manages the user's input
     * @param userInput is the user's input
     */
    @Override
    public void handleInput(String userInput) {
        List<String> data = Arrays.asList(userInput.split(" "));

        int i = 1;
        switch (data.get(0).toLowerCase()) {
            case "help":
                System.out.println("you can " + Colors.color(Colors.GREEN_BRIGHT, "discardleader [number]") + " or " + Colors.color(Colors.GREEN_BRIGHT, "chooseres [depot destination] [resource type]"));
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
                try {
                    DepotSlot dest = DepotSlot.valueOf(data.get(i++).toUpperCase());
                    ResourceType res = ResourceType.valueOf(data.get(i).toUpperCase());
                    context.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new ChooseResourceCommand(dest, res).jsonfy()));
                } catch (IndexOutOfBoundsException e) {
                    context.notifyPlayerError("You missed some parameters");
                } catch (IllegalArgumentException e) {
                    context.notifyPlayerError(data.get(--i) + " is not mappable");
                }
                break;

            case "discardleader":
            case "discard":
                try {
                    context.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new DiscardLeaderCommand("LC" + Integer.parseInt(data.get(1))).jsonfy()));
                } catch (IndexOutOfBoundsException out) {
                    context.notifyPlayerError("you need to insert the leader id");
                } catch (NumberFormatException number) {
                    context.notifyPlayerError(data.get(1) + " Is not a number!");
                }
                break;

            case "done":
            case "end":
                context.socket.send(new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new EndTurnCommand().jsonfy()));
                break;

            default:
                System.out.println(Colors.color(Colors.RED_BRIGHT, "unknown input, type help for available commands"));
                break;
        }
    }
}

/**
 * This class is the State of the CLI where the Player can see the Leaderboard
 */
class CliResultState extends CliState {

    /**
     * This is the constructor of the class
     * @param context is the CLI that will changes its state
     */
    CliResultState(CLI context) {
        super(context);
    }

    /**
     * This method shows to the player the Leaderboard
     */
    @Override
    public void onEntry() {
        context.notifyPlayer("Leaderboard\n");

        List<String> medals = new ArrayList<>();
        medals.add(Colors.YELLOW_BRIGHT);
        medals.add(Colors.WHITE);
        medals.add(Colors.YELLOW);
        medals.add(Colors.WHITE_BRIGHT);

        //context.notifyPlayer("pos - nickname - victory points - resource number\n");

        int pos = 0;
        for (Scoreboard.BoardEntry entry : context.model.getScoreboard().getBoard()) {
            context.notifyPlayer(Colors.color(medals.get(Math.min(pos, 3)),
                    (pos + 1) + "° - " +
                    entry.getNickname() + (entry.getScore() >= 0 ? " - VP: " + entry.getScore() : ""
            )));
            pos++;
        }
    }

    /**
     * This method manages the user's input
     * @param userInput is the user's input
     */
    @Override
    public void handleInput(String userInput) {
        if (userInput.equalsIgnoreCase("quit")) System.exit(0);
    }
}
