package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalNormalProduction;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
import it.polimi.ingsw.model.exceptions.productionException.UnknownUnspecifiedException;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.NormalProduction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.Production;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.UnknownProduction;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class ProductionTest {
    /**
     * create a production as normal production and test all the methods of the production class to check if it works correctly
     */
    @Test
    public void normalProductionCreation() throws UnknownUnspecifiedException {
        List<Resource> req = Arrays.asList(ResourceBuilder.buildCoin(2), ResourceBuilder.buildShield());
        List<Resource> out = Arrays.asList(ResourceBuilder.buildFaithPoint());

        Production production = null;
        try {
            production = new NormalProduction(req, out);
        } catch (IllegalTypeInProduction e) {
            e.printStackTrace();
            fail();
        }

        assertTrue(production.insertResource(ResourceBuilder.buildCoin()));
        assertFalse(production.activate());
        assertFalse(production.insertResource(ResourceBuilder.buildCoin(3)));

        assertNull(production.getOutput());

        assertArrayEquals(req.toArray(), production.getRequired().toArray());

        assertTrue(production.insertResource(ResourceBuilder.buildCoin()));
        assertTrue(production.insertResource(ResourceBuilder.buildShield()));
        assertFalse(production.insertResource(ResourceBuilder.buildServant()));

        assertNull(production.getOutput());
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
    public void unknownProductionCreation() throws UnknownUnspecifiedException {
        Production test = null;
        NormalProduction ok = null;
        NormalProduction illegal = null;

        try {
            test = new UnknownProduction(
                    Arrays.asList(ResourceBuilder.buildCoin(), ResourceBuilder.buildEmpty()),
                    Arrays.asList(ResourceBuilder.buildFaithPoint())
            );
            fail();
        } catch (IllegalTypeInProduction illegalTypeInProduction) {
            illegalTypeInProduction.printStackTrace();
        }

        try {
            test = new UnknownProduction(
                    Arrays.asList(ResourceBuilder.buildCoin(), ResourceBuilder.buildUnknown()),
                    Arrays.asList(ResourceBuilder.buildFaithPoint())
            );
        } catch (IllegalTypeInProduction illegalTypeInProduction) {
            illegalTypeInProduction.printStackTrace();
            fail();
        }

        try{
            test.insertResource(ResourceBuilder.buildShield());
            test.insertResource(ResourceBuilder.buildCoin());
            fail();
        } catch (UnknownUnspecifiedException e){
            assertTrue(true);
        }

        // normal production ok to be added
        try {
            ok = new NormalProduction(
                    Arrays.asList(ResourceBuilder.buildCoin(2)),
                    Arrays.asList(ResourceBuilder.buildFaithPoint())
            );
        } catch (IllegalTypeInProduction illegalTypeInProduction) {
            illegalTypeInProduction.printStackTrace();
            fail();
        }

        assertFalse(test.activate());

// ----- try new illegal production -----
        try {
            illegal = new NormalProduction(
                    Arrays.asList(ResourceBuilder.buildCoin(3)),
                    Arrays.asList(ResourceBuilder.buildFaithPoint())
            );
        } catch (IllegalTypeInProduction illegalTypeInProduction) {
            illegalTypeInProduction.printStackTrace();
            fail();
        }
        try {
            assertTrue(test.setNormalProduction(illegal));
            fail();
        } catch (IllegalNormalProduction illegalNormalProduction) {
            illegalNormalProduction.printStackTrace();
        }

// ----- try new illegal production -----
        try {
            illegal = new NormalProduction(
                    Arrays.asList(ResourceBuilder.buildCoin(), ResourceBuilder.buildShield(2)),
                    Arrays.asList(ResourceBuilder.buildFaithPoint())
            );
        } catch (IllegalTypeInProduction illegalTypeInProduction) {
            illegalTypeInProduction.printStackTrace();
            fail();
        }
        try {
            test.setNormalProduction(illegal);
            fail();
        } catch (IllegalNormalProduction illegalNormalProduction) {
            illegalNormalProduction.printStackTrace();
        }

// ----- try new illegal production -----
        try {
            illegal = new NormalProduction(
                    Arrays.asList(ResourceBuilder.buildCoin(2)),
                    Arrays.asList(ResourceBuilder.buildFaithPoint(2))
            );
        } catch (IllegalTypeInProduction illegalTypeInProduction) {
            illegalTypeInProduction.printStackTrace();
            fail();
        }
        try {
            test.setNormalProduction(illegal);
            fail();
        } catch (IllegalNormalProduction illegalNormalProduction) {
            illegalNormalProduction.printStackTrace();
        }

        try {
            assertTrue(test.setNormalProduction(ok));
        } catch (IllegalNormalProduction illegalNormalProduction) {
            illegalNormalProduction.printStackTrace();
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

}
