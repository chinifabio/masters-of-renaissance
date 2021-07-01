package it.polimi.ingsw;

import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.model.VirtualView;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.DevCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.cards.effects.AddDiscountEffect;
import it.polimi.ingsw.model.cards.effects.AddProductionEffect;
import it.polimi.ingsw.model.exceptions.card.AlreadyInDeckException;
import it.polimi.ingsw.model.exceptions.card.MissingCardException;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.exceptions.requisite.LootTypeException;
import it.polimi.ingsw.model.exceptions.warehouse.NegativeResourcesDepotException;
import it.polimi.ingsw.model.exceptions.warehouse.UnobtainableResourceException;
import it.polimi.ingsw.model.exceptions.warehouse.WrongDepotException;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.Marble;
import it.polimi.ingsw.model.match.markettray.RowCol;
import it.polimi.ingsw.model.match.match.Match;
import it.polimi.ingsw.model.match.match.MultiplayerMatch;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.NormalProduction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.Production;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.model.requisite.Requisite;
import it.polimi.ingsw.model.requisite.ResourceRequisite;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.view.View;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest{
    Player player1;
    Player player2;

    VirtualView view = new VirtualView();
    Match game;

    List<Player> order = new ArrayList<>();

    @BeforeEach
    public void initialization() {
        try {
            game = new MultiplayerMatch(2, view);
        } catch (IOException e) {
            fail(e.getMessage());
        }

        assertDoesNotThrow(()->player1 = new Player("gino", game, view));
        assertTrue(game.playerJoin(player1));
        order.add(player1);
        assertEquals("gino",player1.toString());

        assertDoesNotThrow(()->player2 = new Player("pino", game, view));
        assertTrue(game.playerJoin(player2));
        order.add(player2);

        assertFalse(game.currentPlayer().canDoStuff());

        order.get(0).disconnect();
        game.reconnectPlayer(order.get(0).getNickname());

        game.initialize();
        Collections.rotate(order, order.indexOf(game.currentPlayer()));
        assertTrue(game.isGameOnAir());

        assertDoesNotThrow(()-> order.get(0).test_discardLeader());
        order.get(0).disconnect();
        game.reconnectPlayer(order.get(0).getNickname());
        game.currentPlayer().startHisTurn();
        assertDoesNotThrow(()-> order.get(0).test_discardLeader());
        assertTrue(order.get(0).canDoStuff());
        order.get(0).endThisTurn();
        assertFalse(order.get(0).canDoStuff());
        order.get(0).disconnect();
        game.reconnectPlayer(order.get(0).getNickname());

        order.get(1).chooseResource(DepotSlot.BOTTOM, ResourceType.COIN);
        assertDoesNotThrow(()-> order.get(1).test_discardLeader());
        assertDoesNotThrow(()-> order.get(1).test_discardLeader());
        assertDoesNotThrow(() -> assertEquals(order.get(1).test_getPB().getDepots().get(DepotSlot.BOTTOM).viewResources().get(0), ResourceBuilder.buildCoin()));
        order.get(1).endThisTurn();

    }

    @RepeatedTest(25)
    public void BuyDevCard() throws LootTypeException {
        DevCard card1 = game.test_viewDevSetup().get(0);
        DevCard card2 = game.test_viewDevSetup().get(3);

        game.currentPlayer().buyDevCard(LevelDevCard.LEVEL1,ColorDevCard.BLUE,DevCardSlot.CENTER);

        for(int i=0 ; i<card1.getCost().size() ; i++){
            Resource temp = ResourceBuilder.buildFromType(card1.getCost().get(i).getType(), card1.getCost().get(i).getAmount());
            assertTrue(game.currentPlayer().obtainResource(DepotSlot.STRONGBOX,temp));
        }

        assertTrue(game.currentPlayer().canDoStuff());
        game.currentPlayer().buyCard();
        assertTrue(game.currentPlayer().canDoStuff());
        for(int i=0 ; i<card1.getCost().size() ; i++){
            Resource temp = ResourceBuilder.buildFromType(card1.getCost().get(i).getType(), card1.getCost().get(i).getAmount());
            game.currentPlayer().moveBetweenDepot(DepotSlot.STRONGBOX,DepotSlot.DEVBUFFER,temp);
        }
        game.currentPlayer().buyDevCard(LevelDevCard.LEVEL1, ColorDevCard.GREEN, DevCardSlot.LEFT);
        assertEquals(card1,game.currentPlayer().test_getPB().viewDevCards().get(DevCardSlot.LEFT));

        game.currentPlayer().endThisTurn();
        assertTrue(game.currentPlayer().canDoStuff());
        game.currentPlayer().useMarketTray(RowCol.COL,1);
        game.currentPlayer().endThisTurn();
        assertTrue(game.currentPlayer().canDoStuff());
        game.currentPlayer().buyCard();

        for(int i=0 ; i<card2.getCost().size() ; i++) {
            Resource temp = ResourceBuilder.buildFromType(card2.getCost().get(i).getType(), card2.getCost().get(i).getAmount());
            assertTrue(game.currentPlayer().obtainResource(DepotSlot.STRONGBOX,temp));
        }

        if(card2.getCost().size() > 1) {
            for(int i=0 ; i<card2.getCost().size()-1 ; i++) {
                Resource temp = ResourceBuilder.buildFromType(card2.getCost().get(i).getType(), card2.getCost().get(i).getAmount());
                game.currentPlayer().moveBetweenDepot(DepotSlot.STRONGBOX,DepotSlot.DEVBUFFER,temp);
            }
            game.currentPlayer().buyDevCard(LevelDevCard.LEVEL1, ColorDevCard.YELLOW, DevCardSlot.RIGHT);
            for(int i=0 ; i<card2.getCost().size() ; i++) {
                Resource temp = ResourceBuilder.buildFromType(card2.getCost().get(i).getType(), card2.getCost().get(i).getAmount());
                assertTrue(game.currentPlayer().test_getPB().getWH_forTest().getTotalResources().contains(temp));
            }
        }
        else {
            Resource temp = ResourceBuilder.buildFromType(card2.getCost().get(0).getType(), card2.getCost().get(0).getAmount()-1);
            game.currentPlayer().moveBetweenDepot(DepotSlot.STRONGBOX,DepotSlot.DEVBUFFER,temp);
            game.currentPlayer().addDiscount(ResourceBuilder.buildFromType(card2.getCost().get(0).getType(),1).type());
            game.currentPlayer().buyDevCard(LevelDevCard.LEVEL1, ColorDevCard.YELLOW, DevCardSlot.RIGHT);
            assertEquals(card2,game.currentPlayer().test_getPB().viewDevCards().get(DevCardSlot.RIGHT));
        }

        if(!game.currentPlayer().canDoStuff()){
            game.currentPlayer().endThisTurn();
        }
        game.currentPlayer().buyCard();
        Player p = game.currentPlayer();
        game.currentPlayer().disconnect();
        game.reconnectPlayer(p.getNickname());
    }

    @Test
    public void LeaderCard() throws WrongDepotException, MissingCardException {

        List<Requisite> req = new ArrayList<>();
        ResourceRequisite rr = new ResourceRequisite(ResourceBuilder.buildCoin(2));
        req.add(rr);

        LeaderCard c1 = new LeaderCard("111", new AddDiscountEffect(ResourceType.COIN), 1, req);
        LeaderCard c2 = new LeaderCard("000", new AddDiscountEffect(ResourceType.COIN),0, req);
        game.currentPlayer().addLeader(c1);
        game.currentPlayer().addLeader(c2);

        game.currentPlayer().activateLeaderCard(c1.getCardID());
        game.currentPlayer().test_getPB().getWH_forTest().insertInDepot(DepotSlot.BOTTOM, ResourceBuilder.buildCoin(2));

        //test
        game.currentPlayer().moveBetweenDepot(DepotSlot.BOTTOM, DepotSlot.DEVBUFFER, ResourceBuilder.buildCoin(2));
        game.currentPlayer().moveBetweenDepot(DepotSlot.BOTTOM, DepotSlot.MIDDLE, ResourceBuilder.buildCoin(2));
        //finish test
        game.currentPlayer().activateLeaderCard(c1.getCardID());
        //game.currentPlayer().test_getPB().getWH_forTest().insertInDepot(DepotSlot.STRONGBOX, ResourceBuilder.buildCoin(2));


        assertTrue(game.currentPlayer().test_getPB().viewLeaderCard().peekCard(c1.getCardID()).isActivated());

        game.currentPlayer().discardLeader(c2.getCardID());
        assertEquals(1,game.currentPlayer().personalBoard.faithTrack.getPlayerPosition());
        game.currentPlayer().discardLeader(c2.getCardID());
        assertEquals(1,game.currentPlayer().personalBoard.faithTrack.getPlayerPosition());
    }

    @Test
    public void Productions() throws IllegalTypeInProduction, EndGameException, AlreadyInDeckException, WrongDepotException, UnobtainableResourceException, NegativeResourcesDepotException {

        List<Requisite> req = new ArrayList<>();
        Resource twoCoin = ResourceBuilder.buildCoin(2);
        Resource oneServant = ResourceBuilder.buildServant();
        List<Resource> resourceList = new ArrayList<>();
        resourceList.add(twoCoin);
        resourceList.add(oneServant);
        ResourceRequisite rr = new ResourceRequisite(twoCoin);
        req.add(rr);
        Production prod1 = new NormalProduction(resourceList, Collections.singletonList(ResourceBuilder.buildStone(5)));

        DevCard c1 = new DevCard("000", new AddProductionEffect(prod1), 2, LevelDevCard.LEVEL1, ColorDevCard.GREEN, req);

        assertTrue(game.currentPlayer().canDoStuff());
        game.currentPlayer().test_getPB().addDevCard(DevCardSlot.RIGHT,c1);
        game.currentPlayer().test_getPB().addProduction(prod1,DevCardSlot.RIGHT);
        assertEquals(prod1,game.currentPlayer().test_getPB().possibleProduction().get(ProductionID.RIGHT));

        Production prod2 = new NormalProduction(Collections.singletonList(ResourceBuilder.buildShield(2)),resourceList);
        Production prod3 = new NormalProduction(Arrays.asList(ResourceBuilder.buildStone(),ResourceBuilder.buildCoin()),Collections.singletonList(ResourceBuilder.buildFaithPoint(3)));

        List<Resource> rl = new ArrayList<>(Collections.singletonList(ResourceBuilder.buildCoin()));
        Production prod4 = new NormalProduction(rl,Collections.singletonList(ResourceBuilder.buildFaithPoint()));

        DevCard c2 = new DevCard("111", new AddProductionEffect(prod2), 6, LevelDevCard.LEVEL1, ColorDevCard.BLUE, req);
        DevCard c3 = new DevCard("222", new AddProductionEffect(prod3), 4, LevelDevCard.LEVEL2, ColorDevCard.GREEN, req);
        DevCard c4 = new DevCard("333", new AddProductionEffect(prod4), 5, LevelDevCard.LEVEL1, ColorDevCard.YELLOW, req);

        game.currentPlayer().test_getPB().addDevCard(DevCardSlot.CENTER,c2);
        game.currentPlayer().test_getPB().addDevCard(DevCardSlot.RIGHT,c3);

        game.currentPlayer().test_getPB().addProduction(prod2,DevCardSlot.CENTER);
        assertEquals(prod2, game.currentPlayer().test_getPB().possibleProduction().get(ProductionID.CENTER));
        game.currentPlayer().test_getPB().addProduction(prod3,DevCardSlot.RIGHT);
        assertEquals(prod3, game.currentPlayer().test_getPB().possibleProduction().get(ProductionID.RIGHT));

        game.currentPlayer().test_getPB().insertInDepot(DepotSlot.BUFFER,ResourceBuilder.buildShield(2));
        game.currentPlayer().test_getPB().moveResourceDepot(DepotSlot.BUFFER,DepotSlot.BOTTOM,ResourceBuilder.buildShield(2));

        //test
        game.currentPlayer().setNormalProduction(ProductionID.BASIC, new NormalProduction(Collections.singletonList(ResourceBuilder.buildCoin(2)), Collections.singletonList(ResourceBuilder.buildShield())));

        game.currentPlayer().production();
        game.currentPlayer().moveInProduction(DepotSlot.BOTTOM,ProductionID.CENTER,ResourceBuilder.buildShield(2));
        game.currentPlayer().activateProductions();

        List<Resource> check = new ArrayList<>(Arrays.asList(ResourceBuilder.buildCoin(2), ResourceBuilder.buildStone(0), ResourceBuilder.buildShield(0), ResourceBuilder.buildServant(1)));

        assertEquals(check,game.currentPlayer().test_getPB().getWH_forTest().viewResourcesInDepot(DepotSlot.STRONGBOX));

        game.currentPlayer().endThisTurn();
        assertTrue(game.currentPlayer().canDoStuff());
        game.currentPlayer().test_getPB().getWH_forTest().insertInDepot(DepotSlot.BUFFER,ResourceBuilder.buildCoin());
        game.currentPlayer().test_getPB().addDevCard(DevCardSlot.CENTER, c4);
        game.currentPlayer().test_getPB().addProduction(prod4,DevCardSlot.CENTER);
        assertEquals(prod4, game.currentPlayer().test_getPB().possibleProduction().get(ProductionID.CENTER));
        game.currentPlayer().production();
        game.currentPlayer().moveInProduction(DepotSlot.BUFFER,ProductionID.CENTER,ResourceBuilder.buildCoin(15));
        game.currentPlayer().activateProductions();
        game.currentPlayer().production();
        game.currentPlayer().moveInProduction(DepotSlot.BUFFER,ProductionID.CENTER,ResourceBuilder.buildCoin());
        assertEquals(0,game.currentPlayer().test_getPB().faithTrack.getPlayerPosition());
        game.currentPlayer().production();
        game.currentPlayer().activateProductions();
        assertEquals(1,game.currentPlayer().test_getPB().faithTrack.getPlayerPosition());

        game.currentPlayer().endThisTurn();
        game.currentPlayer().production();
        assertTrue(game.currentPlayer().canDoStuff());
        game.currentPlayer().disconnect();
    }

    @Test
    public void Disconnections(){
        Player p = game.currentPlayer();
        game.currentPlayer().disconnect();
        assertFalse(p.canDoStuff());
        game.currentPlayer().useMarketTray(RowCol.ROW, 1);
        game.currentPlayer().endThisTurn();
        game.reconnectPlayer(p.getNickname());
        game.currentPlayer().useMarketTray(RowCol.ROW,1);
        assertTrue(game.currentPlayer().canDoStuff());
        game.currentPlayer().disconnect();
    }

    @Test
    public void Cheats(){
        game.currentPlayer().resourceCheat();
        for(Resource res : game.currentPlayer().personalBoard.warehouse.viewResourcesInDepot(DepotSlot.STRONGBOX)){
            assertEquals(50,res.amount());
        }
        game.currentPlayer().fpCheat(7);
        assertEquals(7,game.currentPlayer().personalBoard.faithTrack.getPlayerPosition());
    }

    @Test
    public void MainActionDone(){
        game.currentPlayer().useMarketTray(RowCol.ROW,0);
        List <Resource> res = game.currentPlayer().personalBoard.warehouse.viewResourcesInDepot(DepotSlot.BUFFER);
        if(!res.isEmpty()){
            game.currentPlayer().moveBetweenDepot(DepotSlot.BUFFER, DepotSlot.TOP, res.get(0).buildNewOne());
        }
        game.currentPlayer().resourceCheat();
        game.currentPlayer().activateLeaderCard(game.currentPlayer().personalBoard.viewLeaderCard().peekFirstCard().getCardID());
        game.currentPlayer().discardLeader(game.currentPlayer().personalBoard.viewLeaderCard().peekFirstCard().getCardID());
    }

    @Test
    public void EndGame(){
        Player p = game.currentPlayer();
        game.currentPlayer().fpCheat(25);
        assertTrue(game.isGameOnAir());
        if(game.currentPlayer().canDoStuff()){
            game.currentPlayer().useMarketTray(RowCol.ROW,1);
            game.currentPlayer().fpCheat(10);
            game.currentPlayer().resourceCheat();
            game.currentPlayer().endThisTurn();
        }
        assertFalse(game.isGameOnAir());
        assertFalse(p.canDoStuff());
        assertEquals(0, p.resourcesNumber());
        assertEquals(40, game.currentPlayer().resourcesNumber());
        assertEquals(29, p.calculateVictoryPoints());
        assertEquals(44, game.currentPlayer().calculateVictoryPoints());
        p.startHisTurn();
        p.disconnect();
        game.reconnectPlayer(p.getNickname());
    }

    @Test
    public void PlayerState(){
        final Player p = order.get(0).equals(game.currentPlayer()) ?
                order.get(1):
                order.get(0);
        assertFalse(p.canDoStuff());
        p.disconnect();
        game.reconnectPlayer(p.getNickname());
        p.useMarketTray(RowCol.ROW,1);
        assertEquals(HeaderTypes.INVALID, view.getTest());
        p.paintMarbleInTray(0,0);
        assertEquals(HeaderTypes.INVALID, view.getTest());
        p.buyCard();
        assertEquals(HeaderTypes.INVALID, view.getTest());
        p.buyDevCard(LevelDevCard.LEVEL1,ColorDevCard.BLUE,DevCardSlot.CENTER);
        assertEquals(HeaderTypes.INVALID, view.getTest());
        p.production();
        assertEquals(HeaderTypes.INVALID, view.getTest());
        p.activateProductions();
        assertEquals(HeaderTypes.INVALID, view.getTest());
        p.moveInProduction(DepotSlot.BUFFER,ProductionID.BASIC,ResourceBuilder.buildServant());
        assertEquals(HeaderTypes.INVALID, view.getTest());
        assertDoesNotThrow(() -> p.setNormalProduction(ProductionID.BASIC,new NormalProduction(Collections.singletonList(ResourceBuilder.buildShield()),Collections.singletonList(ResourceBuilder.buildCoin()))));
        assertEquals(HeaderTypes.INVALID, view.getTest());
        p.moveBetweenDepot(DepotSlot.BUFFER,DepotSlot.TOP,ResourceBuilder.buildServant());
        assertEquals(HeaderTypes.INVALID, view.getTest());
        p.activateLeaderCard("000");
        assertEquals(HeaderTypes.INVALID, view.getTest());
        p.discardLeader("000");
        assertEquals(HeaderTypes.INVALID, view.getTest());
        p.endThisTurn();
        assertEquals(HeaderTypes.INVALID, view.getTest());
        p.chooseResource(DepotSlot.TOP,ResourceType.COIN);
        assertEquals(HeaderTypes.INVALID, view.getTest());
        p.rollBack();
        assertEquals(HeaderTypes.INVALID, view.getTest());
    }
}