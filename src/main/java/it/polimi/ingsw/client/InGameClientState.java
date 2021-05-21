package it.polimi.ingsw.client;

import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.*;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.litemodel.liteplayer.Actions;
import it.polimi.ingsw.model.match.markettray.RowCol;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.view.cli.printer.FaithTrackPrinter;
import it.polimi.ingsw.view.cli.printer.MarketTrayPrinter;
import it.polimi.ingsw.view.cli.printer.WarehousePrinter;
import it.polimi.ingsw.view.cli.printer.cardprinter.DevSetupPrinter;
import it.polimi.ingsw.view.cli.printer.cardprinter.ShowLeaderCards;

import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

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

        while (command == null) {
            System.out.println("give me action: ");
            System.out.print("> ");
            switch (new Scanner(System.in).nextLine()) {

                case "dis":
                    System.out.println("choose the id: ");
                    try {
                        ShowLeaderCards.printLeaderCardsPlayer(model.getLeader(model.getMe()));
                    } catch (Exception e) {
                        System.out.println("something wrong...");
                    }
                    System.out.print("> ");
                    command = new DiscardLeaderCommand(new Scanner(System.in).nextLine());
                    break;

                case "chooseRes":
                    System.out.println("insert the resource: ");
                    System.out.print("> ");
                    ResourceType res = ResourceType.valueOf(new Scanner(System.in).nextLine().toUpperCase(Locale.ROOT));
                    System.out.println("insert the depot to insert it: ");
                    System.out.print("> ");
                    DepotSlot dest = DepotSlot.valueOf(new Scanner(System.in).nextLine().toUpperCase());
                    command = new ChooseResourceCommand(dest, res);
                    break;

                case "done":
                    command = new EndTurnCommand();
                    break;

                case "viewTray":
                    MarketTrayPrinter.printMarketTray(model.getTray());
                    break;

                case "useMarket":
                    MarketTrayPrinter.printMarketTray(model.getTray());
                    System.out.println("Row or col?");
                    System.out.print("> ");
                    RowCol rowCol = RowCol.valueOf(new Scanner(System.in).nextLine().toUpperCase(Locale.ROOT));
                    int index = -1;
                    boolean repeat = false;
                    do{
                        System.out.println("Select the arrow");
                        System.out.print("> ");
                        try {
                            index = Integer.parseInt(new Scanner(System.in).nextLine());
                        } catch (NumberFormatException e){
                            repeat = true;
                        }
                    }while (repeat);
                    command = new UseMarketTrayCommand(rowCol,index);
                    break;

                case "track":
                    try {
                        new FaithTrackPrinter(model).printTrack();
                    } catch (IOException e) {
                        System.out.println("something wrong...");
                    }
                    break;

                case "activateLeader":
                    if (model.getPlayerState(model.getMe()).canDoAction(Actions.ACTIVATE_LEADER_CARD))
                        System.out.println("illegal");
                    break;

                case "devSetup":
                    DevSetupPrinter.printDevSetup(model.getDevSetup());
                    break;

                case "showWarehouse":
                    //TODO inizializzare il LiteWarehouse
                    WarehousePrinter.printWarehouse(model, model.getMe());
                    break;

                default:
                    System.out.println("I don't understand... pls don't bullish me. It is your fault! <3");
                    break;
            }
        }
        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, command.jsonfy());
    }
}