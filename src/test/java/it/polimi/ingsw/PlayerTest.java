package it.polimi.ingsw;

import static it.polimi.ingsw.model.resource.ResourceBuilder.buildStone;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.communication.packet.commands.SetNumberCommand;
import it.polimi.ingsw.communication.server.ClientController;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.DevCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.cards.effects.AddDiscountEffect;
import it.polimi.ingsw.model.cards.effects.AddProductionEffect;
import it.polimi.ingsw.model.exceptions.PlayerStateException;
import it.polimi.ingsw.model.exceptions.card.AlreadyInDeckException;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;
import it.polimi.ingsw.model.exceptions.card.MissingCardException;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.exceptions.requisite.LootTypeException;
import it.polimi.ingsw.model.exceptions.warehouse.NegativeResourcesDepotException;
import it.polimi.ingsw.model.exceptions.warehouse.UnobtainableResourceException;
import it.polimi.ingsw.model.exceptions.warehouse.WrongDepotException;
import it.polimi.ingsw.model.exceptions.warehouse.WrongPointsException;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
import it.polimi.ingsw.model.exceptions.warehouse.production.UnknownUnspecifiedException;
import it.polimi.ingsw.model.match.match.Match;
import it.polimi.ingsw.model.match.match.MultiplayerMatch;
import it.polimi.ingsw.model.match.match.SingleplayerMatch;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlayerTest {

    Model model = new Model();
    Match game;

    ClientController player1 = new ClientController(null, "lino");
    ClientController player2 = new ClientController(null, "gino");

    @BeforeEach
    public void initialization() {
        assertDoesNotThrow(()->model.start(player1));
        model.handleClientCommand(player1, new SetNumberCommand(2));
        assertTrue(model.connectController(player2));
        game = model.getMatch();

        assertDoesNotThrow(()-> game.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> game.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> game.test_getCurrPlayer().endThisTurn());

        assertDoesNotThrow(()-> game.test_getCurrPlayer().chooseResource(DepotSlot.BOTTOM, ResourceType.COIN));
        assertDoesNotThrow(()-> game.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> game.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(() -> assertEquals(game.test_getCurrPlayer().test_getPB().test_getDepots().get(DepotSlot.BOTTOM).viewResources().get(0), ResourceBuilder.buildCoin()));
        assertDoesNotThrow(()-> game.test_getCurrPlayer().endThisTurn());
    }

    @Test
    public void turnLifeCycle() { /*
        Player dummy = new Player("dummy", null);

        assertFalse(dummy.canDoStuff());

        dummy.startHisTurn();

        assertTrue(dummy.canDoStuff());

        try {
            dummy.doMainActionInput();
        } catch (IllegalMovesException e) {
            fail();
        }

        assertTrue(dummy.canDoStuff());

        try {
            dummy.endTurnInput();
        } catch (IllegalMovesException e) {
            fail();
        }

        assertFalse(dummy.canDoStuff());*/
    }

    /**
     *
     */
    @Test
    void buyDevCard() throws LootTypeException, WrongDepotException, UnobtainableResourceException, EndGameException {
        DevCard devCard1 = game.viewDevSetup().get(0);
        DevCard devCard2 = game.viewDevSetup().get(3);

        for(int i=0 ; i<devCard1.getCost().size() ; i++){
            Resource temp = ResourceBuilder.buildFromType(devCard1.getCost().get(i).getType(), devCard1.getCost().get(i).getAmount());
            assertTrue(game.test_getCurrPlayer().obtainResource(DepotSlot.BUFFER,temp));
        }

        assertTrue(game.test_getCurrPlayer().canDoStuff());
        game.test_getCurrPlayer().buyDevCard(LevelDevCard.LEVEL1, ColorDevCard.GREEN, DevCardSlot.LEFT);
        assertEquals(devCard1,game.test_getCurrPlayer().test_getPB().viewDevCards().get(DevCardSlot.LEFT));
        game.test_getCurrPlayer().endThisTurn();
        assertTrue(game.test_getCurrPlayer().canDoStuff());
        game.test_getCurrPlayer().buyDevCard(LevelDevCard.LEVEL2, ColorDevCard.YELLOW, DevCardSlot.CENTER);

        for(int i=0 ; i<devCard2.getCost().size() ; i++){
            Resource temp = ResourceBuilder.buildFromType(devCard2.getCost().get(i).getType(), devCard2.getCost().get(i).getAmount());
            assertTrue(game.test_getCurrPlayer().obtainResource(DepotSlot.BUFFER,temp));
        }

        game.test_getCurrPlayer().moveBetweenDepot(DepotSlot.BUFFER,DepotSlot.TOP,ResourceBuilder.buildFromType(devCard2.getCost().get(0).getType(),1));
        game.test_getCurrPlayer().addDiscount(ResourceBuilder.buildFromType(devCard2.getCost().get(0).getType(),1));
        game.test_getCurrPlayer().buyDevCard(LevelDevCard.LEVEL1, ColorDevCard.PURPLE, DevCardSlot.RIGHT);
        assertEquals(devCard2,game.test_getCurrPlayer().test_getPB().viewDevCards().get(DevCardSlot.RIGHT));
    }

    /**
     *
     */
    @Test
    void activateLeaderCard() throws EmptyDeckException {

        List<Requisite> req = new ArrayList<>();
        Resource coin = ResourceBuilder.buildCoin(2);
        ResourceRequisite rr = new ResourceRequisite(coin);
        req.add(rr);

        LeaderCard c1 = new LeaderCard("111", new AddDiscountEffect(ResourceType.COIN), 1, req);

        assertTrue(game.test_getCurrPlayer().canDoStuff());
        game.test_getCurrPlayer().test_discardLeader();
        game.test_getCurrPlayer().test_discardLeader();
        game.test_getCurrPlayer().endThisTurn();
        assertTrue(game.test_getCurrPlayer().canDoStuff());
        game.test_getCurrPlayer().addLeader(c1);
        game.test_getCurrPlayer().activateLeaderCard(c1.getCardID());
        assertTrue(game.test_getCurrPlayer().test_getLeader(0).isActivated());
    }


    @Test
    void Productions() throws IllegalTypeInProduction, EndGameException, AlreadyInDeckException, WrongDepotException, UnobtainableResourceException, NegativeResourcesDepotException {

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

        assertTrue(game.test_getCurrPlayer().canDoStuff());
        game.test_getCurrPlayer().test_getPB().addDevCard(DevCardSlot.RIGHT,c1);
        game.test_getCurrPlayer().test_getPB().addProduction(prod1,DevCardSlot.RIGHT);
        assertEquals(prod1,game.test_getCurrPlayer().test_getPB().possibleProduction().get(ProductionID.RIGHT));


        Production prod2 = new NormalProduction(Collections.singletonList(ResourceBuilder.buildShield(2)),resourceList);
        Production prod3 = new NormalProduction(Arrays.asList(ResourceBuilder.buildStone(),ResourceBuilder.buildCoin()),Collections.singletonList(ResourceBuilder.buildFaithPoint(3)));

        DevCard c2 = new DevCard("111", new AddProductionEffect(prod2), 6, LevelDevCard.LEVEL1, ColorDevCard.BLUE, req);
        DevCard c3 = new DevCard("222", new AddProductionEffect(prod3), 4, LevelDevCard.LEVEL2, ColorDevCard.GREEN, req);

        game.test_getCurrPlayer().test_getPB().addDevCard(DevCardSlot.CENTER,c2);
        game.test_getCurrPlayer().test_getPB().addDevCard(DevCardSlot.RIGHT,c3);
        game.test_getCurrPlayer().test_getPB().addProduction(prod2,DevCardSlot.CENTER);
        assertEquals(prod2, game.test_getCurrPlayer().test_getPB().possibleProduction().get(ProductionID.CENTER));

        game.test_getCurrPlayer().test_getPB().addProduction(prod3,DevCardSlot.RIGHT);
        assertEquals(prod3, game.test_getCurrPlayer().test_getPB().possibleProduction().get(ProductionID.RIGHT));
        game.test_getCurrPlayer().test_getPB().insertInDepot(DepotSlot.BUFFER,ResourceBuilder.buildShield(2));
        game.test_getCurrPlayer().test_getPB().moveResourceDepot(DepotSlot.BUFFER,DepotSlot.BOTTOM,ResourceBuilder.buildShield(2));
        game.test_getCurrPlayer().moveInProduction(DepotSlot.BOTTOM,ProductionID.CENTER,ResourceBuilder.buildShield(2));
        game.test_getCurrPlayer().activateProductions();
        game.test_getCurrPlayer().endThisTurn();
        game.test_getCurrPlayer().moveInProduction(DepotSlot.BOTTOM,ProductionID.CENTER,ResourceBuilder.buildShield(2));
        game.test_getCurrPlayer().endThisTurn();
        assertTrue(game.test_getCurrPlayer().canDoStuff());
        game.test_getCurrPlayer().activateProductions();
        List<Resource> check = new ArrayList<>(Arrays.asList(ResourceBuilder.buildCoin(2), ResourceBuilder.buildStone(0), ResourceBuilder.buildShield(0), ResourceBuilder.buildServant(1)));
        assertEquals(check,game.test_getCurrPlayer().test_getPB().viewDepotResource(DepotSlot.STRONGBOX));

    }
}