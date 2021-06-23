package it.polimi.ingsw.view.cli;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.communication.Disconnectable;
import it.polimi.ingsw.communication.SocketListener;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.*;
import it.polimi.ingsw.communication.packet.rendering.Lighter;
import it.polimi.ingsw.communication.packet.updates.Updater;
import it.polimi.ingsw.litemodel.LiteModel;
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

    public static Map<ResourceType, String> colorResource = new EnumMap<>(ResourceType.class) {{
        put(ResourceType.COIN, Colors.color(Colors.YELLOW_BRIGHT, "©"));
        put(ResourceType.SHIELD, Colors.color(Colors.BLUE_BRIGHT, "▼"));
        put(ResourceType.SERVANT, Colors.color(Colors.PURPLE, "Õ"));
        put(ResourceType.STONE, Colors.color(Colors.WHITE, "■"));
        put(ResourceType.EMPTY, " ");
        put(ResourceType.UNKNOWN, Colors.color(Colors.WHITE_BRIGHT, "?"));
        put(ResourceType.FAITHPOINT, Colors.color(Colors.RED, "┼"));
    }};

    public static Map<MarbleColor, String> colorMarbles = new EnumMap<>(MarbleColor.class) {{
        put(MarbleColor.YELLOW, Colors.color(Colors.YELLOW_BRIGHT, "●"));
        put(MarbleColor.BLUE, Colors.color(Colors.BLUE_BRIGHT, "●"));
        put(MarbleColor.PURPLE, Colors.color(Colors.PURPLE, "●"));
        put(MarbleColor.WHITE, Colors.color(Colors.WHITE_BRIGHT, "●"));
        put(MarbleColor.GRAY, Colors.color(Colors.WHITE, "●"));
        put(MarbleColor.RED, Colors.color(Colors.RED, "●"));
    }};

    public final LiteModel model = new LiteModel();
    public final FaithTrackPrinter faithTrackPrinter = new FaithTrackPrinter();
    public final SocketListener socket;

    private CliState state;

    public final Object printerLock = new Object();

    private boolean connected;

    public CLI(String address, int port) throws IOException {
        titleName();
        System.out.println(Colors.color(Colors.YELLOW_BRIGHT, "\nAt the start of the game, you have to discard two leader cards and, based on your position in the sequence, you can select up to 2 initial resources.\nAfter that, end your turn. You can type \"help\" to see again the possible moves.\n"));

        socket = new SocketListener(new Socket(address, port), this);
        connected = true;
        setState(new ChooseNicknameCLI(this));
    }

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
     * read data from command line when needed
     */
    public void start() {
        new Thread(this.socket).start();
        new Thread(() -> {
            while (true) handleUserInput(new Scanner(System.in).nextLine());
        }).start();

        while (connected) {
            Packet received = socket.pollPacket();
            switch (received.channel) {
                case MESSENGER -> notifyPlayer(received.body);

                case PLAYER_ACTIONS -> {
                    // todo send with OK header the new model
                    if (received.header != HeaderTypes.INVALID) {
                        notifyPlayer(received.body);
                    } else
                        notifyPlayerError(received.body);
                }

                case UPDATE_LITE_MODEL -> {
                    try {
                        ((Updater) new ObjectMapper().readerFor(Updater.class).readValue(received.body)).update(this.model);
                    } catch (JsonProcessingException e) {
                        System.out.println(Colors.color(Colors.RED, "update view error: ") + e.getMessage());
                    }
                }

                case RENDER_CANNON -> {
                    try {
                        ((Lighter)new ObjectMapper().readerFor(Lighter.class).readValue(received.body)).fire(this);
                    } catch (JsonProcessingException e) {
                        System.out.println(Colors.color(Colors.RED, "render cannon error: ") + e.getMessage());
                    }
                }

                case CONNECTION_STATUS -> {}
            }
        }
    }

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

    public void setState(CliState state) {
        this.state = state;
        notifyPlayer("");
        notifyPlayer(state.entryMessage);
    }

    @Override
    public void handleDisconnection() {
        this.connected = false;
        notifyPlayerError("disconnected");
    }

    @Override
    public void fireGameCreator() {
        setState(new CliSetPlayersNumberState(this));
    }

    @Override
    public void fireLobbyWait() {
        setState(new LobbyWaitingCLI(this));
    }

    @Override
    public void fireGameInit() {
        setState(new CliInitGameState(this));
    }

    @Override
    public void fireGameSession() {
        setState(new CliInGameState(this));
    }

    @Override
    public void fireGameEnded() {
        setState(new CliErrorState(this));
    }

    @Override
    public void fireGameResult() {
        setState(new CliErrorState(this));
    }

    public static void main(String[] args) {
        try {
            new CLI("localhost", 4444).start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

abstract class CliState {

    protected final CLI context;
    public final String entryMessage;


    protected CliState(CLI context, String entryMessage) {
        this.context = context;
        this.entryMessage = entryMessage;
    }

    public abstract void handleInput(String userInput);
}

class CliErrorState extends CliState {

    CliErrorState(CLI context) {
        super(context, "There was an error, quitting");
    }

    @Override
    public void handleInput(String userInput) {

    }
}

class ChooseNicknameCLI extends CliState {

    ChooseNicknameCLI(CLI context) {
        super(context, "Choose your nickname:");
    }

    @Override
    public void handleInput(String userInput) {
        context.model.setMyNickname(userInput);
        context.socket.send(new Packet(HeaderTypes.HELLO, ChannelTypes.PLAYER_ACTIONS, userInput));
    }
}

class CliSetPlayersNumberState extends CliState {

    CliSetPlayersNumberState(CLI context) {
        super(context, "How many players?");
    }

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

class LobbyWaitingCLI extends CliState {

    LobbyWaitingCLI(CLI context) {
        super(context, "waiting for other players...");
    }

    @Override
    public void handleInput(String userInput) {
        System.out.println("trimone ho detto che devi aspettare");
    }
}

class CliInGameState extends CliState {

    CliInGameState(CLI context) {
        super(context, "Choose an action. Type help for commands.");
    }

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
                    context.notifyPlayerError("You missed some parameter");
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
                CasualPrinter.printDiscounts(context.getModel().getDiscounts(context.getModel().getMe()));
                break;
            case "conversion":
            case "conv":
                CasualPrinter.printConversion(context.getModel().getConversion(context.getModel().getMe()));
                break;
            case "players":
                CasualPrinter.printPlayers(context.model);
                break;
            default:
                context.notifyPlayerError("Unknown command, try again!");
                break;
        }
    }
}

class CliInitGameState extends CliState {

    CliInitGameState(CLI context) {
        super(context, "You have to discard leader cards and choose resource");
    }

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

class CliEndGameState extends CliState {
    CliEndGameState(CLI context) {
        super(context, "Game ended, results are coming");
    }

    @Override
    public void handleInput(String userInput) {
        System.out.println("work in progress");
    }
}
