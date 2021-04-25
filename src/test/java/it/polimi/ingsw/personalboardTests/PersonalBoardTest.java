package it.polimi.ingsw.personalboardTests;


import static it.polimi.ingsw.model.resource.ResourceBuilder.buildStone;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.cards.effects.AddDepotEffect;
import it.polimi.ingsw.model.cards.effects.AddExtraProductionEffect;
import it.polimi.ingsw.model.cards.effects.AddProductionEffect;
import it.polimi.ingsw.model.cards.effects.Effect;
import it.polimi.ingsw.model.exceptions.PlayerStateException;
import it.polimi.ingsw.model.exceptions.card.AlreadyInDeckException;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;
import it.polimi.ingsw.model.exceptions.card.MissingCardException;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.exceptions.requisite.LootTypeException;
import it.polimi.ingsw.model.exceptions.warehouse.NegativeResourcesDepotException;
import it.polimi.ingsw.model.exceptions.warehouse.UnobtainableResourceException;
import it.polimi.ingsw.model.exceptions.warehouse.WrongDepotException;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
import it.polimi.ingsw.model.match.match.Match;
import it.polimi.ingsw.model.match.match.MultiplayerMatch;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;
import it.polimi.ingsw.model.player.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.player.personalBoard.warehouse.Warehouse;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.NormalProduction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.Production;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.model.requisite.CardRequisite;
import it.polimi.ingsw.model.requisite.ColorCardRequisite;
import it.polimi.ingsw.model.requisite.Requisite;
import it.polimi.ingsw.model.requisite.ResourceRequisite;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * test collectors for PersonalBoard
 */
public class PersonalBoardTest {

    Match game;

    Player player1;
    Player player2;

    @BeforeEach
    public void initialization() {
        game = new MultiplayerMatch();

        assertDoesNotThrow(()->player1 = new Player("uno", game));
        assertDoesNotThrow(()->player2 = new Player("due", game));

        assertTrue(game.playerJoin(player1));
        assertTrue(game.playerJoin(player2));

        assertDoesNotThrow(()-> game.startGame());

        assertDoesNotThrow(()-> game.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> game.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> game.test_getCurrPlayer().endThisTurn());

        assertDoesNotThrow(()-> game.test_getCurrPlayer().chooseResource(ResourceType.COIN));
        assertDoesNotThrow(()-> game.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> game.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(() -> assertTrue(game.test_getCurrPlayer().test_getPB().test_getDepots().get(DepotSlot.STRONGBOX).viewAllResources().contains(ResourceBuilder.buildCoin())));
        assertDoesNotThrow(()-> game.test_getCurrPlayer().endThisTurn());
    }

    /**
     * This test creates a PersonalBoard and add
     */
    @Test
    void DevCards() throws IllegalTypeInProduction {
        List<Requisite> req = new ArrayList<>();
        Resource coin = ResourceBuilder.buildCoin(2);
        ResourceRequisite rr = new ResourceRequisite(coin);
        req.add(rr);
        Production p = new NormalProduction(Collections.singletonList(buildStone()), Collections.singletonList(buildStone()));

        DevCard c1 = new DevCard("000", new AddProductionEffect(p), 2, LevelDevCard.LEVEL1, ColorDevCard.GREEN, req);
        DevCard c2 = new DevCard("111", new AddProductionEffect(p), 4, LevelDevCard.LEVEL2, ColorDevCard.YELLOW, req);
        DevCard c3 = new DevCard("222", new AddProductionEffect(p), 6, LevelDevCard.LEVEL3, ColorDevCard.BLUE, req);
        DevCard c31 = new DevCard("333", new AddProductionEffect(p), 0, LevelDevCard.LEVEL3, ColorDevCard.PURPLE, req);

        DevCardSlot Left = DevCardSlot.LEFT;
        DevCardSlot Center = DevCardSlot.CENTER;

        Match match = new MultiplayerMatch();

        Player player =  new Player("gino",match);

        PersonalBoard finalPersonalBoard = new PersonalBoard(player);

        assertDoesNotThrow(()->{
            if (finalPersonalBoard.addDevCard(Left, c1, null)) {
                assertEquals(c1, finalPersonalBoard.viewDevCards().get(Left));
            }

            if (finalPersonalBoard.addDevCard(Left, c2, null)) {
                assertEquals(c2, finalPersonalBoard.viewDevCards().get(Left));
            } else {
                fail();
            }

            if (finalPersonalBoard.addDevCard(Center, c2, null)) {
                fail();
            } else {
                try {
                    assertNull(finalPersonalBoard.viewDevCards().get(Center));
                } catch (NullPointerException ignore) {}
            }

            if (finalPersonalBoard.addDevCard(Left, c3, null)){
                assertEquals(c3, finalPersonalBoard.viewDevCards().get(Left));
            } else {
                fail();
            }

            if (finalPersonalBoard.addDevCard(Center, c3, null)){
                fail();
            } else {
                try {
                    assertNull(finalPersonalBoard.viewDevCards().get(Center));
                } catch (NullPointerException ignored) {}
            }

            if (finalPersonalBoard.addDevCard(Center, c1, null)){
                assertEquals(c1, finalPersonalBoard.viewDevCards().get(Center));
            } else {
                fail();
            }

            if (finalPersonalBoard.addDevCard(Center, c3, null)){
                fail();
            } else {
                assertEquals(c1, finalPersonalBoard.viewDevCards().get(Center));
            }

            if (finalPersonalBoard.addDevCard(Left, c31, null)){
                fail();
            } else {
                assertEquals(c3, finalPersonalBoard.viewDevCards().get(Left));
            }
        });

    }

    /**
     * This test creates two LeaderCards, adds them to the deck and activate them.
     */
    @Test
    void ActivateLeaderCard() throws MissingCardException, AlreadyInDeckException, IllegalTypeInProduction, WrongDepotException {
        String ID1="000", ID2="111";
        List<Resource> sample = new ArrayList<>();
        Warehouse warehouse = new Warehouse();

        Production p = new NormalProduction( sample, sample);

        List<Requisite> req = new ArrayList<>();
        Resource coin = ResourceBuilder.buildCoin(2);
        ResourceRequisite rr = new ResourceRequisite(coin);
        req.add(rr);

        warehouse.insertInDepot(DepotSlot.BOTTOM,ResourceBuilder.buildCoin(2));


        LeaderCard c1 = new LeaderCard(ID1, new AddExtraProductionEffect(p), 1, req);
        LeaderCard c2 = new LeaderCard(ID2, new AddExtraProductionEffect(p), 2, req);

        Match match = new MultiplayerMatch();

        Player player = new Player("gino",match);

        PersonalBoard personalBoard =  new PersonalBoard(player);

        personalBoard.addLeaderCard(c1);
        try {
            assertEquals(personalBoard.viewLeaderCard().peekFirstCard(), c1);
        } catch (EmptyDeckException e) {
            fail();
        }

        //personalBoard.addLeaderCard(c1);

        assertEquals(1,personalBoard.viewLeaderCard().getNumberOfCards());

        personalBoard.addLeaderCard(c2);

        assertEquals(2,personalBoard.viewLeaderCard().getNumberOfCards());

        assertFalse(personalBoard.viewLeaderCard().peekCard(ID1).isActivated());

        try {
            personalBoard.activateLeaderCard(ID1);
        } catch (EmptyDeckException | LootTypeException e) {
            fail();
        }

        assertTrue(personalBoard.viewLeaderCard().peekCard(ID1).isActivated());

    }

    /**
     * This test creates two LeaderCards and discard them one by one
     */
    @Test
    void DiscardLeaderCard() throws EmptyDeckException, MissingCardException, AlreadyInDeckException, IllegalTypeInProduction {
        String ID1="000", ID2="111";

        Production p = new NormalProduction(Collections.singletonList(buildStone()), Collections.singletonList(buildStone()));


        List<Requisite> req = new ArrayList<>();
        Resource coin = ResourceBuilder.buildCoin(2);
        ResourceRequisite rr = new ResourceRequisite(coin);
        req.add(rr);

        LeaderCard c1 = new LeaderCard(ID1, new AddProductionEffect(p), 1, req);
        LeaderCard c2 = new LeaderCard(ID2, new AddProductionEffect(p), 2, req);

        Match match = new MultiplayerMatch();

        Player player =  new Player("gino",match);


        PersonalBoard personalBoard = new PersonalBoard(player);

        assertNotNull(personalBoard);
        for (LeaderCard leaderCard : Arrays.asList(c1, c2)) {
            personalBoard.addLeaderCard(leaderCard);
        }

        assertEquals(2,personalBoard.viewLeaderCard().getNumberOfCards());

        personalBoard.discardLeaderCard(ID1);

        assertEquals(1,personalBoard.viewLeaderCard().getNumberOfCards());

        try {
            personalBoard.viewLeaderCard().peekCard(ID1);
            fail();
        } catch (MissingCardException ignored) { }

        try {
            personalBoard.discardLeaderCard(ID1);
            fail();
        } catch (MissingCardException ignored) { }
        try {
            personalBoard.discardLeaderCard(ID2);
        } catch (MissingCardException ignored) { }

        try {
            personalBoard.viewLeaderCard().peekCard(ID2);
            fail();
        } catch (MissingCardException ignored) { }

        assertEquals(0,personalBoard.viewLeaderCard().getNumberOfCards());

    }

    /**
     * This test insert some resources into depots and checks them.
     */
    @Test
    void Resources() throws IllegalTypeInProduction, NegativeResourcesDepotException, UnobtainableResourceException, WrongDepotException {
        Match match = new MultiplayerMatch();

        Player player = new Player("gino",match);

        PersonalBoard personalBoard = new PersonalBoard(player);

        Resource twoStone = ResourceBuilder.buildStone(2);
        Resource oneStone = ResourceBuilder.buildStone(1);

        personalBoard.insertInDepot(DepotSlot.BUFFER, twoStone);
        personalBoard.moveResourceDepot(DepotSlot.BUFFER,DepotSlot.MIDDLE,twoStone);
        assertEquals(twoStone,personalBoard.viewDepotResource(DepotSlot.MIDDLE));
        assertNotEquals(oneStone,personalBoard.viewDepotResource(DepotSlot.MIDDLE));
        assertNotEquals(twoStone,personalBoard.viewDepotResource(DepotSlot.TOP));
    }


    /**
     * This test checks every method that involve production.
     */
    @Test
    void Production() throws IllegalTypeInProduction {
        Match match = new MultiplayerMatch();
        Player player = new Player("gino",match);
        PersonalBoard personalBoard = new PersonalBoard(player);

        List<Resource> unknownReq = new ArrayList<>();
        List<Resource> unknownOutput = new ArrayList<>();
        unknownReq.add(ResourceBuilder.buildUnknown());
        unknownReq.add(ResourceBuilder.buildUnknown());
        unknownOutput.add(ResourceBuilder.buildUnknown());

        for(int i=0;i<5;i++){
            assertNull(personalBoard.possibleProduction().get(i));
        }

        List<Requisite> req = new ArrayList<>();
        Resource twoCoin = ResourceBuilder.buildCoin(2);
        Resource oneServant = ResourceBuilder.buildServant();
        List<Resource> resourceList = new ArrayList<>();
        resourceList.add(twoCoin);
        resourceList.add(oneServant);
        ResourceRequisite rr = new ResourceRequisite(twoCoin);
        req.add(rr);
        Production prod1 = new NormalProduction(resourceList, Collections.singletonList(buildStone()));

        DevCard c1 = new DevCard("000", new AddProductionEffect(prod1), 2, LevelDevCard.LEVEL1, ColorDevCard.GREEN, req);

        personalBoard.addDevCard(DevCardSlot.RIGHT,c1,match);
        personalBoard.addProduction(prod1,DevCardSlot.RIGHT);
        assertEquals(prod1,personalBoard.possibleProduction().get(ProductionID.RIGHT));


        Production prod2 = new NormalProduction(Collections.singletonList(ResourceBuilder.buildShield(2)),resourceList);
        Production prod3 = new NormalProduction(Arrays.asList(ResourceBuilder.buildStone(),ResourceBuilder.buildCoin()),Collections.singletonList(ResourceBuilder.buildFaithPoint(3)));

        DevCard c2 = new DevCard("111", new AddProductionEffect(prod2), 6, LevelDevCard.LEVEL1, ColorDevCard.BLUE, req);
        DevCard c3 = new DevCard("222", new AddProductionEffect(prod3), 4, LevelDevCard.LEVEL2, ColorDevCard.GREEN, req);

        personalBoard.addDevCard(DevCardSlot.CENTER,c2,match);
        personalBoard.addDevCard(DevCardSlot.RIGHT,c3,match);
        personalBoard.addProduction(prod2,DevCardSlot.CENTER);
        assertEquals(prod2, personalBoard.possibleProduction().get(ProductionID.CENTER));

        personalBoard.addProduction(prod3,DevCardSlot.RIGHT);
        assertEquals(prod3, personalBoard.possibleProduction().get(ProductionID.RIGHT));


    }

    /**
     * This test create a personalBoard and moves the player on its faith track.
     */
    @Test
    void FaithTrackMoves() throws IllegalTypeInProduction {
        Resource first = ResourceBuilder.buildFaithPoint(1);
        Resource ten = ResourceBuilder.buildFaithPoint(10);

        Match match = new MultiplayerMatch();

        Player player1 = new Player("gino",match);

        PersonalBoard personalBoard = new PersonalBoard(player1);

        Player player2 = new Player("gino",match);


        Match pm = new MultiplayerMatch();
        pm.playerJoin(player2);
        pm.playerJoin(player1);

        assertEquals(0,personalBoard.FaithMarkerPosition());
        assertTrue(personalBoard.moveFaithMarker(first.amount(), pm));

        assertEquals(1,personalBoard.FaithMarkerPosition());
        assertTrue(personalBoard.moveFaithMarker(first.amount(), pm));

        assertEquals(2,personalBoard.FaithMarkerPosition());
        assertTrue(personalBoard.moveFaithMarker(ten.amount(), pm));

        assertEquals(12,personalBoard.FaithMarkerPosition());
        assertTrue(personalBoard.moveFaithMarker(ten.amount(), pm));

        assertEquals(22,personalBoard.FaithMarkerPosition());

        personalBoard.moveFaithMarker(ten.amount(), pm);

    }

    @Test
    public void countsDevCardsPoints() throws PlayerStateException, WrongDepotException {

        PersonalBoard board = player1.test_getPB();
        List<Player> orderList = new ArrayList<>();
        orderList.add(player1);
        orderList.add(player2);
        Collections.rotate(orderList, -orderList.indexOf(game.test_getCurrPlayer()));

        DevCard devCard1 = new DevCard("DC1", null, 3, LevelDevCard.LEVEL1, ColorDevCard.BLUE,null);
        DevCard devCard2 = new DevCard("DC2", null, 5, LevelDevCard.LEVEL1, ColorDevCard.GREEN,null);
        DevCard devCard3 = new DevCard("DC3", null, 2, LevelDevCard.LEVEL1, ColorDevCard.YELLOW,null);
        DevCard devCard4 = new DevCard("DC4", null, 6, LevelDevCard.LEVEL2, ColorDevCard.BLUE,null);
        DevCard devCard5 = new DevCard("DC5", null, 3, LevelDevCard.LEVEL2, ColorDevCard.GREEN,null);
        DevCard devCard6 = new DevCard("DC6", null, 10, LevelDevCard.LEVEL2, ColorDevCard.YELLOW,null);
        DevCard devCard7 = new DevCard("DC7", null, 25, LevelDevCard.LEVEL3, ColorDevCard.GREEN,null);

        DevCard nothing = new DevCard("DC8", null, 1, LevelDevCard.LEVEL3, ColorDevCard.BLUE,null);

        assertTrue(board.addDevCard(DevCardSlot.LEFT,devCard1, game));
        assertTrue(board.addDevCard(DevCardSlot.RIGHT,devCard2, game));

        assertEquals(8,board.getVictoryPointsDevCards());

        assertTrue(board.addDevCard(DevCardSlot.CENTER,devCard3, game));
        assertEquals(10,board.getVictoryPointsDevCards());

        assertTrue(board.addDevCard(DevCardSlot.CENTER, devCard4, game));
        board.addDevCard(DevCardSlot.LEFT, devCard5, game);

        assertEquals(19,board.getVictoryPointsDevCards());

        assertTrue(board.addDevCard(DevCardSlot.RIGHT, devCard6, game));
        assertEquals(29,board.getVictoryPointsDevCards());

        //Obtaining the seventh card ends the turn of the Player and starts the EndGameLogic
        assertTrue(board.addDevCard(DevCardSlot.LEFT, devCard7, game));
        assertEquals(54, board.getVictoryPointsDevCards());

        //Do nothing
        assertFalse(board.addDevCard(DevCardSlot.CENTER, nothing, game));
        assertEquals(54, board.getVictoryPointsDevCards());
        //

        orderList.get(1).endThisTurn();

        assertFalse(game.test_getGameOnAir());

    }

    @Test
    public void countingLeaderPoints() throws EmptyDeckException, MissingCardException, PlayerStateException, LootTypeException, AlreadyInDeckException, WrongDepotException {
        PersonalBoard board = player1.test_getPB();


        Effect effect = new AddDepotEffect(ResourceType.STONE);

        List<Player> orderList = new ArrayList<>();
        orderList.add(player1);
        orderList.add(player2);
        Collections.rotate(orderList, -orderList.indexOf(game.test_getCurrPlayer()));
        Requisite cardReq1 = new ColorCardRequisite(ColorDevCard.GREEN, 3);
        Requisite cardReq2 = new CardRequisite(LevelDevCard.LEVEL1, ColorDevCard.YELLOW, 1);
        Requisite cardReq3 = new CardRequisite(LevelDevCard.LEVEL2, ColorDevCard.BLUE, 1);

        String ID1 = "";
        String ID2 = "";
        for (LeaderCard card : player1.test_getPB().viewLeaderCard().getCards()){
            if (ID1.equals("")){
                ID1 = card.getCardID();
            } else {
                ID2 = card.getCardID();
            }
        }
        player1.test_getPB().discardLeaderCard(ID1);
        player1.test_getPB().discardLeaderCard(ID2);

        //Adding the DevCards to activate LeaderCards
        DevCard devCard1 = new DevCard("DC1", null, 1, LevelDevCard.LEVEL1, ColorDevCard.GREEN,null);
        DevCard devCard2 = new DevCard("DC2", null, 1, LevelDevCard.LEVEL1, ColorDevCard.GREEN,null);
        DevCard devCard3 = new DevCard("DC3", null, 1, LevelDevCard.LEVEL1, ColorDevCard.YELLOW,null);
        DevCard devCard4 = new DevCard("DC4", null, 1, LevelDevCard.LEVEL2, ColorDevCard.BLUE,null);
        DevCard devCard5 = new DevCard("DC5", null, 1, LevelDevCard.LEVEL2, ColorDevCard.GREEN,null);

        board.addDevCard(DevCardSlot.LEFT, devCard1, game);
        board.addDevCard(DevCardSlot.CENTER, devCard2, game);
        board.addDevCard(DevCardSlot.RIGHT, devCard3, game);
        board.addDevCard(DevCardSlot.LEFT, devCard4, game);
        board.addDevCard(DevCardSlot.CENTER, devCard5, game);


        List<Requisite> requisites1 = new ArrayList<>();
        List<Requisite> requisites2 = new ArrayList<>();

        requisites1.add(cardReq1);

        requisites2.add(cardReq2);
        requisites2.add(cardReq3);

        LeaderCard l1 = new LeaderCard("LC1",effect, 5, requisites1);
        LeaderCard l2 = new LeaderCard("LC2", effect, 5, requisites2);

        player1.test_getPB().addLeaderCard(l1);
        player1.test_getPB().addLeaderCard(l2);


        assertEquals(0, player1.test_getPB().getVictoryPointsLeaderCards());


        player1.test_getPB().activateLeaderCard("LC1");


        assertEquals(player1.test_getPB().viewLeaderCard().peekCard("LC1").getVictoryPoint(),
                    player1.test_getPB().getVictoryPointsLeaderCards());


        player1.test_getPB().activateLeaderCard("LC2");

        assertEquals(
                player1.test_getPB().viewLeaderCard().peekCard("LC1").getVictoryPoint() +
                player1.test_getPB().viewLeaderCard().peekCard("LC2").getVictoryPoint(),
                player1.test_getPB().getVictoryPointsLeaderCards()
        );

        orderList.get(0).endThisTurn();
        assertTrue(game.test_getGameOnAir());
    }


    @RepeatedTest(15)
    public void countingTotalPoints() throws EndGameException, EmptyDeckException, MissingCardException, LootTypeException, WrongDepotException {
        Random rand = new Random();
        int max = 24;
        int randomNum = rand.nextInt(max);

        PersonalBoard board = player1.test_getPB();

        List<Player> orderList = new ArrayList<>();
        orderList.add(player1);
        orderList.add(player2);
        Collections.rotate(orderList, -orderList.indexOf(game.test_getCurrPlayer()));

        //Inserting resources into the warehouse
        player1.test_getPB().getWH_forTest().insertInDepot(DepotSlot.MIDDLE, buildStone(2));
        player1.test_getPB().getWH_forTest().insertInDepot(DepotSlot.BOTTOM, ResourceBuilder.buildCoin(3));
        player1.test_getPB().getWH_forTest().insertInDepot(DepotSlot.STRONGBOX, ResourceBuilder.buildShield(10));
        player1.test_getPB().getWH_forTest().insertInDepot(DepotSlot.STRONGBOX, ResourceBuilder.buildServant(10));
        player1.test_getPB().getWH_forTest().insertInDepot(DepotSlot.STRONGBOX, ResourceBuilder.buildCoin(10));
        player1.test_getPB().getWH_forTest().insertInDepot(DepotSlot.STRONGBOX, ResourceBuilder.buildStone(10));

        assertEquals(9,player1.test_getPB().getWH_forTest().countPointsWarehouse());

        //Moving the player into the FaithTrack
        player1.getFT_forTest().movePlayer(randomNum,game);


        //Activating LeaderCards
        String ID1 = "";
        String ID2 = "";
        for (LeaderCard card : player1.test_getPB().viewLeaderCard().getCards()){
            if (ID1.equals("")){
                ID1 = card.getCardID();
            } else {
                ID2 = card.getCardID();
            }
        }

        boolean activated;

        activated = player1.test_getPB().activateLeaderCard(ID1);
        player1.test_getPB().discardLeaderCard(ID2);

        if (activated) {
            assertEquals(player1.test_getPB().viewLeaderCard().peekCard(ID1).getVictoryPoint(),
                    player1.test_getPB().getVictoryPointsLeaderCards());
        }

        //Adding DevCards
        DevCard devCard1 = new DevCard("DC1", null, randomNum, LevelDevCard.LEVEL1, ColorDevCard.BLUE,null);
        DevCard devCard2 = new DevCard("DC2", null, randomNum, LevelDevCard.LEVEL1, ColorDevCard.GREEN,null);
        DevCard devCard3 = new DevCard("DC3", null, randomNum, LevelDevCard.LEVEL1, ColorDevCard.YELLOW,null);
        DevCard devCard4 = new DevCard("DC4", null, randomNum, LevelDevCard.LEVEL2, ColorDevCard.BLUE,null);

        board.addDevCard(DevCardSlot.LEFT, devCard1, game);
        board.addDevCard(DevCardSlot.CENTER, devCard2, game);
        board.addDevCard(DevCardSlot.RIGHT, devCard3, game);
        board.addDevCard(DevCardSlot.LEFT, devCard4, game);

        assertEquals(devCard1.getVictoryPoint() +
                devCard2.getVictoryPoint() +
                devCard3.getVictoryPoint() +
                devCard4.getVictoryPoint()
                ,player1.test_getPB().getVictoryPointsDevCards());

        //Counting TotalVictoryPoints
        if (activated) {
            assertEquals(player1.test_getPB().getWH_forTest().countPointsWarehouse() +
                    player1.getFT_forTest().countingFaithTrackVictoryPoints() +
                    player1.test_getPB().getVictoryPointsLeaderCards() +
                    player1.test_getPB().getVictoryPointsDevCards(), player1.test_getPB().getTotalVictoryPoints());
        } else {
            assertEquals(player1.test_getPB().getWH_forTest().countPointsWarehouse() +
                    player1.getFT_forTest().countingFaithTrackVictoryPoints() +
                    player1.test_getPB().getVictoryPointsDevCards(), player1.test_getPB().getTotalVictoryPoints());
        }

    }
}