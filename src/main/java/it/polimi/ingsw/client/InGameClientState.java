package it.polimi.ingsw.client;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.ChooseResourceCommand;
import it.polimi.ingsw.communication.packet.commands.Command;
import it.polimi.ingsw.communication.packet.commands.DiscardLeaderCommand;
import it.polimi.ingsw.communication.packet.commands.EndTurnCommand;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.litemodel.litecards.LiteDevCard;
import it.polimi.ingsw.litemodel.litecards.LiteDevSetup;
import it.polimi.ingsw.litemodel.litecards.LiteLeaderCard;
import it.polimi.ingsw.litemodel.litemarkettray.LiteMarble;
import it.polimi.ingsw.litemodel.liteplayer.Actions;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.view.cli.printer.FaithTrackPrinter;
import it.polimi.ingsw.view.cli.printer.cardprinter.ShowLeaderCards;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;

public class InGameClientState extends ClientState {

    public InGameClientState() {
        super();
        this.nextState.put(HeaderTypes.OK, this);
        this.nextState.put(HeaderTypes.INVALID, this);
        this.nextState.put(HeaderTypes.END_TURN, this);
        this.nextState.put(HeaderTypes.END_GAME, new ErrorClientState());
    }

    /**
     * Do some stuff that generate a packet to send to the server
     *
     * @return packet to send to the server
     */
    @Override
    protected Packet doStuff(LiteModel model) {
        Command command = null;
        boolean commandSet = false;

        while (!commandSet) {
            System.out.println("give me action: ");
            System.out.print("> ");
            switch (new Scanner(System.in).nextLine()) {
                case "discardLeader":
                    System.out.println("choose the id: ");
                    try {
                        ShowLeaderCards.printLeaderCardsPlayer(model.getLeader(model.getMe()));
                    } catch (IOException e) {
                        System.out.println("something wrong...");
                    }
                    System.out.print("> ");
                    command = new DiscardLeaderCommand(new Scanner(System.in).nextLine());
                    commandSet = true;
                    break;

                case "chooseRes":
                    System.out.println("insert the resource: ");
                    System.out.print("> ");
                    ResourceType res = ResourceType.valueOf(new Scanner(System.in).nextLine().toUpperCase(Locale.ROOT));
                    System.out.println("insert the depot to insert it: ");
                    System.out.print("> ");
                    DepotSlot dest = DepotSlot.valueOf(new Scanner(System.in).nextLine().toUpperCase());
                    command = new ChooseResourceCommand(dest, res);
                    commandSet = true;
                    break;

                case "endTurn":
                    command = new EndTurnCommand();
                    commandSet = true;
                    break;

                case "viewTray":
                    LiteMarble[][] tray = model.getTray();
                    for (LiteMarble[] row : tray) System.out.println(Arrays.asList(row));
                    break;

                case "track":
                    try {
                        new FaithTrackPrinter(model).printTrack();
                    } catch (IOException e) {
                        System.out.println("something wrong...");
                    }

                case "activateLeader":
                    if (model.getState().canDoAction(Actions.ACTIVATE_LEADER_CARD))
                        System.out.println("illegal");
                    break;

                case "devSetup":
                    for (LiteDevCard[] row : model.getDevSetup().getDevSetup())
                        System.out.println(Arrays.asList(row));
                    break;

                default:
                    System.out.println("I don't understand... pls don't bullish me. It is your fault! <3");
                    break;
            }
        }

        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, command.jsonfy());
    }
}
