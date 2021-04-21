package it.polimi.ingsw.personalboardTests;


import static it.polimi.ingsw.model.resource.ResourceBuilder.buildStone;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.cards.effects.AddExtraProductionEffect;
import it.polimi.ingsw.model.cards.effects.AddProductionEffect;
import it.polimi.ingsw.model.exceptions.PlayerStateException;
import it.polimi.ingsw.model.exceptions.card.AlreadyInDeckException;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;
import it.polimi.ingsw.model.exceptions.card.MissingCardException;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
import it.polimi.ingsw.model.match.match.Match;
import it.polimi.ingsw.model.match.match.MultiplayerMatch;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;
import it.polimi.ingsw.model.player.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.NormalProduction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.Production;
import it.polimi.ingsw.model.requisite.Requisite;
import it.polimi.ingsw.model.requisite.ResourceRequisite;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
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
        assertDoesNotThrow(()-> game.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> game.test_getCurrPlayer().test_discardLeader());
    }

    /**
     * This test creates a PersonalBoard and add
     */
    @Test
    void DevCards() {
        List<Requisite> req = new ArrayList<>();
        Resource coin = ResourceBuilder.buildCoin(2);
        ResourceRequisite rr = new ResourceRequisite(coin);
        req.add(rr);
        Production p = null;
        try {
            p = new NormalProduction(Collections.singletonList(buildStone()), Collections.singletonList(buildStone()));
        } catch (IllegalTypeInProduction illegalTypeInProduction) {
            fail();
        }

        DevCard c1 = new DevCard("000", new AddProductionEffect(p), 2, LevelDevCard.LEVEL1, ColorDevCard.GREEN, req);
        DevCard c2 = new DevCard("111", new AddProductionEffect(p), 4, LevelDevCard.LEVEL2, ColorDevCard.YELLOW, req);
        DevCard c3 = new DevCard("222", new AddProductionEffect(p), 6, LevelDevCard.LEVEL3, ColorDevCard.BLUE, req);
        DevCard c31 = new DevCard("333", new AddProductionEffect(p), 0, LevelDevCard.LEVEL3, ColorDevCard.PURPLE, req);

        DevCardSlot dcsLeft = DevCardSlot.LEFT;
        DevCardSlot dcsCenter = DevCardSlot.CENTER;

        Match match = new MultiplayerMatch();

        Player player = null;
        try {
            player = new Player("gino",match);
        } catch (IllegalTypeInProduction e1) {
            fail();
        }

        PersonalBoard personalBoard = null;
        try {
            personalBoard = new PersonalBoard(player);
        } catch (IllegalTypeInProduction e2) {
            fail();
        }

        PersonalBoard finalPersonalBoard = personalBoard;
        assertDoesNotThrow(()->{
            if (finalPersonalBoard.addDevCard(dcsLeft, c1, null)) {
                assertEquals(c1, finalPersonalBoard.viewDevCards().get(dcsLeft));
            }

            if (finalPersonalBoard.addDevCard(dcsLeft, c2, null)) {
                assertEquals(c2, finalPersonalBoard.viewDevCards().get(dcsLeft));
            } else {
                fail();
            }

            if (finalPersonalBoard.addDevCard(dcsCenter, c2, null)) {
                fail();
            } else {
                try {
                    assertNull(finalPersonalBoard.viewDevCards().get(dcsCenter));
                } catch (NullPointerException ignore) {}
            }

            if (finalPersonalBoard.addDevCard(dcsLeft, c3, null)){
                assertEquals(c3, finalPersonalBoard.viewDevCards().get(dcsLeft));
            } else {
                fail();
            }

            if (finalPersonalBoard.addDevCard(dcsCenter, c3, null)){
                fail();
            } else {
                try {
                    assertNull(finalPersonalBoard.viewDevCards().get(dcsCenter));
                } catch (NullPointerException ignored) {}
            }

            if (finalPersonalBoard.addDevCard(dcsCenter, c1, null)){
                assertEquals(c1, finalPersonalBoard.viewDevCards().get(dcsCenter));
            } else {
                fail();
            }

            if (finalPersonalBoard.addDevCard(dcsCenter, c3, null)){
                fail();
            } else {
                assertEquals(c1, finalPersonalBoard.viewDevCards().get(dcsCenter));
            }

            if (finalPersonalBoard.addDevCard(dcsLeft, c31, null)){
                fail();
            } else {
                assertEquals(c3, finalPersonalBoard.viewDevCards().get(dcsLeft));
            }
        });

    }

    /**
     * This test creates two LeaderCards, adds them to the deck and activate them.
     */
    @Test
    void ActivateLeaderCard() throws MissingCardException, AlreadyInDeckException {
        String ID1="000", ID2="111";
        List<Resource> sample = new ArrayList<>();

        Production p = null;
        try {
            p = new NormalProduction( sample, sample);
        } catch (IllegalTypeInProduction illegalTypeInProduction) {
            fail();
        }

        List<Requisite> req = new ArrayList<>();
        Resource coin = ResourceBuilder.buildCoin(2);
        ResourceRequisite rr = new ResourceRequisite(coin);
        req.add(rr);



        LeaderCard c1 = new LeaderCard(ID1, new AddExtraProductionEffect(p), 1, req);
        LeaderCard c2 = new LeaderCard(ID2, new AddExtraProductionEffect(p), 2, req);

        Match match = new MultiplayerMatch();

        Player player = null;
        try {
            player = new Player("gino",match);
        } catch (IllegalTypeInProduction e1) {
            fail();
        }

        PersonalBoard personalBoard = null;
        try {
            personalBoard = new PersonalBoard(player);
        } catch (IllegalTypeInProduction e2) {
            fail();
        }

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

        try {
            assertFalse(personalBoard.viewLeaderCard().peekCard(ID1).isActivated());
        } catch (MissingCardException e) {
           e.printStackTrace();
        }

        try {
            personalBoard.activateLeaderCard(ID1);
        } catch (EmptyDeckException e) {
            fail();
        }


        try {
            assertTrue(personalBoard.viewLeaderCard().peekCard(ID1).isActivated());
        } catch (MissingCardException e) {
            fail();
        }
    }

    /**
     * This test creates two LeaderCards and discard them one by one
     */
    @Test
    void DiscardLeaderCard() throws EmptyDeckException, MissingCardException, AlreadyInDeckException {
        String ID1="000", ID2="111";

        Production p = null;
        try {
            p = new NormalProduction(Collections.singletonList(buildStone()), Collections.singletonList(buildStone()));
        } catch (IllegalTypeInProduction illegalTypeInProduction) {
            fail();
        }

        List<Requisite> req = new ArrayList<>();
        Resource coin = ResourceBuilder.buildCoin(2);
        ResourceRequisite rr = new ResourceRequisite(coin);
        req.add(rr);

        LeaderCard c1 = new LeaderCard(ID1, new AddProductionEffect(p), 1, req);
        LeaderCard c2 = new LeaderCard(ID2, new AddProductionEffect(p), 2, req);

        Match match = new MultiplayerMatch();

        Player player = null;
        try {
            player = new Player("gino",match);
        } catch (IllegalTypeInProduction e1) {
            e1.printStackTrace();
        }

        PersonalBoard personalBoard = null;
        try {
            personalBoard = new PersonalBoard(player);
        } catch (IllegalTypeInProduction e2) {
            e2.printStackTrace();
        }

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
        } catch (MissingCardException e) {
            e.printStackTrace();
        }

        // todo remove try when implemented leader cards
        try {
            personalBoard.discardLeaderCard(ID1);
        } catch (MissingCardException e) {
            e.printStackTrace();
        }
        try {
            personalBoard.discardLeaderCard(ID2);
        } catch (MissingCardException e) {
            e.printStackTrace();
        }

        try {
            personalBoard.viewLeaderCard().peekCard(ID1);
            fail();
        } catch (MissingCardException e) {
            e.printStackTrace();
        }

        assertEquals(0,personalBoard.viewLeaderCard().getNumberOfCards());

    }

    /**
     * This test
     */
    @Test
    void Resources(){
        Match match = new MultiplayerMatch();

        Player player = null;
        try {
            player = new Player("gino",match);
        } catch (IllegalTypeInProduction e1) {
            e1.printStackTrace();
        }

        try {
            PersonalBoard personalBoard = new PersonalBoard(player);
        } catch (IllegalTypeInProduction e2) {
           fail();
        }

    }

    /**
     * This test create a personalBoard and moves the player and Lorenzo on its faith track.
     */
    @Test
    void FaithTrackMoves() {
        Resource first = ResourceBuilder.buildFaithPoint(1);
        Resource ten = ResourceBuilder.buildFaithPoint(10);

        Match match = new MultiplayerMatch();

        Player player = null;
        try {
            player = new Player("gino",match);
        } catch (IllegalTypeInProduction e1) {
            fail();
        }
        Player player2 = null;
        try {
            player2 = new Player("Gino",match);
        } catch (IllegalTypeInProduction e1) {
            fail();
        }

        PersonalBoard personalBoard = null;
        try {
            personalBoard = new PersonalBoard(player);
        } catch (IllegalTypeInProduction e2) {
            fail();
        }

        Match pm = new MultiplayerMatch();
        pm.playerJoin(player2);
        pm.playerJoin(player);

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
    public void countsDevCardsPoints() throws PlayerStateException {

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

    @RepeatedTest(10)
    public void countingLeaderPoints() throws EmptyDeckException, MissingCardException, PlayerStateException {
        PersonalBoard board = player1.test_getPB();

        List<Player> orderList = new ArrayList<>();
        orderList.add(player1);
        orderList.add(player2);
        Collections.rotate(orderList, -orderList.indexOf(game.test_getCurrPlayer()));

        String ID1 = "";
        String ID2 = "";
        for (LeaderCard card : player1.test_getPB().viewLeaderCard().getCards()){
            if (ID1.equals("")){
                ID1 = card.getCardID();
            } else {
                ID2 = card.getCardID();
            }
        }

        assertEquals(0, player1.test_getPB().getVictoryPointsLeaderCards());

        player1.test_getPB().activateLeaderCard(ID1);

        assertEquals(player1.test_getPB().viewLeaderCard().peekCard(ID1).getVictoryPoint(),
                player1.test_getPB().getVictoryPointsLeaderCards()
        );

        player1.test_getPB().activateLeaderCard(ID2);

        assertEquals(
                player1.test_getPB().viewLeaderCard().peekCard(ID1).getVictoryPoint() +
                player1.test_getPB().viewLeaderCard().peekCard(ID2).getVictoryPoint(),
                player1.test_getPB().getVictoryPointsLeaderCards()
        );

        orderList.get(0).endThisTurn();
        assertTrue(game.test_getGameOnAir());
    }


    @RepeatedTest(10)
    public void countingTotalPoints() throws EndGameException, EmptyDeckException, MissingCardException {
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
        player1.test_getPB().getWH_forTest().insertInDepot(DepotSlot.STRONGBOX, ResourceBuilder.buildServant(5));
        player1.test_getPB().getWH_forTest().insertInDepot(DepotSlot.STRONGBOX, ResourceBuilder.buildCoin(5));

        assertEquals(5,player1.test_getPB().getWH_forTest().countPointsWarehouse());

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

        player1.test_getPB().activateLeaderCard(ID1);
        player1.test_getPB().discardLeaderCard(ID2);

        assertEquals(player1.test_getPB().viewLeaderCard().peekCard(ID1).getVictoryPoint(),
                player1.test_getPB().getVictoryPointsLeaderCards());

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

        assertEquals(player1.test_getPB().getWH_forTest().countPointsWarehouse()+
                player1.getFT_forTest().countingFaithTrackVictoryPoints() +
                player1.test_getPB().getVictoryPointsLeaderCards() +
                player1.test_getPB().getVictoryPointsDevCards(), player1.test_getPB().getTotalVictoryPoints());

    }
}