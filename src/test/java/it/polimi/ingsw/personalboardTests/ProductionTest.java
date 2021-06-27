package it.polimi.ingsw.personalboardTests;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.VirtualView;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalNormalProduction;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
import it.polimi.ingsw.model.exceptions.warehouse.production.UnknownUnspecifiedException;
import it.polimi.ingsw.model.match.match.Match;
import it.polimi.ingsw.model.match.match.SingleplayerMatch;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.NormalProduction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.Production;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.UnknownProduction;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProductionTest {
    /**
     * create a production as normal production and test all the methods of the production class to check if it works correctly
     */
    @Test
    public void normalProductionCreation() {
        List<Resource> req = Arrays.asList(ResourceBuilder.buildCoin(2), ResourceBuilder.buildShield());
        List<Resource> out = Collections.singletonList(ResourceBuilder.buildFaithPoint());

        Production production = null;
        try {
            production = new NormalProduction(req, out);
        } catch (IllegalTypeInProduction e) {
            fail(e.getMessage());
        }

        assertTrue(production.insertResource(ResourceBuilder.buildCoin()));
        assertFalse(production.activate());
        assertFalse(production.insertResource(ResourceBuilder.buildCoin(3)));

        assertArrayEquals(req.toArray(), production.getRequired().toArray());

        assertTrue(production.insertResource(ResourceBuilder.buildCoin()));
        assertTrue(production.insertResource(ResourceBuilder.buildShield()));
        assertFalse(production.insertResource(ResourceBuilder.buildServant()));

        assertArrayEquals(req.toArray(), production.getRequired().toArray());

        assertTrue(production.activate());
        assertFalse(production.insertResource(ResourceBuilder.buildShield()));
        assertFalse(production.activate());

        assertArrayEquals(out.toArray(), production.getOutput().toArray());
        assertFalse(production.insertResource(ResourceBuilder.buildShield()));
        assertTrue(production.reset());
    }

    /**
     * create a production as normal production and test all the methods of the production class to check if it works correctly
     */
    @Test
    public void unknownProductionCreation() {
        Production test = null;
        NormalProduction ok = null;
        NormalProduction illegal = null;

        try {
            test = new UnknownProduction(
                    Arrays.asList(ResourceBuilder.buildCoin(), ResourceBuilder.buildEmpty()),
                    Collections.singletonList(ResourceBuilder.buildFaithPoint())
            );
            fail();
        } catch (IllegalTypeInProduction ignore) {}

        try {
            test = new UnknownProduction(
                    Arrays.asList(ResourceBuilder.buildCoin(), ResourceBuilder.buildUnknown()),
                    Collections.singletonList(ResourceBuilder.buildFaithPoint())
            );
        } catch (IllegalTypeInProduction ignore) {}


        assert test != null;
        assertFalse(test.insertResource(ResourceBuilder.buildShield()));
        assertFalse(test.insertResource(ResourceBuilder.buildCoin()));


        // normal production ok to be added
        try {
            ok = new NormalProduction(
                    Collections.singletonList(ResourceBuilder.buildCoin(2)),
                    Collections.singletonList(ResourceBuilder.buildFaithPoint())
            );
        } catch (IllegalTypeInProduction illegalTypeInProduction) {
            fail(illegalTypeInProduction.getMessage());
        }

        assertFalse(test.activate());

// ----- try new illegal production -----
        try {
            illegal = new NormalProduction(
                    Collections.singletonList(ResourceBuilder.buildCoin(3)),
                    Collections.singletonList(ResourceBuilder.buildFaithPoint())
            );
        } catch (IllegalTypeInProduction illegalTypeInProduction) {
            fail(illegalTypeInProduction.getMessage());
        }
        try {
            assertTrue(test.setNormalProduction(illegal));
            fail();
        } catch (IllegalNormalProduction ignore) {}

// ----- try new illegal production -----
        try {
            illegal = new NormalProduction(
                    Arrays.asList(ResourceBuilder.buildCoin(), ResourceBuilder.buildShield(2)),
                    Collections.singletonList(ResourceBuilder.buildFaithPoint())
            );
        } catch (IllegalTypeInProduction illegalTypeInProduction) {
            fail(illegalTypeInProduction.getMessage());
        }
        try {
            test.setNormalProduction(illegal);
            fail();
        } catch (IllegalNormalProduction ignore) {}

// ----- try new illegal production -----
        try {
            illegal = new NormalProduction(
                    Collections.singletonList(ResourceBuilder.buildCoin(2)),
                    Collections.singletonList(ResourceBuilder.buildFaithPoint(2))
            );
        } catch (IllegalTypeInProduction illegalTypeInProduction) {
            fail();
        }
        try {
            test.setNormalProduction(illegal);
            fail();
        } catch (IllegalNormalProduction ignore) {}

        try {
            assertTrue(test.setNormalProduction(ok));
        } catch (IllegalNormalProduction illegalNormalProduction) {
            fail();
        }

        assertFalse(test.insertResource(ResourceBuilder.buildCoin(3)));
        assertFalse(test.insertResource(ResourceBuilder.buildShield()));
        assertFalse(test.activate());

        assertArrayEquals(new Resource[]{ResourceBuilder.buildCoin(2)}, test.getRequired().toArray());
        assertTrue(test.insertResource(ResourceBuilder.buildCoin(2)));

        assertFalse(test.insertResource(ResourceBuilder.buildShield()));

        assertTrue(test.activate());
        assertFalse(test.activate());

        assertArrayEquals(new Resource[]{ResourceBuilder.buildFaithPoint()}, test.getOutput().toArray());
    }

    @Test
    public void activateUnknownProduction() {
        assertDoesNotThrow(()->{
            VirtualView view = new VirtualView();
            Match match = new SingleplayerMatch(view);

            Player player = new Player("Dummy", match, view);

            match.playerJoin(player);
            match.initialize();

            player.test_discardLeader();
            player.test_discardLeader();
            player.endThisTurn();

            player.production();

            // normalizing the basic production
            player.setNormalProduction(ProductionID.BASIC, new NormalProduction(
                    Collections.singletonList(ResourceBuilder.buildCoin(2)),
                    Collections.singletonList(ResourceBuilder.buildServant())
            ));
            // cheating and getting resources
            player.obtainResource(DepotSlot.MIDDLE, ResourceBuilder.buildCoin(2));

            // adding resource to the basic production
            player.moveInProduction(DepotSlot.MIDDLE, ProductionID.BASIC, ResourceBuilder.buildCoin(2));

            // activating the production
            player.activateProductions();

            // checking he has stone in the strongbox
            assertTrue(player.test_getPB().getDepots().get(DepotSlot.STRONGBOX).viewResources().contains(ResourceBuilder.buildServant()));

        });
    }

}
