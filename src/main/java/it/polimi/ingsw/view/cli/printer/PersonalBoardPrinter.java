package it.polimi.ingsw.view.cli.printer;

import it.polimi.ingsw.litemodel.LiteModel;
import it.polimi.ingsw.litemodel.litecards.LiteDevCard;
import it.polimi.ingsw.litemodel.litecards.LiteLeaderCard;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;
import it.polimi.ingsw.view.cli.printer.cardprinter.DevDecksPrinter;
import it.polimi.ingsw.view.cli.printer.cardprinter.ShowLeaderCards;

import java.util.HashMap;
import java.util.List;

/**
 * This class is the Printer of the PersonalBoard
 */
public class PersonalBoardPrinter {

    /**
     * This attribute is the height of the PersonalBoard
     */
    private static final int HEIGHT = 33; //rows.

    /**
     * This attribute is the width of the PersonalBoard
     */
    private static final int WIDTH = 133; //cols.

    /**
     * This method prints the PersonalBoard
     * @param model is the Model of the match
     * @param nickname is the Nickname of the Player
     */
    public static void printPersonalBoard(LiteModel model, String nickname){
        List<LiteLeaderCard> leaderCards = model.getLeader(nickname);
        HashMap<DevCardSlot, List<LiteDevCard>> devCards = model.getDevelop(nickname);


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
        DevDecksPrinter.createDevCardSlot(personalBoard, devCards, 15, 68);

        for (int i = 0; i< HEIGHT-1; i++){
            System.out.println();
            for (int j = 0; j<WIDTH-1; j++){
                System.out.print(personalBoard[i][j]);
            }
        }
        System.out.println();
    }

    /*
    public static void main(String[] args) throws IOException {
        //LeaderCards
        List<LiteLeaderCard> names = new ArrayList<>();
        List<LiteLeaderCard> leaderCards;
        ObjectMapper objectMapper = new ObjectMapper();
        Random rd = new Random();

        leaderCards = objectMapper.readValue(
                PersonalBoardPrinter.class.getResourceAsStream("/json/LeaderCards.json"),
                new TypeReference<List<LiteLeaderCard>>(){});

        names.add(leaderCards.get(rd.nextInt(16)));
        names.add(leaderCards.get(rd.nextInt(16)));

        //Devcards
        List<List<LiteDevCard>> cardFile;
        HashMap<DevCardSlot, List<LiteDevCard>> deck = new HashMap<DevCardSlot, List<LiteDevCard>>(){{
            put(DevCardSlot.LEFT, new ArrayList<>());
            put(DevCardSlot.CENTER, new ArrayList<>());
            put(DevCardSlot.RIGHT, new ArrayList<>());
        }};
        ObjectMapper mapper = new ObjectMapper();

        cardFile = mapper.readValue(
                PersonalBoardPrinter.class.getResourceAsStream("/json/DevCards.json"),
                new TypeReference<List<List<LiteDevCard>>>(){});


        deck.get(DevCardSlot.LEFT).add(cardFile.get(rd.nextInt(12)).get(rd.nextInt(4)));
        deck.get(DevCardSlot.LEFT).add(cardFile.get(rd.nextInt(12)).get(rd.nextInt(4)));
        deck.get(DevCardSlot.CENTER).add(cardFile.get(rd.nextInt(12)).get(rd.nextInt(4)));
        deck.get(DevCardSlot.CENTER).add(cardFile.get(rd.nextInt(12)).get(rd.nextInt(4)));
        deck.get(DevCardSlot.RIGHT).add(cardFile.get(rd.nextInt(12)).get(rd.nextInt(4)));

        //Warehouse
        LiteModel model = new LiteModel();
        model.createPlayer("gino");

        LiteResource coin = new LiteResource(ResourceType.COIN, 2);
        LiteResource shield = new LiteResource(ResourceType.SHIELD, 1);
        LiteResource stone = new LiteResource(ResourceType.STONE, 3);
        LiteResource servant = new LiteResource(ResourceType.SERVANT, 5);

        List<LiteResource> resourcesTop = new ArrayList<>();
        resourcesTop.add(shield);
        List<LiteResource> resourcesMiddle = new ArrayList<>();
        resourcesMiddle.add(coin);
        List<LiteResource> resourcesBottom = new ArrayList<>();
        resourcesBottom.add(stone);

        List<LiteResource> resourceSpecial = new ArrayList<>();
        resourceSpecial.add(ResourceBuilder.buildEmpty().liteVersion());

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
        LiteDepot special1 = new LiteDepot(resourcesMiddle);
        LiteDepot special2 = new LiteDepot(resourceSpecial);

        model.setDepot("gino", DepotSlot.TOP, depotTop);
        model.setDepot("gino", DepotSlot.MIDDLE, depotMiddle);
        model.setDepot("gino", DepotSlot.BOTTOM, depotBottom);
        model.setDepot("gino", DepotSlot.STRONGBOX, strongbox);
        model.setDepot("gino", DepotSlot.BUFFER, buffer);
        model.setDepot("gino", DepotSlot.SPECIAL1, special1);
        model.setDepot("gino", DepotSlot.SPECIAL2, special2);

        //PersonalBoardPrinter.printPersonalBoard(model, "gino", names, deck );
    }
     */
}
