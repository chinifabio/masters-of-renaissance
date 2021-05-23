package it.polimi.ingsw.view.cli.printer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.litemodel.LiteResource;
import it.polimi.ingsw.litemodel.litecards.LiteDevCard;
import it.polimi.ingsw.litemodel.litecards.LiteLeaderCard;
import it.polimi.ingsw.litemodel.litewarehouse.LiteDepot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.view.cli.printer.cardprinter.DevCardSlotPrinter;
import it.polimi.ingsw.view.cli.printer.cardprinter.ShowLeaderCards;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PersonalBoardPrinter {


    private static final int HEIGHT = 27; //rows.
    private static final int WIDTH = 130; //cols.

    public static void printPersonalBoard(LiteModel model, String nickname, List<LiteLeaderCard> leaderCards, List<LiteDevCard> devCards){
        String[][] personalBoard = new String[HEIGHT][WIDTH];

        for (int i = 0; i< HEIGHT; i++){
            for (int j = 0; j<WIDTH; j++){
                personalBoard[i][j] = " ";
            }
        }
        personalBoard[0][0] ="╔";
        for (int i = 1; i < HEIGHT-2; i++){
            personalBoard[i][0] = "║";
            personalBoard[i][WIDTH-2] = "║";
        }
        personalBoard[HEIGHT-2][0] ="╚";
        personalBoard[HEIGHT-2][WIDTH-2] = "╝";
        personalBoard[0][WIDTH-2] = "╗";
        for (int i = 1; i < WIDTH-2; i++){
            personalBoard[0][i] = "═";
            personalBoard[HEIGHT-2][i] = "═";
        }

        personalBoard[0][55] ="╦";
        for (int i = 1; i < HEIGHT-1; i++){
            personalBoard[i][55] = "║";
        }
        personalBoard[HEIGHT-2][55] ="╩";

        personalBoard[13][55] ="╠";
        for (int i = 56; i < WIDTH-2; i++){
            personalBoard[13][i] = "═";
        }
        personalBoard[13][ WIDTH-2] ="╣";

        WarehousePrinter.createWarehouse(personalBoard, model, nickname,8,12);
        WarehousePrinter.createBuffer(personalBoard, model, nickname,15,12);
        ShowLeaderCards.createLeaderCardsSlot(personalBoard, leaderCards, 3, 68);
        DevCardSlotPrinter.createDevCardSlot(personalBoard, devCards, 15, 68 );

        for (int i = 0; i< HEIGHT-1; i++){
            System.out.println();
            for (int j = 0; j<WIDTH-1; j++){
                System.out.print(personalBoard[i][j]);
            }
        }
        System.out.println();
    }

    public static void main(String[] args) throws IOException {
        //LeaderCards
        List<LiteLeaderCard> names = new ArrayList<>();
        List<LiteLeaderCard> leaderCards;
        ObjectMapper objectMapper = new ObjectMapper();
        Random rd = new Random();

        leaderCards = objectMapper.readValue(
                new File("src/resources/LeaderCards.json"),
                new TypeReference<List<LiteLeaderCard>>(){});

        names.add(leaderCards.get(rd.nextInt(16)));
        names.add(leaderCards.get(rd.nextInt(16)));

        //Devcards
        List<LiteDevCard> dev = new ArrayList<>();
        List<List<LiteDevCard>> cardFile;
        ObjectMapper mapper = new ObjectMapper();

        cardFile = mapper.readValue(
                new File("src/resources/DevCards.json"),
                new TypeReference<List<List<LiteDevCard>>>(){});

        dev.add(cardFile.get(rd.nextInt(12)).get(rd.nextInt(4)));
        dev.add(cardFile.get(rd.nextInt(12)).get(rd.nextInt(4)));
        dev.add(cardFile.get(rd.nextInt(12)).get(rd.nextInt(4)));

        //Warehouse
        LiteModel model = new LiteModel();
        model.createPlayer("gino");

        LiteResource coin = new LiteResource(ResourceType.COIN, 1);
        LiteResource shield = new LiteResource(ResourceType.SHIELD, 1);
        LiteResource stone = new LiteResource(ResourceType.EMPTY, 0);
        LiteResource servant = new LiteResource(ResourceType.EMPTY, 0);

        List<LiteResource> resourcesTop = new ArrayList<>();
        resourcesTop.add(shield);
        List<LiteResource> resourcesMiddle = new ArrayList<>();
        resourcesMiddle.add(coin);
        List<LiteResource> resourcesBottom = new ArrayList<>();
        resourcesBottom.add(stone);

        List<LiteResource> resourcesStrongbox = new ArrayList<>();
        resourcesStrongbox.add(new LiteResource(ResourceType.COIN, 20));
        resourcesStrongbox.add(servant);
        resourcesStrongbox.add(new LiteResource(ResourceType.STONE, 3));
        resourcesStrongbox.add(new LiteResource(ResourceType.SHIELD, 15));

        LiteDepot depotTop = new LiteDepot(resourcesTop);
        LiteDepot depotBottom = new LiteDepot(resourcesBottom);
        LiteDepot depotMiddle = new LiteDepot(resourcesMiddle);
        LiteDepot strongbox = new LiteDepot(resourcesStrongbox);
        LiteDepot buffer = new LiteDepot(resourcesStrongbox);

        model.setDepot("gino", DepotSlot.TOP, depotTop);
        model.setDepot("gino", DepotSlot.MIDDLE, depotMiddle);
        model.setDepot("gino", DepotSlot.BOTTOM, depotBottom);
        model.setDepot("gino", DepotSlot.STRONGBOX, strongbox);
        model.setDepot("gino", DepotSlot.BUFFER, buffer);

        PersonalBoardPrinter.printPersonalBoard(model, "gino", names, dev );
    }
}
