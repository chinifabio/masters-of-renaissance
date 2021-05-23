package it.polimi.ingsw;

import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.model.Dispatcher;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.DevCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.cards.effects.AddDiscountEffect;
import it.polimi.ingsw.model.cards.effects.AddProductionEffect;
import it.polimi.ingsw.model.exceptions.card.AlreadyInDeckException;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;
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
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.NormalProduction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.Production;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.model.requisite.Requisite;
import it.polimi.ingsw.model.requisite.ResourceRequisite;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.BeforeEach;
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

    Dispatcher view = new Dispatcher();
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

        assertDoesNotThrow(()->player2 = new Player("pino", game, view));
        assertTrue(game.playerJoin(player2));
        order.add(player2);

        Collections.rotate(order, order.indexOf(game.currentPlayer()));

        assertTrue(game.isGameOnAir());

        assertDoesNotThrow(()-> order.get(0).test_discardLeader());
        assertDoesNotThrow(()-> order.get(0).test_discardLeader());
        assertEquals(HeaderTypes.END_TURN, order.get(0).endThisTurn().header);

        assertEquals(HeaderTypes.OK, order.get(1).chooseResource(DepotSlot.BOTTOM, ResourceType.COIN).header);
        assertDoesNotThrow(()-> order.get(1).test_discardLeader());
        assertDoesNotThrow(()-> order.get(1).test_discardLeader());
        assertDoesNotThrow(() -> assertEquals(order.get(1).test_getPB().getDepots().get(DepotSlot.BOTTOM).viewResources().get(0), ResourceBuilder.buildCoin()));
        assertEquals(HeaderTypes.END_TURN, order.get(1).endThisTurn().header);

    }

    @Test
    public void BuyDevCard() throws UnobtainableResourceException, WrongDepotException, EndGameException, LootTypeException {
        DevCard card1 = game.viewDevSetup().get(0);
        DevCard card2 = game.viewDevSetup().get(3);


        for(int i=0 ; i<card1.getCost().size() ; i++){
            Resource temp = ResourceBuilder.buildFromType(card1.getCost().get(i).getType(), card1.getCost().get(i).getAmount());
            assertTrue(game.currentPlayer().obtainResource(DepotSlot.BUFFER,temp));
        }

        assertTrue(game.currentPlayer().canDoStuff());
        game.currentPlayer().buyDevCard(LevelDevCard.LEVEL1, ColorDevCard.GREEN, DevCardSlot.LEFT);
        assertEquals(card1,game.currentPlayer().test_getPB().viewDevCards().get(DevCardSlot.LEFT));

        game.currentPlayer().endThisTurn();
        assertTrue(game.currentPlayer().canDoStuff());
        game.currentPlayer().buyDevCard(LevelDevCard.LEVEL3, ColorDevCard.BLUE, DevCardSlot.CENTER);

        for(int i=0 ; i<card2.getCost().size() ; i++){
            Resource temp = ResourceBuilder.buildFromType(card2.getCost().get(i).getType(), card2.getCost().get(i).getAmount());
            assertTrue(game.currentPlayer().obtainResource(DepotSlot.BUFFER,temp));
        }

        game.currentPlayer().moveBetweenDepot(DepotSlot.BUFFER,DepotSlot.TOP,ResourceBuilder.buildFromType(card2.getCost().get(0).getType(),1));
        game.currentPlayer().addDiscount(ResourceBuilder.buildFromType(card2.getCost().get(0).getType(),1).type());
        game.currentPlayer().buyDevCard(LevelDevCard.LEVEL1, ColorDevCard.YELLOW, DevCardSlot.RIGHT);
        assertEquals(card2,game.currentPlayer().test_getPB().viewDevCards().get(DevCardSlot.RIGHT));

    }

    @Test
    public void ActivateLeaderCard() throws EmptyDeckException {

        List<Requisite> req = new ArrayList<>();
        Resource coin = ResourceBuilder.buildCoin(2);
        ResourceRequisite rr = new ResourceRequisite(coin);
        req.add(rr);

        LeaderCard c1 = new LeaderCard("111", new AddDiscountEffect(ResourceType.COIN), 1, req);

        assertTrue(game.currentPlayer().canDoStuff());
        game.currentPlayer().test_discardLeader();
        game.currentPlayer().test_discardLeader();
        game.currentPlayer().endThisTurn();
        assertTrue(game.currentPlayer().canDoStuff());
        game.currentPlayer().addLeader(c1);
        game.currentPlayer().activateLeaderCard(c1.getCardID());
        assertTrue(game.currentPlayer().test_getLeader(0).isActivated());
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
        game.currentPlayer().moveInProduction(DepotSlot.BOTTOM,ProductionID.CENTER,ResourceBuilder.buildShield(2));
        game.currentPlayer().activateProductions();

        List<Resource> check = new ArrayList<>(Arrays.asList(ResourceBuilder.buildCoin(2), ResourceBuilder.buildStone(0), ResourceBuilder.buildShield(0), ResourceBuilder.buildServant(1)));

        assertEquals(check,game.currentPlayer().test_getPB().getWH_forTest().viewResourcesInDepot(DepotSlot.STRONGBOX));

        game.turnDone();
        assertTrue(game.currentPlayer().canDoStuff());
        game.currentPlayer().test_getPB().getWH_forTest().insertInDepot(DepotSlot.BUFFER,ResourceBuilder.buildCoin());
        game.currentPlayer().test_getPB().addDevCard(DevCardSlot.CENTER, c4);
        game.currentPlayer().test_getPB().addProduction(prod4,DevCardSlot.CENTER);
        assertEquals(prod4, game.currentPlayer().test_getPB().possibleProduction().get(ProductionID.CENTER));
        game.currentPlayer().moveInProduction(DepotSlot.BUFFER,ProductionID.CENTER,ResourceBuilder.buildCoin());
        assertEquals(0,game.currentPlayer().test_getPB().faithTrack.getPlayerPosition());
        game.currentPlayer().activateProductions();
        assertEquals(1,game.currentPlayer().test_getPB().faithTrack.getPlayerPosition());
    }
}