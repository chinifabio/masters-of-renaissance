package it.polimi.ingsw.client;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.*;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
import it.polimi.ingsw.model.match.markettray.RowCol;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.NormalProduction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.*;
import it.polimi.ingsw.view.View;
import java.util.*;

public class InGameCS extends ClientState {

    public InGameCS() {
        super();
        this.nextState.put(HeaderTypes.OK, this);
        this.nextState.put(HeaderTypes.INVALID, this);
        this.nextState.put(HeaderTypes.END_GAME, new EndGameCS());
    }

    @Override
    protected Packet doStuff(LiteModel model, View view) throws InterruptedException {
        view.renderHomePage();
        while (true) {
            List<String> data = view.pollData("Choose an action");
            int i = 1;

            switch (Actions.fromString(data.get(0))) {
                case HELP:
                    view.renderHelp();
                    break;

                case RETURN_HOME:
                    view.renderHomePage();
                    break;

                case END_TURN:
                    return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new EndTurnCommand().jsonfy());

                case BUY_DEV_CARD:
                    i = 1;
                    try {
                        LevelDevCard level = LevelDevCard.valueOf(data.get(i++).toUpperCase());
                        ColorDevCard color = ColorDevCard.valueOf(data.get(i++).toUpperCase());
                        DevCardSlot slot = DevCardSlot.valueOf(data.get(i++).toUpperCase());
                        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new BuyDevCardCommand(level, color, slot).jsonfy());
                    } catch (IllegalArgumentException out) {
                        view.notifyPlayerError(data.get(i) + " is not mappable");
                    } catch (IndexOutOfBoundsException arg) {
                        view.notifyPlayerError("You missed some parameter");
                    }
                    break;

                case CHOOSE_RESOURCE:
                    i = 0;
                    try {
                        DepotSlot dest = DepotSlot.valueOf(data.get(i++).toUpperCase());
                        ResourceType res = ResourceType.valueOf(data.get(i++).toUpperCase());
                        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new ChooseResourceCommand(dest, res).jsonfy());
                    } catch (IndexOutOfBoundsException e) {
                        view.notifyPlayerError("You missed some parameters");
                    } catch (IllegalArgumentException e) {
                        view.notifyPlayerError(data.get(i) + " is not mappable");
                    }
                    break;

                case USE_MARKET_TRAY:
                    try {
                        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new UseMarketTrayCommand(
                                RowCol.valueOf(data.get(i++).toUpperCase()),
                                Integer.parseInt(data.get(i))-1
                        ).jsonfy());
                    } catch (IndexOutOfBoundsException out) {
                        view.notifyPlayerError("you missed some parameter");
                    } catch (NumberFormatException number) {
                        view.notifyPlayerError(data.get(2) + " is not a number!");
                    } catch (IllegalArgumentException arg) {
                        view.notifyPlayerError("some parameter in the command is not correct");
                    }
                    break;

                case MOVE_IN_PRODUCTION:
                    try {
                        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new MoveInProductionCommand(
                                DepotSlot.valueOf(data.get(i++).toUpperCase()),
                                ProductionID.valueOf(data.get(i++).toUpperCase()),
                                ResourceBuilder.buildFromType(ResourceType.valueOf(data.get(i++).toUpperCase()), Integer.parseInt(data.get(i)))
                        ).jsonfy());
                    } catch (NumberFormatException number) {
                        view.notifyPlayerError(data.get(4) + " Is not a number");
                    } catch (IllegalArgumentException arg) {
                        view.notifyPlayerError(data.get(i) + " Doesn't exist. Type help for accepted values");
                    } catch (IndexOutOfBoundsException out) {
                        view.notifyPlayerError("You missed some parameter");
                    }
                    break;

                case MOVE_BETWEEN_DEPOT:
                    try {
                        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new MoveDepotCommand(
                                DepotSlot.valueOf(data.get(i++).toUpperCase()),
                                DepotSlot.valueOf(data.get(i++).toUpperCase()),
                                ResourceBuilder.buildFromType(ResourceType.valueOf(data.get(i++).toUpperCase()), Integer.parseInt(data.get(i)))
                        ).jsonfy());
                    } catch (IndexOutOfBoundsException out) {
                        view.notifyPlayerError("You missed some parameter ...");
                    } catch (NumberFormatException number) {
                        view.notifyPlayerError(data.get(4) + " Is not a number!");
                    } catch (IllegalArgumentException arg) {
                        view.notifyPlayerError(data.get(i) + " Doesn't exist. Type help for accepted values");
                    }
                    break;

                case ACTIVATE_PRODUCTION:
                    return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new ActivateProductionCommand().jsonfy());

                case DISCARD_LEADER_CARD:
                    try {
                        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new DiscardLeaderCommand("LC" + Integer.parseInt(data.get(1))).jsonfy());
                    } catch (IndexOutOfBoundsException out) {
                        view.notifyPlayerError("you need to insert the leader id");
                    } catch (NumberFormatException number) {
                        view.notifyPlayerError(data.get(1) + " Is not a number!");
                    }
                    break;

                case ACTIVATE_LEADER_CARD:
                    try {
                        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new ActivateLeaderCommand("LC" + Integer.parseInt(data.get(1))).jsonfy());
                    } catch (IndexOutOfBoundsException out) {
                        view.notifyPlayerError("You need to insert the leader id");
                    } catch (NumberFormatException number) {
                        view.notifyPlayerError(data.get(1) + " Is not a number!");
                    }
                    break;

                case SET_NORMAL_PRODUCTION:
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
                        view.notifyPlayerError(ill.getMessage());
                    } catch (IndexOutOfBoundsException out) {
                        view.notifyPlayerError("You missed some parameter ...");
                    } catch (NumberFormatException number) {
                        view.notifyPlayerError(data.get(i - 1) + " Is not a number!");
                    } catch (IllegalArgumentException arg) {
                        view.notifyPlayerError(data.get(i - 1) + " Doesn't exist. Type help for accepted values");
                    }
                    break;

                case PAINT_MARBLE_IN_TRAY:
                    try {
                        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new PaintMarbleCommand(
                                Integer.parseInt(data.get(i++)),
                                Integer.parseInt(data.get(i))
                        ).jsonfy());
                    } catch (IndexOutOfBoundsException out) {
                        view.notifyPlayerError("You missed some parameter ...");
                    } catch (NumberFormatException number) {
                        view.notifyPlayerError(data.get(i) + " Is not a number!");
                    }
                    break;

                case VIEW_LEADER_CARD:
                    view.renderLeaderCards();
                    break;

                case VIEW_PERSONAL_BOARD:
                    try {
                        view.renderPersonalBoard(data.get(1));
                    } catch (IndexOutOfBoundsException out) {
                        view.renderPersonalBoard(model.getMe());
                    } catch (NullPointerException n) {
                        view.notifyPlayerError(data.get(i) + " player doesn't exist");
                    }
                    break;

                case VIEW_PLAYERS: case VIEW_PRODUCTIONS:
                    view.notifyPlayerError("work in progress...");
                    break;

                case VIEW_DEV_SETUP:
                    view.renderDevSetup();
                    break;

                case VIEW_WAREHOUSE:
                    try {
                        view.renderWarehouse(data.get(1));
                    } catch (IndexOutOfBoundsException out) {
                        view.renderWarehouse(model.getMe());
                    }
                    break;

                case VIEW_MARKET_TRAY:
                    view.renderMarketTray();
                    break;

                case VIEW_FAITH_TRACK:
                    view.renderFaithTrack();
                    break;

                case NULL:
                    view.notifyPlayerError("Unknown command, try again!");
                    break;
            }
        }
    }
}
