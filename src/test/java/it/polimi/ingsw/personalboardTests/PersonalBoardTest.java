package it.polimi.ingsw.personalboardTests;


import static it.polimi.ingsw.model.resource.ResourceBuilder.buildStone;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.VirtualView;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.cards.effects.AddDepotEffect;
import it.polimi.ingsw.model.cards.effects.AddExtraProductionEffect;
import it.polimi.ingsw.model.cards.effects.AddProductionEffect;
import it.polimi.ingsw.model.cards.effects.Effect;
import it.polimi.ingsw.model.exceptions.card.AlreadyInDeckException;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;
import it.polimi.ingsw.model.exceptions.card.MissingCardException;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.exceptions.requisite.LootTypeException;
import it.polimi.ingsw.model.exceptions.requisite.NoRequisiteException;
import it.polimi.ingsw.model.exceptions.tray.OutOfBoundMarketTrayException;
import it.polimi.ingsw.model.exceptions.warehouse.NegativeResourcesDepotException;
import it.polimi.ingsw.model.exceptions.warehouse.UnobtainableResourceException;
import it.polimi.ingsw.model.exceptions.warehouse.WrongDepotException;
import it.polimi.ingsw.model.exceptions.warehouse.WrongPointsException;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
import it.polimi.ingsw.model.exceptions.warehouse.production.UnknownUnspecifiedException;
import it.polimi.ingsw.model.match.markettray.RowCol;
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
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.UnknownProduction;
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

    Match game = new MultiplayerMatch(2);

    Player player1;
    Player player2;

    VirtualView view = new VirtualView();

    @BeforeEach
    public void initialization() {
        assertDoesNotThrow(()->player1 = new Player("gino", game, view));
        assertTrue(game.playerJoin(player1));
        assertDoesNotThrow(()->player2 = new Player("pino", game, view));
        assertTrue(game.playerJoin(player2));

        assertTrue(game.startGame());

        assertDoesNotThrow(()-> game.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> game.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> game.test_getCurrPlayer().endThisTurn());

        assertDoesNotThrow(()-> game.test_getCurrPlayer().chooseResource(DepotSlot.BOTTOM, ResourceType.COIN));
        assertDoesNotThrow(()-> game.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> game.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(() -> assertEquals(game.test_getCurrPlayer().test_getPB().test_getDepots().get(DepotSlot.BOTTOM).viewResources().get(0), ResourceBuilder.buildCoin()));
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

        PersonalBoard finalPersonalBoard = new PersonalBoard(player1, null);

        assertDoesNotThrow(()->{
            if (finalPersonalBoard.addDevCard(Left, c1)) {
                assertEquals(c1, finalPersonalBoard.viewDevCards().get(Left));
            }

            if (finalPersonalBoard.addDevCard(Left, c2)) {
                assertEquals(c2, finalPersonalBoard.viewDevCards().get(Left));
            } else {
                fail();
            }

            if (finalPersonalBoard.addDevCard(Center, c2)) {
                fail();
            } else {
                try {
                    assertNull(finalPersonalBoard.viewDevCards().get(Center));
                } catch (NullPointerException ignore) {}
            }

            if (finalPersonalBoard.addDevCard(Left, c3)){
                assertEquals(c3, finalPersonalBoard.viewDevCards().get(Left));
            } else {
                fail();
            }

            if (finalPersonalBoard.addDevCard(Center, c3)){
                fail();
            } else {
                try {
                    assertNull(finalPersonalBoard.viewDevCards().get(Center));
                } catch (NullPointerException ignored) {}
            }

            if (finalPersonalBoard.addDevCard(Center, c1)){
                assertEquals(c1, finalPersonalBoard.viewDevCards().get(Center));
            } else {
                fail();
            }

            if (finalPersonalBoard.addDevCard(Center, c3)){
                fail();
            } else {
                assertEquals(c1, finalPersonalBoard.viewDevCards().get(Center));
            }

            if (finalPersonalBoard.addDevCard(Left, c31)){
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

        PersonalBoard personalBoard =  new PersonalBoard(game.test_getCurrPlayer(), view);

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

        PersonalBoard personalBoard = new PersonalBoard(game.test_getCurrPlayer(), view);

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
        PersonalBoard personalBoard = new PersonalBoard(player1, null);

        Resource twoStone = ResourceBuilder.buildStone(2);
        Resource oneStone = ResourceBuilder.buildStone(1);

        personalBoard.insertInDepot(DepotSlot.BUFFER, twoStone);
        personalBoard.moveResourceDepot(DepotSlot.BUFFER,DepotSlot.MIDDLE,twoStone);
        assertEquals(twoStone,personalBoard.viewDepotResource(DepotSlot.MIDDLE).get(0));
        assertNotEquals(oneStone,personalBoard.viewDepotResource(DepotSlot.MIDDLE).get(0));
        assertNotEquals(twoStone,personalBoard.viewDepotResource(DepotSlot.TOP).get(0));
    }


    /**
     * This test checks every method that involve production.
     */
    @Test
    void Production() {
        assertDoesNotThrow(()->{
            Player player = this.game.test_getCurrPlayer();

            List<Requisite> req = new ArrayList<>();
            Resource twoCoin = ResourceBuilder.buildCoin(2);
            Resource oneServant = ResourceBuilder.buildServant();
            List<Resource> resourceList = new ArrayList<>();
            resourceList.add(twoCoin);
            resourceList.add(oneServant);
            ResourceRequisite rr = new ResourceRequisite(twoCoin);
            req.add(rr);
            Production prod1 = new NormalProduction(resourceList, Collections.singletonList(buildStone(5)));

            DevCard c1 = new DevCard("000", new AddProductionEffect(prod1), 2, LevelDevCard.LEVEL1, ColorDevCard.GREEN, req);

            player.test_getPB().addDevCard(DevCardSlot.RIGHT, c1);
            player.test_getPB().addProduction(prod1, DevCardSlot.RIGHT);
            assertEquals(prod1, player.test_getPB().possibleProduction().get(ProductionID.RIGHT));


            Production prod2 = new NormalProduction(Collections.singletonList(ResourceBuilder.buildShield(2)), resourceList);
            Production prod3 = new NormalProduction(Arrays.asList(ResourceBuilder.buildStone(), ResourceBuilder.buildCoin()), Collections.singletonList(ResourceBuilder.buildFaithPoint(3)));

            DevCard c2 = new DevCard("111", new AddProductionEffect(prod2), 6, LevelDevCard.LEVEL1, ColorDevCard.BLUE, req);
            DevCard c3 = new DevCard("222", new AddProductionEffect(prod3), 4, LevelDevCard.LEVEL2, ColorDevCard.GREEN, req);
            DevCard c4 = new DevCard("333", new AddProductionEffect(prod1), 3, LevelDevCard.LEVEL3, ColorDevCard.YELLOW, req);

            player.test_getPB().addDevCard(DevCardSlot.CENTER, c2);
            player.test_getPB().addDevCard(DevCardSlot.RIGHT, c3);
            player.test_getPB().addProduction(prod2, DevCardSlot.CENTER);
            assertEquals(prod2, player.test_getPB().possibleProduction().get(ProductionID.CENTER));

            player.test_getPB().addProduction(prod3, DevCardSlot.RIGHT);
            assertEquals(prod3, player.test_getPB().possibleProduction().get(ProductionID.RIGHT));
            player.test_getPB().insertInDepot(DepotSlot.BUFFER, ResourceBuilder.buildShield(2));
            player.test_getPB().moveResourceDepot(DepotSlot.BUFFER, DepotSlot.BOTTOM, ResourceBuilder.buildShield(2));
            player.test_getPB().moveInProduction(DepotSlot.BOTTOM, ProductionID.CENTER, ResourceBuilder.buildShield(2));
            player.test_getPB().activateProductions();
            List<Resource> check = new ArrayList<>(Arrays.asList(ResourceBuilder.buildCoin(2), ResourceBuilder.buildStone(0), ResourceBuilder.buildShield(0), ResourceBuilder.buildServant(1)));
            assertEquals(check, player.test_getPB().viewDepotResource(DepotSlot.STRONGBOX));
            try {
                player.test_getPB().moveInProduction(DepotSlot.BOTTOM, ProductionID.CENTER, ResourceBuilder.buildShield(2));
                fail();
            } catch (NegativeResourcesDepotException e) {
            }
            player.test_getPB().activateProductions();
            assertEquals(check, player.test_getPB().viewDepotResource(DepotSlot.STRONGBOX));

            player.test_getPB().addDevCard(DevCardSlot.RIGHT, c4);
            player.test_getPB().addProduction(prod1, DevCardSlot.RIGHT);
            player.test_getPB().moveInProduction(DepotSlot.STRONGBOX, ProductionID.RIGHT, twoCoin);
            player.test_getPB().moveInProduction(DepotSlot.STRONGBOX, ProductionID.RIGHT, oneServant);
            player.test_getPB().insertInDepot(DepotSlot.BUFFER, ResourceBuilder.buildShield(2));
            player.test_getPB().moveResourceDepot(DepotSlot.BUFFER, DepotSlot.MIDDLE, ResourceBuilder.buildShield(2));
            player.test_getPB().moveInProduction(DepotSlot.MIDDLE, ProductionID.CENTER, ResourceBuilder.buildShield(2));
            player.test_getPB().activateProductions();
            List<Resource> check2 = new ArrayList<>(Arrays.asList(ResourceBuilder.buildCoin(2), ResourceBuilder.buildStone(5), ResourceBuilder.buildShield(0), ResourceBuilder.buildServant(1)));
            assertEquals(check2, player.test_getPB().viewDepotResource(DepotSlot.STRONGBOX));


            List<Requisite> LCreq = new ArrayList<>(Arrays.asList(new ResourceRequisite(ResourceBuilder.buildCoin())));
            Production extraProd = new UnknownProduction(Arrays.asList(ResourceBuilder.buildUnknown()), Arrays.asList(ResourceBuilder.buildUnknown(), ResourceBuilder.buildFaithPoint()));
            LeaderCard card = new LeaderCard("175", new AddExtraProductionEffect(extraProd), 5, LCreq);
        });
    }

    /**
     * This test create a personalBoard and moves the player on its faith track.
     */
    @Test
    void FaithTrackMoves() throws IllegalTypeInProduction, EndGameException {
        Resource first = ResourceBuilder.buildFaithPoint(1);
        Resource ten = ResourceBuilder.buildFaithPoint(10);

        Player player1 = new Player("gino",false);

        PersonalBoard personalBoard = new PersonalBoard(player1);

        Player player2 = new Player("gino",false);

        Match pm = new MultiplayerMatch(2);
        pm.playerJoin(player2);
        pm.playerJoin(player1);

        assertEquals(0,personalBoard.FaithMarkerPosition());
        personalBoard.moveFaithMarker(first.amount(), pm);

        assertEquals(1,personalBoard.FaithMarkerPosition());
        personalBoard.moveFaithMarker(first.amount(), pm);

        assertEquals(2,personalBoard.FaithMarkerPosition());
        personalBoard.moveFaithMarker(ten.amount(), pm);

        assertEquals(12,personalBoard.FaithMarkerPosition());
        personalBoard.moveFaithMarker(ten.amount(), pm);

        assertEquals(22,personalBoard.FaithMarkerPosition());

        try{
            personalBoard.moveFaithMarker(ten.amount(), pm);
            fail();
        }
        catch (EndGameException e){ }


        //List<Requisite> LCreq = new ArrayList<>(Arrays.asList(new ResourceRequisite(ResourceBuilder.buildCoin())));
        //Production extraProd = new UnknownProduction(Arrays.asList(ResourceBuilder.buildUnknown()), Arrays.asList(ResourceBuilder.buildUnknown(), ResourceBuilder.buildFaithPoint()));
        //LeaderCard card = new LeaderCard("175", new AddExtraProductionEffect(extraProd), 5, LCreq);

    }

    /**
     *
     */
    @Test
    public void countsDevCardsPoints() {
        PersonalBoard board = player1.test_getPB();

        DevCard devCard1 = new DevCard("DC1", null, 3, LevelDevCard.LEVEL1, ColorDevCard.BLUE,null);
        DevCard devCard2 = new DevCard("DC2", null, 5, LevelDevCard.LEVEL1, ColorDevCard.GREEN,null);
        DevCard devCard3 = new DevCard("DC3", null, 2, LevelDevCard.LEVEL1, ColorDevCard.YELLOW,null);
        DevCard devCard4 = new DevCard("DC4", null, 6, LevelDevCard.LEVEL2, ColorDevCard.BLUE,null);
        DevCard devCard5 = new DevCard("DC5", null, 3, LevelDevCard.LEVEL2, ColorDevCard.GREEN,null);
        DevCard devCard6 = new DevCard("DC6", null, 10, LevelDevCard.LEVEL2, ColorDevCard.YELLOW,null);
        DevCard devCard7 = new DevCard("DC7", null, 25, LevelDevCard.LEVEL3, ColorDevCard.GREEN,null);

        DevCard nothing = new DevCard("DC8", null, 1, LevelDevCard.LEVEL3, ColorDevCard.BLUE,null);

        PersonalBoard finalBoard = board;
        assertDoesNotThrow(()->{
            assertTrue(finalBoard.addDevCard(DevCardSlot.LEFT, devCard1));
            assertTrue(finalBoard.addDevCard(DevCardSlot.RIGHT, devCard2));

            assertEquals(8, finalBoard.getVictoryPointsDevCards());

            assertTrue(finalBoard.addDevCard(DevCardSlot.CENTER, devCard3));
            assertEquals(10, finalBoard.getVictoryPointsDevCards());

            assertTrue(finalBoard.addDevCard(DevCardSlot.CENTER, devCard4));
            finalBoard.addDevCard(DevCardSlot.LEFT, devCard5);

            assertEquals(19, finalBoard.getVictoryPointsDevCards());

            assertTrue(finalBoard.addDevCard(DevCardSlot.RIGHT, devCard6));
            assertEquals(29, finalBoard.getVictoryPointsDevCards());

            //Obtaining the seventh card ends the turn of the Player and starts the EndGameLogic
            try {
                assertTrue(finalBoard.addDevCard(DevCardSlot.LEFT, devCard7));
            } catch (EndGameException ignore) {}
            assertEquals(54, finalBoard.getVictoryPointsDevCards());

            //Do nothing
            try {
                assertTrue(finalBoard.addDevCard(DevCardSlot.LEFT, nothing));
            } catch (EndGameException ignore) {}
            assertEquals(54, finalBoard.getVictoryPointsDevCards());
        });
    }

    @Test
    public void countingLeaderPoints()  {

        assertDoesNotThrow(()->{
            PersonalBoard board = player1.test_getPB();


            Effect effect = new AddDepotEffect(ResourceType.STONE);

            Requisite cardReq1 = new ColorCardRequisite(ColorDevCard.GREEN, 3);
            Requisite cardReq2 = new CardRequisite(LevelDevCard.LEVEL1, ColorDevCard.YELLOW, 1);
            Requisite cardReq3 = new CardRequisite(LevelDevCard.LEVEL2, ColorDevCard.BLUE, 1);

            String ID1 = "";
            String ID2 = "";
            for (LeaderCard card : board.viewLeaderCard().getCards()) {
                if (ID1.equals("")) {
                    ID1 = card.getCardID();
                } else {
                    ID2 = card.getCardID();
                }
            }

            //Adding the DevCards to activate LeaderCards
            DevCard devCard1 = new DevCard("DC1", null, 1, LevelDevCard.LEVEL1, ColorDevCard.GREEN, null);
            DevCard devCard2 = new DevCard("DC2", null, 1, LevelDevCard.LEVEL1, ColorDevCard.GREEN, null);
            DevCard devCard3 = new DevCard("DC3", null, 1, LevelDevCard.LEVEL1, ColorDevCard.YELLOW, null);
            DevCard devCard4 = new DevCard("DC4", null, 1, LevelDevCard.LEVEL2, ColorDevCard.BLUE, null);
            DevCard devCard5 = new DevCard("DC5", null, 1, LevelDevCard.LEVEL2, ColorDevCard.GREEN, null);


            board.addDevCard(DevCardSlot.LEFT, devCard1);
            board.addDevCard(DevCardSlot.CENTER, devCard2);
            board.addDevCard(DevCardSlot.RIGHT, devCard3);
            board.addDevCard(DevCardSlot.LEFT, devCard4);
            board.addDevCard(DevCardSlot.CENTER, devCard5);


            List<Requisite> requisites1 = new ArrayList<>();
            List<Requisite> requisites2 = new ArrayList<>();

            requisites1.add(cardReq1);

            requisites2.add(cardReq2);
            requisites2.add(cardReq3);

            LeaderCard l1 = new LeaderCard("LC1", effect, 5, requisites1);
            LeaderCard l2 = new LeaderCard("LC2", effect, 5, requisites2);

            board.addLeaderCard(l1);
            board.addLeaderCard(l2);


            assertEquals(0, board.getVictoryPointsLeaderCards());


            board.activateLeaderCard("LC1");


            assertEquals(board.viewLeaderCard().peekCard("LC1").getVictoryPoint(),
                    board.getVictoryPointsLeaderCards());


            board.activateLeaderCard("LC2");

            assertEquals(
                    board.viewLeaderCard().peekCard("LC1").getVictoryPoint() +
                            board.viewLeaderCard().peekCard("LC2").getVictoryPoint(),
                    board.getVictoryPointsLeaderCards()
            );
        });

    }


    @RepeatedTest(15)
    public void countingTotalPoints() throws EndGameException, EmptyDeckException, MissingCardException, LootTypeException, WrongDepotException {
        Random rand = new Random();
        int max = 24;
        int randomNum = rand.nextInt(max);

        List<Player> orderList = new ArrayList<>();
        orderList.add(game.test_getCurrPlayer());
        Collections.rotate(orderList, -orderList.indexOf(game.test_getCurrPlayer()));

        PersonalBoard board = orderList.get(0).test_getPB();

        //Inserting resources into the warehouse
        orderList.get(0).test_getPB().getWH_forTest().insertInDepot(DepotSlot.MIDDLE, buildStone(2));
        orderList.get(0).test_getPB().getWH_forTest().insertInDepot(DepotSlot.BOTTOM, ResourceBuilder.buildCoin(3));
        orderList.get(0).test_getPB().getWH_forTest().insertInDepot(DepotSlot.STRONGBOX, ResourceBuilder.buildShield(10));
        orderList.get(0).test_getPB().getWH_forTest().insertInDepot(DepotSlot.STRONGBOX, ResourceBuilder.buildServant(10));
        orderList.get(0).test_getPB().getWH_forTest().insertInDepot(DepotSlot.STRONGBOX, ResourceBuilder.buildCoin(10));
        orderList.get(0).test_getPB().getWH_forTest().insertInDepot(DepotSlot.STRONGBOX, ResourceBuilder.buildStone(10));

        assertEquals(9,orderList.get(0).test_getPB().getWH_forTest().countPointsWarehouse());

        //Moving the player into the FaithTrack
        orderList.get(0).getFT_forTest().movePlayer(randomNum,game);


        //Activating LeaderCards
        String ID1 = "";
        String ID2 = "";
        for (LeaderCard card : orderList.get(0).test_getPB().viewLeaderCard().getCards()){
            if (ID1.equals("")){
                ID1 = card.getCardID();
            } else {
                ID2 = card.getCardID();
            }
        }

        boolean activated;

        activated = orderList.get(0).test_getPB().activateLeaderCard(ID1);
        orderList.get(0).test_getPB().discardLeaderCard(ID2);

        if (activated) {
            assertEquals(orderList.get(0).test_getPB().viewLeaderCard().peekCard(ID1).getVictoryPoint(),
                    orderList.get(0).test_getPB().getVictoryPointsLeaderCards());
        }

        //Adding DevCards
        DevCard devCard1 = new DevCard("DC1", null, randomNum, LevelDevCard.LEVEL1, ColorDevCard.BLUE,null);
        DevCard devCard2 = new DevCard("DC2", null, randomNum, LevelDevCard.LEVEL1, ColorDevCard.GREEN,null);
        DevCard devCard3 = new DevCard("DC3", null, randomNum, LevelDevCard.LEVEL1, ColorDevCard.YELLOW,null);
        DevCard devCard4 = new DevCard("DC4", null, randomNum, LevelDevCard.LEVEL2, ColorDevCard.BLUE,null);

        assertDoesNotThrow(()->{
            board.addDevCard(DevCardSlot.LEFT, devCard1);
            board.addDevCard(DevCardSlot.CENTER, devCard2);
            board.addDevCard(DevCardSlot.RIGHT, devCard3);
            board.addDevCard(DevCardSlot.LEFT, devCard4);
        });

        assertEquals(devCard1.getVictoryPoint() +
                devCard2.getVictoryPoint() +
                devCard3.getVictoryPoint() +
                devCard4.getVictoryPoint()
                ,orderList.get(0).test_getPB().getVictoryPointsDevCards());

        //Counting TotalVictoryPoints
        if (activated) {
            assertEquals(orderList.get(0).test_getPB().getWH_forTest().countPointsWarehouse() +
                    orderList.get(0).getFT_forTest().countingFaithTrackVictoryPoints() +
                    orderList.get(0).test_getPB().getVictoryPointsLeaderCards() +
                    orderList.get(0).test_getPB().getVictoryPointsDevCards(), orderList.get(0).test_getPB().getTotalVictoryPoints());
        } else {
            assertEquals(orderList.get(0).test_getPB().getWH_forTest().countPointsWarehouse() +
                    orderList.get(0).getFT_forTest().countingFaithTrackVictoryPoints() +
                    orderList.get(0).test_getPB().getVictoryPointsDevCards(), orderList.get(0).test_getPB().getTotalVictoryPoints());
        }

    }
}