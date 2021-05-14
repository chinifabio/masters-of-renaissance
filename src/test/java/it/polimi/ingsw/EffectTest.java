package it.polimi.ingsw;

import it.polimi.ingsw.model.Dispatcher;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.cards.effects.*;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
import it.polimi.ingsw.model.match.match.Match;
import it.polimi.ingsw.model.match.match.SingleplayerMatch;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;
import it.polimi.ingsw.model.player.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.NormalProduction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.Production;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.UnknownProduction;
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

import static it.polimi.ingsw.model.resource.ResourceBuilder.buildStone;
import static org.junit.jupiter.api.Assertions.*;

/**
 * test collector for cards effects.
 */
public class EffectTest {
    private Player gino;

    Dispatcher view = new Dispatcher();
    private SingleplayerMatch singleplayer = new SingleplayerMatch(view);

    @BeforeEach
    public void initializeMatch() throws IllegalTypeInProduction {
        gino = new Player("gino", singleplayer, view);
        assertTrue(singleplayer.playerJoin(gino));

        //assertTrue(singleplayer.startGame());

        // the player discard the first two leader card
        assertDoesNotThrow(()->singleplayer.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()->singleplayer.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()->singleplayer.test_getCurrPlayer().endThisTurn());
        // once the second leader is discarded the turn end and match manage lorenzo automatically
    }

    @Test
    void addProduction() throws IllegalTypeInProduction {
        List<Requisite> req = new ArrayList<>();
        Resource coin = ResourceBuilder.buildCoin(2);
        ResourceRequisite rr = new ResourceRequisite(coin);
        req.add(rr);
        Production p = new NormalProduction(Collections.singletonList(buildStone()), Collections.singletonList(buildStone()));

        DevCard c = new DevCard("000", new AddProductionEffect(p), 2, LevelDevCard.LEVEL1, ColorDevCard.GREEN, req);

        Player player = new Player("dummy", null, new Dispatcher());

        PersonalBoard personalBoard = new PersonalBoard(player);

        assertDoesNotThrow(()->personalBoard.addDevCard(DevCardSlot.LEFT,c));
        player.test_setDest(DevCardSlot.LEFT);
        c.useEffect(player);

        assertEquals(c, personalBoard.viewDevCards().get(DevCardSlot.LEFT));
    }

    /**
     * This test activates a LeaderCard that creates a depot and check its ResourceType
     */
    @Test
    void addDepot() {
        List<Requisite> req = new ArrayList<>();
        Resource coin = ResourceBuilder.buildCoin(2);
        ResourceRequisite rr = new ResourceRequisite(coin);
        req.add(rr);

        DevCard c = new DevCard("000", new AddDepotEffect(ResourceType.COIN), 2, LevelDevCard.LEVEL1, ColorDevCard.GREEN, req);

        assertDoesNotThrow(()->{
            Player player = new Player("gino", null, new Dispatcher());

            c.useEffect(player);
            try {
                assertNotNull(player.test_getPB().viewDepotResource(DepotSlot.SPECIAL1).get(0));
            } catch (NullPointerException e) {
                fail();
            }
            player.test_getPB().insertInDepot(DepotSlot.BUFFER, ResourceBuilder.buildCoin());

            player.test_getPB().moveResourceDepot(DepotSlot.BUFFER, DepotSlot.SPECIAL1, ResourceBuilder.buildCoin());

            assertEquals(ResourceType.COIN, player.test_getPB().viewDepotResource(DepotSlot.SPECIAL1).get(0).type());
        });
    }

    /**
     * This test activates a LeaderCard that creates a new production and checks it
     */
    @Test
    void addExtraProductionEffect() {
        assertDoesNotThrow(()->{
            List<Requisite> req = new ArrayList<>();
            Resource coin = ResourceBuilder.buildCoin(2);
            ResourceRequisite rr = new ResourceRequisite(coin);
            req.add(rr);

            Production extraProd = new UnknownProduction(Collections.singletonList(ResourceBuilder.buildUnknown()), Arrays.asList(ResourceBuilder.buildUnknown(), ResourceBuilder.buildFaithPoint()));
            LeaderCard c = new LeaderCard("000", new AddExtraProductionEffect(extraProd), 1, req);

            Player player = new Player("gino", null, new Dispatcher());

            c.useEffect(player);
            assertEquals(extraProd, player.test_getPB().possibleProduction().get(ProductionID.LEADER1));
        });
    }

    @Test
    void destroyCardsEffect() {
        SoloActionToken token = new SoloActionToken("505", new DestroyCardsEffect(ColorDevCard.GREEN));

        DevCard oldOnTop = singleplayer.viewDevSetup().get(0);
        token.useEffect(singleplayer);
        assertNotEquals(oldOnTop, singleplayer.viewDevSetup().get(0));
    }

    @Test
    void moveTwoEffect() {
        SoloActionToken token = new SoloActionToken("505", new MoveTwoEffect());

        int x = singleplayer.test_getLorenzoPosition();
        token.useEffect(singleplayer);
        assertEquals(x+2, singleplayer.test_getLorenzoPosition());
    }

    //TODO adjust when buyDevCard will works
    @Test
    void addDiscountEffect(){
        List<Requisite> req = new ArrayList<>();
        Resource coin = ResourceBuilder.buildCoin(2);
        ResourceRequisite rr = new ResourceRequisite(coin);
        req.add(rr);

        LeaderCard c = new LeaderCard("000", new AddDiscountEffect(ResourceType.COIN), 1, req);
    }
}