package it.polimi.ingsw.client;
import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.communication.packet.ChannelTypes;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.Packet;
import it.polimi.ingsw.communication.packet.commands.*;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.litemodel.liteplayer.Actions;
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
import it.polimi.ingsw.view.cli.printer.*;
import it.polimi.ingsw.view.cli.printer.cardprinter.DevCardSlotPrinter;
import it.polimi.ingsw.view.cli.printer.cardprinter.DevDecksPrinter;
import it.polimi.ingsw.view.cli.printer.cardprinter.DevSetupPrinter;
import it.polimi.ingsw.view.cli.printer.cardprinter.ShowLeaderCards;

import java.io.IOException;
import java.util.Collections;
import java.util.Locale;
import java.util.Scanner;

public class InGameCS extends ClientState {

    private boolean introduction = false;

    private final Packet invalid = new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, "Invalid");
    private final Packet view = new Packet(HeaderTypes.INVALID, ChannelTypes.PLAYER_ACTIONS, "View");

    public InGameCS() {
        super();
        this.nextState.put(HeaderTypes.ACTION_DONE, this);
        this.nextState.put(HeaderTypes.OK, this);
        this.nextState.put(HeaderTypes.INVALID, this);
        this.nextState.put(HeaderTypes.END_TURN, this);
        this.nextState.put(HeaderTypes.END_GAME, new EndGameCS());
    }

    //TODO se scelgo di fare qualcosa ma poi cambio idea? controllo nei metodi?

    @Override
    protected Packet doStuff(LiteModel model) {
        if(!introduction){
            printHelp();
            System.out.println(TextColors.colorText(TextColors.YELLOW_BRIGHT,"\nAt the start of the game, you have to discard two leader cards and, based on your position in the sequence, you can select up to 2 initial resources.\nAfter that, end your turn. You can type \"help\" to see again the possible moves."));
            introduction=true;
        }
        System.out.print("\n" + TextColors.colorText(TextColors.YELLOW, "Choose an action:\n>"));
        String action = new Scanner(System.in).nextLine();      //gui/cli per scegliere azione   -  usiamo enumerazione
        switch (action.toLowerCase(Locale.ROOT)){
            //---------ACTIONS-----------
            case "discardleader": return discardLeader(model);
            case "chooseres": return chooseResource(model);
            case "activateleader": return activateLeader(model);
            case "buydevcard": return buyDevCard(model);
            case "paintmarble": return paintMarbleColor(model);
            case "usemarket": return useMarketTray(model);
            case "moveres":
                WarehousePrinter.printWarehouse(model, model.getMe());
                return moveDepot(model);
            case "moveinproduction":
                ProductionPrinter.printProductions(model.getAllProductions(model.getMe()));
                WarehousePrinter.printWarehouse(model, model.getMe());
                return moveInProduction(model);
            case "setnormalproduction":
                ProductionPrinter.printProductions(model.getAllProductions(model.getMe()));
                return setNormalProduction(model);
            case "activateproduction":
                ProductionPrinter.printProductions(model.getAllProductions(model.getMe()));
                return activateProduction(model);

            case "endturn": return endTurn(model);
            //---------VIEW METHODS---------
            case "viewdepots":
                WarehousePrinter.printResourcesLegend();
                WarehousePrinter.printWarehouse(model, model.getMe());
                return view;
            case "viewdevcards":
                DevDecksPrinter.printDevDecks(model.getDevelop(model.getMe()));
                return view;
            case "viewleaders":
                try {
                    ShowLeaderCards.printLeaderCardsPlayer(model.getLeader(model.getMe()));
                } catch (IOException e) {
                    System.out.println("something's wrong... I can feel it");
                }
                return view;
            case "viewmarket":
                System.out.println();
                MarketTrayPrinter.printMarketTray(model.getTray());
                return view;
            case "viewtrack":
                try {
                    new FaithTrackPrinter().printTrack(model);
                } catch (IOException e) {
                    System.out.println("Something's wrong... I can feel it: " + e.getMessage());
                }
                return view;
            case "viewcardsgrid":
                DevSetupPrinter.printDevSetup(model.getDevSetup());
                return view;
            case "viewplayers":
                //model.getPlayers().forEach((nick,pb)->PersonalBoardPrinter.printPersonalBoard(model, nick, model.getLeader(nick), model.getDevelop(nick)));
                return view;
            case "viewboard":
                PersonalBoardPrinter.printPersonalBoard(model, model.getMe(), model.getLeader(model.getMe()), model.getDevelop(model.getMe()));
                return view;
            case "help":
                printHelp();
                return view;
            default:
                System.out.println(TextColors.colorText(TextColors.RED_BRIGHT,"I don't understand!"));
                return invalid;
        }
    }

    //------------ACTIONS METHODS---------------

    public Packet discardLeader(LiteModel model) {
        if(!model.getPlayerState(model.getMe()).canDoAction(Actions.DISCARD_LEADER_CARD)) return invalid;

        //metodi cli/gui per scegliere la carta

        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new DiscardLeaderCommand(leader(model)).jsonfy());
    }

    public Packet chooseResource(LiteModel model){
        if(!model.getPlayerState(model.getMe()).canDoAction(Actions.CHOOSE_RESOURCE)) return invalid;

        Resource res;
        res = resConv(resPick());

        assert res != null;
        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new ChooseResourceCommand(DepotSlot.MIDDLE,res.type()).jsonfy());
    }


    public Packet activateLeader(LiteModel model) {
        if(!model.getPlayerState(model.getMe()).canDoAction(Actions.ACTIVATE_LEADER_CARD)) return invalid;

        //metodi cli/gui per scegliere la carta

        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new ActivateLeaderCommand(leader(model)).jsonfy());
    }

    public Packet buyDevCard(LiteModel model) {
        if(!model.getPlayerState(model.getMe()).canDoAction(Actions.BUY_DEV_CARD)) return invalid;

        System.out.println("The devsetup:");
        DevSetupPrinter.printDevSetup(model.getDevSetup());

        LevelDevCard level;
        ColorDevCard color;
        DevCardSlot slot;
        String lev,col,pos;
        Scanner scanner = new Scanner(System.in);
        System.out.println(TextColors.colorText(TextColors.YELLOW_BRIGHT,"Don't forget that you have to place the needed resource into the buffer!"));
        System.out.print("Insert the level\n>");
        while(true){
            lev = scanner.nextLine();
            if(lev.equals("1") || lev.equals("2") || lev.equals("3")) break;
            else System.out.print("You have to insert a valid level between 1, 2 and 3!\n>");
        }
        System.out.print("Insert a color, 0->Green, 1->Yellow, 2->Blue, 3->Purple:\n>");
        while(true){
            col = scanner.nextLine();
            if(col.equals("0") || col.equals("1") || col.equals("2") || col.equals("3")) break;
            else System.out.print("You have to insert a valid color! 0->Green, 1->Yellow, 2->Blue, 3->Purple:\n>");
        }
        System.out.print("Insert the position in the personal board, 0->Left, 1->Center, 2->Right:\n>");
        while(true){
            pos = scanner.nextLine();
            if(pos.equals("0") || pos.equals("1") || pos.equals("2")) break;
            else System.out.print("You have to insert a valid position! 0->Left, 1->Center, 2->Right:\n>");
        }
        level = levelConv(Integer.parseInt(lev));
        color = colorConv(Integer.parseInt(col));
        slot  = slotConv(Integer.parseInt(pos));

        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new BuyDevCardCommand(level,color,slot).jsonfy());
    }

    public Packet useMarketTray(LiteModel model) {
        if(!model.getPlayerState(model.getMe()).canDoAction(Actions.USE_MARKET_TRAY)) return invalid;

        System.out.println("This is the MarketTray:");
        MarketTrayPrinter.printMarketTray(model.getTray());
        System.out.print("Row or Column?\n>");
        RowCol rc;
        String temp;
        Scanner scanner = new Scanner(System.in);
        while(true){
            temp = scanner.nextLine();
            if(temp.toLowerCase(Locale.ROOT).equals("row")){
                rc= RowCol.ROW;
                break;
            }
            else if (temp.toLowerCase(Locale.ROOT).equals("column")) {
                rc = RowCol.COL;
                break;
            }
            else System.out.print("You have to insert row or column!\n>");
        }
        System.out.print("Insert the row or column number:\n>");
        String index;
        while(true){
            index = scanner.nextLine();
            if(rc.equals(RowCol.ROW)){
                if(index.equals("1") || index.equals("2") || index.equals("3")) break;
                else System.out.print("You have to insert a valid number!\n>");
            }
            else {
                if(index.equals("1") || index.equals("2") || index.equals("3") || index.equals("4")) break;
                else System.out.print("You have to insert a valid number!\n>");
            }
        }
        System.out.println(TextColors.colorText(TextColors.YELLOW_BRIGHT,"The obtained resource are stored in the buffer depot, don't forget to reposition them!"));

        //metodi cli/gui per scegliere la linea

        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new UseMarketTrayCommand(rc,Integer.parseInt(index)-1).jsonfy());
    }

    public Packet paintMarbleColor(LiteModel model) {
        if(!model.getPlayerState(model.getMe()).canDoAction(Actions.PAINT_MARBLE_IN_TRAY)) return invalid;
        if(model.getConversion(model.getMe()).size()==0){
            System.out.println(TextColors.colorText(TextColors.RED,"You don't have any conversion power!"));
            return view;
        }

        String conversion, index;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Choose the index of the white marble to convert:\n>");

        while(true){
            index = scanner.nextLine();
            if(index.equals("0") || index.equals("1") || index.equals("2") || index.equals("3") || index.equals("4") || index.equals("5") || index.equals("6") || index.equals("7") || index.equals("8") || index.equals("9") || index.equals("10") || index.equals("11")) break;
            else System.out.print("You have to choose a valid index!\n>");
        }

        if(model.getConversion(model.getMe()).size()==1) System.out.print("What the white marble should be? 1->" + model.getConversion(model.getMe()) + "\n>");
        else System.out.print("What the white marble should be? 1->" + model.getConversion(model.getMe()).get(0) + " 2->" + model.getConversion(model.getMe()).get(1) + "\n>");

        while(true){
            conversion = scanner.nextLine();
            if(conversion.equals("1") || conversion.equals("2")) break;
            else System.out.print("You have to choose a valid conversion resource!\n>");
        }   // conversion compreso fra 0 e 1 inclusi, solo 0 se non ho due carte leader, magari in automatico?

        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new PaintMarbleCommand(Integer.parseInt(conversion)-1,Integer.parseInt(index)-1).jsonfy());
    }

    public Packet moveDepot(LiteModel model) {
        if(!model.getPlayerState(model.getMe()).canDoAction(Actions.MOVE_BETWEEN_DEPOT)) return invalid;

        DepotSlot from,dest;
        Resource res;

        from = depotConv(depotPick("starting"));
        dest = depotConv(depotPick("destination"));
        res = resConv(resPick());

        return new Packet(HeaderTypes.DO_ACTION,ChannelTypes.PLAYER_ACTIONS, new MoveDepotCommand(from,dest, res).jsonfy());
    }

    public Packet activateProduction(LiteModel model) {
        if(!model.getPlayerState(model.getMe()).canDoAction(Actions.ACTIVATE_PRODUCTION)) return invalid;

        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new ActivateProductionCommand().jsonfy());
    }

    public Packet moveInProduction(LiteModel model) {
        if(!model.getPlayerState(model.getMe()).canDoAction(Actions.MOVE_IN_PRODUCTION)) return invalid;

        DepotSlot from;
        ProductionID dest;
        Resource res;

        //metodi per scegliere la risorsa da spostare e depot/produzione

        from = depotConv(depotPick("starting"));
        dest = prodConv(prodPick());
        res = resConv(resPick());

        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new MoveInProductionCommand(from,dest,res).jsonfy());
    }

    public Packet setNormalProduction(LiteModel model) { //todo da fare
        if(!model.getPlayerState(model.getMe()).canDoAction(Actions.SET_NORMAL_PRODUCTION)) return invalid;

        ProductionID prod=ProductionID.BASIC;
        NormalProduction normal = null;
        try {
            normal = new NormalProduction(Collections.singletonList(ResourceBuilder.buildCoin()), Collections.singletonList(ResourceBuilder.buildCoin()));
        } catch (IllegalTypeInProduction illegalTypeInProduction) {
            // risorsa unknown o empty
        }

        return new Packet(HeaderTypes.DO_ACTION, ChannelTypes.PLAYER_ACTIONS, new SetNormalProductionCommand(prod,normal).jsonfy());
    }

    public Packet endTurn(LiteModel model) {
        if(!model.getPlayerState(model.getMe()).canDoAction(Actions.END_TURN)) return invalid;

        return new Packet(HeaderTypes.DO_ACTION,ChannelTypes.PLAYER_ACTIONS, new EndTurnCommand().jsonfy());
    }

    public void printHelp(){
        System.out.println(TextColors.colorText(TextColors.YELLOW,"\nPossible actions:"));
        System.out.println("discardleader, chooseres, activateleader, buydevcard, paintmarble, usemarket, moveres, moveinproduction, setnormalproduction, activateproduction, endturn.");
        System.out.println(TextColors.colorText(TextColors.YELLOW,"Possible views:"));
        System.out.println("viewtrack, viewcardsgrid, viewplayer, viewmarket, viewdepots, viewleader, viewboard");
    }

    //-----------------------HELPER METHODS--------------------------

    private String leader(LiteModel model){
        System.out.println("Those are your leader cards:");
        try {
            ShowLeaderCards.printLeaderCardsPlayer(model.getLeader(model.getMe()));
        } catch (IOException e) {
            System.out.println("something's wrong... I can feel it");
        }
        System.out.print("Insert the leader card to discard:\n>");
        return new Scanner(System.in).nextLine().toUpperCase(Locale.ROOT);
    }

    private int depotPick(String where){
        String temp;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Choose the " + where + " depot (1->TOP, 2->MID, 3->BOTTOM, 4->LEADER1, 5->LEADER2, 6->BUFFER):\n>");
        while(true){
            temp = scanner.nextLine();
            if(temp.equals("1") || temp.equals("2") || temp.equals("3") || temp.equals("4") || temp.equals("5") || temp.equals("6")) return Integer.parseInt(temp);
            else System.out.print("You have to choose a valid depot! (1->TOP, 2->MID, 3->BOTTOM, 4->LEADER1, 5->LEADER2, 6->BUFFER):\n>");
        }
    }

    private int prodPick(){
        String temp;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Choose the destination production (0->BASIC,1->LEFT,2->CENTER,3->RIGHT,4->LEADER1,5->LEADER2):\n>");
        while(true){
            temp = scanner.nextLine();
            if(temp.equals("0") || temp.equals("1") || temp.equals("2") || temp.equals("3") || temp.equals("4") || temp.equals("5") ) return Integer.parseInt(temp);
            else System.out.print("You have to choose a valid production!\n>");
        }
    }

    private int resPick(){
        String temp;
        System.out.print("What resource do you want? (0->Coin, 1->Stone, 2->Shield, 3->Servant)\n>");
        Scanner scanner = new Scanner(System.in);
        while(true) {
            temp = scanner.nextLine();
            if(temp.equals("0") || temp.equals("1") || temp.equals("2") || temp.equals("3")) return Integer.parseInt(temp);
            else System.out.print("You have to choose a valid resource!\n>");
        }
    }

    private Resource resConv(int x){
        switch(x){
            case 0: return ResourceBuilder.buildCoin();
            case 1: return ResourceBuilder.buildStone();
            case 2: return ResourceBuilder.buildShield();
            case 3: return ResourceBuilder.buildServant();
            default: return null;
        }
    }

    private LevelDevCard levelConv(int x){
        switch(x){
            case 1: return LevelDevCard.LEVEL1;
            case 2: return LevelDevCard.LEVEL2;
            case 3: return LevelDevCard.LEVEL3;
            default: return null;
        }
    }

    private ColorDevCard colorConv(int x){
        switch(x){
            case 0: return ColorDevCard.GREEN;
            case 1: return ColorDevCard.YELLOW;
            case 2: return ColorDevCard.BLUE;
            case 3: return ColorDevCard.PURPLE;
            default: return null;
        }
    }

    private DevCardSlot slotConv(int x){
        switch(x){
            case 0: return DevCardSlot.LEFT;
            case 1: return DevCardSlot.CENTER;
            case 2: return DevCardSlot.RIGHT;
            default: return null;
        }
    }

    private DepotSlot depotConv(int x){
        switch(x){
            case 1: return DepotSlot.TOP;
            case 2: return DepotSlot.MIDDLE;
            case 3: return DepotSlot.BOTTOM;
            case 4: return DepotSlot.SPECIAL1;
            case 5: return DepotSlot.SPECIAL2;
            case 6: return DepotSlot.BUFFER;
            default: return null;
        }
    }

    private ProductionID prodConv(int x){
        switch(x){
            case 0: return ProductionID.BASIC;
            case 1: return ProductionID.LEFT;
            case 2: return ProductionID.CENTER;
            case 3: return ProductionID.RIGHT;
            case 4: return ProductionID.LEADER1;
            case 5: return ProductionID.LEADER2;
            default: return null;
        }
    }
}
