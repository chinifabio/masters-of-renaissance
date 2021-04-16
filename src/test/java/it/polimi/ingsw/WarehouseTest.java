package it.polimi.ingsw;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.exceptions.faithtrack.IllegalMovesException;
import it.polimi.ingsw.model.exceptions.warehouse.*;
import it.polimi.ingsw.model.exceptions.productionException.UnknownUnspecifiedException;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalNormalProduction;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerReactEffect;
import it.polimi.ingsw.model.player.personalBoard.warehouse.Warehouse;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotBuilder;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.*;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import org.junit.jupiter.api.Test;

import java.util.*;

public class WarehouseTest {
    /**
     * Testing if the resources are correctly inserted into the Depots with all the constraints
     */
    @Test
    public void insertResources() throws IllegalTypeInProduction, UnobtainableResourceException, WrongPointsException, IllegalMovesException {
        Player player = new Player("Dummy", null);
        Warehouse test = new Warehouse(player);

        //Testing the bottom depot
        assertTrue(test.insertInDepot(DepotSlot.BOTTOM,ResourceBuilder.buildCoin(2)));
        assertFalse(test.insertInDepot(DepotSlot.BOTTOM,ResourceBuilder.buildServant()));
        assertTrue(test.insertInDepot(DepotSlot.BOTTOM,ResourceBuilder.buildCoin()));
        assertFalse(test.insertInDepot(DepotSlot.BOTTOM,ResourceBuilder.buildCoin()));

        //Testing the middle depot
        assertFalse(test.insertInDepot(DepotSlot.MIDDLE,ResourceBuilder.buildCoin(2)));
        assertFalse(test.insertInDepot(DepotSlot.MIDDLE,ResourceBuilder.buildCoin()));
        assertTrue(test.insertInDepot(DepotSlot.MIDDLE,ResourceBuilder.buildServant()));

        //Testing the top depot
        assertFalse(test.insertInDepot(DepotSlot.TOP,ResourceBuilder.buildCoin(2)));
        assertFalse(test.insertInDepot(DepotSlot.TOP, ResourceBuilder.buildServant()));
        assertFalse(test.insertInDepot(DepotSlot.TOP, ResourceBuilder.buildCoin(2)));
        assertFalse(test.insertInDepot(DepotSlot.TOP, ResourceBuilder.buildStone(2)));
        assertTrue(test.insertInDepot(DepotSlot.TOP, ResourceBuilder.buildStone()));

    }

    /**
     * Testing the warehouse returns the resources into the depots correctly
     */
    @Test
    public void viewResources() throws IllegalTypeInProduction, UnobtainableResourceException, WrongPointsException, IllegalMovesException {
        Player player = new Player("Dummy", null);
        Warehouse test = new Warehouse(player);

        //Testing resources in BOTTOM Depot
        test.insertInDepot(DepotSlot.BOTTOM,ResourceBuilder.buildShield(2));
        assertEquals(ResourceBuilder.buildShield(2), test.viewResourcesInDepot(DepotSlot.BOTTOM));
        test.insertInDepot(DepotSlot.BOTTOM,ResourceBuilder.buildShield(2)); //Should not add
        assertEquals(ResourceBuilder.buildShield(2), test.viewResourcesInDepot(DepotSlot.BOTTOM));
        test.insertInDepot(DepotSlot.BOTTOM,ResourceBuilder.buildShield());
        assertEquals(ResourceBuilder.buildShield(3), test.viewResourcesInDepot(DepotSlot.BOTTOM));

        //Testing resources in MIDDLE Depot
        test.insertInDepot(DepotSlot.MIDDLE,ResourceBuilder.buildStone(2));
        assertEquals(ResourceBuilder.buildStone(2), test.viewResourcesInDepot(DepotSlot.MIDDLE));
        test.insertInDepot(DepotSlot.MIDDLE,ResourceBuilder.buildStone(2)); //Should not add
        assertEquals(ResourceBuilder.buildStone(2), test.viewResourcesInDepot(DepotSlot.MIDDLE));
        test.insertInDepot(DepotSlot.MIDDLE,ResourceBuilder.buildShield());//Should not add
        assertEquals(ResourceBuilder.buildStone(2), test.viewResourcesInDepot(DepotSlot.MIDDLE));

        //Testing resources in TOP Depot
        test.insertInDepot(DepotSlot.TOP,ResourceBuilder.buildShield(2));//Should not add
        assertEquals(ResourceBuilder.buildEmpty(), test.viewResourcesInDepot(DepotSlot.TOP));
        test.insertInDepot(DepotSlot.TOP,ResourceBuilder.buildCoin(1));
        assertEquals(ResourceBuilder.buildCoin(1), test.viewResourcesInDepot(DepotSlot.TOP));
        test.insertInDepot(DepotSlot.TOP,ResourceBuilder.buildCoin());//Should not add
        assertEquals(ResourceBuilder.buildCoin(1), test.viewResourcesInDepot(DepotSlot.TOP));


    }

    /**
     * Testing if the resources are correctly inserted into the warehouse
     */
    @Test
    public void insertInStrongbox() throws IllegalTypeInProduction, UnobtainableResourceException, WrongPointsException, IllegalMovesException {
        Player player = new Player("Dummy", null);
        Warehouse test = new Warehouse(player);
        List<Resource> list = new ArrayList<>();

        list.add(ResourceBuilder.buildCoin(2));
        list.add(ResourceBuilder.buildStone(3));
        list.add(ResourceBuilder.buildShield(0));
        list.add(ResourceBuilder.buildServant(3));



        test.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildCoin(2));
        test.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildServant(3));
        test.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildStone(3));
        assertArrayEquals(list.toArray(), test.viewResourcesInStrongbox().toArray());

        test.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildCoin(2));
        list.set(0,ResourceBuilder.buildCoin(4));
        assertArrayEquals(list.toArray(), test.viewResourcesInStrongbox().toArray());

        test.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildServant(3));
        test.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildStone(3));
        test.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildStone(3));
        list.set(3,ResourceBuilder.buildServant(6));
        list.set(1,ResourceBuilder.buildStone(9));
        assertArrayEquals(list.toArray(), test.viewResourcesInStrongbox().toArray());

        test.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildShield(4));
        test.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildShield(2));
        list.set(2,ResourceBuilder.buildShield(6));
        assertArrayEquals(list.toArray(), test.viewResourcesInStrongbox().toArray());
        test.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildFaithPoint(4));
        assertArrayEquals(list.toArray(), test.viewResourcesInStrongbox().toArray());

    }

    /**
     * Testing if the extra Depots are correctly created
     */
    @Test
    public void InsertInExtraDepots() throws ExtraDepotsException, IllegalTypeInProduction, UnobtainableResourceException, WrongPointsException, IllegalMovesException {
        Player player = new Player("Dummy", null);
        Warehouse test = new Warehouse(player);
        boolean exc = false;

        //Add depot SPECIAL1
        test.addDepot(DepotBuilder.buildSpecialDepot(ResourceBuilder.buildShield()));
        test.insertInDepot(DepotSlot.SPECIAL1, ResourceBuilder.buildShield());
        assertEquals(ResourceBuilder.buildShield(1),test.viewResourcesInDepot(DepotSlot.SPECIAL1));
        test.insertInDepot(DepotSlot.SPECIAL1, ResourceBuilder.buildShield(3));
        test.insertInDepot(DepotSlot.SPECIAL1, ResourceBuilder.buildCoin());
        assertEquals(ResourceBuilder.buildShield(1),test.viewResourcesInDepot(DepotSlot.SPECIAL1));
        test.insertInDepot(DepotSlot.SPECIAL1, ResourceBuilder.buildShield());
        assertEquals(ResourceBuilder.buildShield(2),test.viewResourcesInDepot(DepotSlot.SPECIAL1));
        //Add depot SPECIAL2
        assertFalse(test.addDepot(DepotBuilder.buildSpecialDepot(ResourceBuilder.buildShield())));
        test.addDepot(DepotBuilder.buildSpecialDepot(ResourceBuilder.buildCoin()));
        assertEquals(ResourceBuilder.buildCoin(0),test.viewResourcesInDepot(DepotSlot.SPECIAL2));
        test.insertInDepot(DepotSlot.SPECIAL2, ResourceBuilder.buildStone());
        assertEquals(ResourceBuilder.buildCoin(0),test.viewResourcesInDepot(DepotSlot.SPECIAL2));
        test.insertInDepot(DepotSlot.SPECIAL2, ResourceBuilder.buildCoin());
        assertEquals(ResourceBuilder.buildCoin(),test.viewResourcesInDepot(DepotSlot.SPECIAL2));
        test.insertInDepot(DepotSlot.SPECIAL2, ResourceBuilder.buildCoin());
        test.insertInDepot(DepotSlot.SPECIAL2, ResourceBuilder.buildCoin());
        assertEquals(ResourceBuilder.buildCoin(2),test.viewResourcesInDepot(DepotSlot.SPECIAL2));
        try{
            test.addDepot(DepotBuilder.buildSpecialDepot(ResourceBuilder.buildStone()));
        } catch (ExtraDepotsException e){
            exc = true;
        }
        assertTrue(exc);
    }


    @Test
    public void moveResourcesInDepots() throws NegativeResourcesDepotException, WrongDepotException, ExtraDepotsException, IllegalTypeInProduction, UnobtainableResourceException, WrongPointsException, IllegalMovesException {
        Player player = new Player("Dummy", null);
        Warehouse warehouse = new Warehouse(player);
        warehouse.addDepot(DepotBuilder.buildSpecialDepot(ResourceBuilder.buildStone()));
        warehouse.addDepot(DepotBuilder.buildSpecialDepot(ResourceBuilder.buildShield()));


        warehouse.insertInDepot(DepotSlot.BOTTOM, ResourceBuilder.buildStone(2));
        warehouse.insertInDepot(DepotSlot.MIDDLE, ResourceBuilder.buildCoin());
        warehouse.insertInDepot(DepotSlot.SPECIAL1, ResourceBuilder.buildStone(1));

        warehouse.moveBetweenDepot(DepotSlot.SPECIAL1,DepotSlot.BOTTOM,ResourceBuilder.buildStone());
        assertEquals(warehouse.viewResourcesInDepot(DepotSlot.BOTTOM),ResourceBuilder.buildStone(3));
        assertEquals(warehouse.viewResourcesInDepot(DepotSlot.SPECIAL1),ResourceBuilder.buildStone(0));

        warehouse.insertInDepot(DepotSlot.TOP,ResourceBuilder.buildShield());
        assertEquals(warehouse.viewResourcesInDepot(DepotSlot.TOP),ResourceBuilder.buildShield(1));

        warehouse.moveBetweenDepot(DepotSlot.TOP,DepotSlot.SPECIAL2,ResourceBuilder.buildShield());
        assertEquals(warehouse.viewResourcesInDepot(DepotSlot.TOP),ResourceBuilder.buildEmpty());
        assertEquals(warehouse.viewResourcesInDepot(DepotSlot.SPECIAL2),ResourceBuilder.buildShield(1));

        warehouse.moveBetweenDepot(DepotSlot.BOTTOM,DepotSlot.SPECIAL1,ResourceBuilder.buildStone(2));
        assertEquals(warehouse.viewResourcesInDepot(DepotSlot.BOTTOM),ResourceBuilder.buildStone(1));
        assertEquals(warehouse.viewResourcesInDepot(DepotSlot.SPECIAL1),ResourceBuilder.buildStone(2));

        warehouse.moveBetweenDepot(DepotSlot.MIDDLE,DepotSlot.TOP,ResourceBuilder.buildCoin());
        warehouse.moveBetweenDepot(DepotSlot.SPECIAL2,DepotSlot.MIDDLE,ResourceBuilder.buildShield());
        warehouse.removeFromDepot(DepotSlot.BOTTOM,ResourceBuilder.buildStone(1));
        assertEquals(warehouse.viewResourcesInDepot(DepotSlot.TOP),ResourceBuilder.buildCoin(1));
        assertEquals(warehouse.viewResourcesInDepot(DepotSlot.MIDDLE),ResourceBuilder.buildShield(1));
        assertEquals(warehouse.viewResourcesInDepot(DepotSlot.BOTTOM),ResourceBuilder.buildEmpty());
        assertEquals(warehouse.viewResourcesInDepot(DepotSlot.SPECIAL2),ResourceBuilder.buildShield(0));

        warehouse.moveBetweenDepot(DepotSlot.TOP,DepotSlot.BOTTOM,ResourceBuilder.buildCoin());
        assertEquals(warehouse.viewResourcesInDepot(DepotSlot.TOP),ResourceBuilder.buildEmpty());
        assertEquals(warehouse.viewResourcesInDepot(DepotSlot.BOTTOM),ResourceBuilder.buildCoin(1));

        warehouse.insertInDepot(DepotSlot.TOP,ResourceBuilder.buildCoin(1)); //Do nothing
        assertEquals(warehouse.viewResourcesInDepot(DepotSlot.TOP),ResourceBuilder.buildEmpty());
        assertEquals(warehouse.viewResourcesInDepot(DepotSlot.BOTTOM),ResourceBuilder.buildCoin(1));

        warehouse.insertInDepot(DepotSlot.SPECIAL2,ResourceBuilder.buildShield(2));
        warehouse.moveBetweenDepot(DepotSlot.SPECIAL2,DepotSlot.MIDDLE,ResourceBuilder.buildShield(1));
        warehouse.moveBetweenDepot(DepotSlot.SPECIAL2,DepotSlot.TOP,ResourceBuilder.buildShield(1)); //Do nothing

        assertEquals(warehouse.viewResourcesInDepot(DepotSlot.SPECIAL2),ResourceBuilder.buildShield(1));
        assertEquals(warehouse.viewResourcesInDepot(DepotSlot.MIDDLE),ResourceBuilder.buildShield(2));
        assertEquals(warehouse.viewResourcesInDepot(DepotSlot.TOP),ResourceBuilder.buildEmpty());

        assertFalse(warehouse.moveBetweenDepot(DepotSlot.SPECIAL2,DepotSlot.SPECIAL1,ResourceBuilder.buildShield(1)));

        warehouse.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildServant(1));
        assertFalse(warehouse.moveBetweenDepot(DepotSlot.STRONGBOX,DepotSlot.TOP,ResourceBuilder.buildServant(1)));
    }

    @Test
    public void activateProductions() throws IllegalTypeInProduction, UnobtainableResourceException, NegativeResourcesDepotException, UnknownUnspecifiedException, WrongPointsException, IllegalMovesException {

        PlayerReactEffect player = new Player("Dummy", null);
        Warehouse warehouse = new Warehouse(player);
        List<Resource> req = Arrays.asList(ResourceBuilder.buildCoin(2), ResourceBuilder.buildShield());
        List<Resource> out = Arrays.asList(ResourceBuilder.buildStone(10));

        List<Resource> req2 = Arrays.asList(ResourceBuilder.buildShield(2), ResourceBuilder.buildStone(2));
        List<Resource> out2 = Arrays.asList(ResourceBuilder.buildServant(10));

        warehouse.insertInDepot(DepotSlot.BOTTOM, ResourceBuilder.buildCoin(3));
        warehouse.insertInDepot(DepotSlot.MIDDLE, ResourceBuilder.buildShield(2));
        warehouse.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildCoin(20));
        warehouse.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildShield(20));
        warehouse.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildStone(20));

        Production prod1 = new NormalProduction(req, out);
        Production prod2 = new NormalProduction(req2, out2);
        Production prod3 = new NormalProduction(req, out2); //Do nothing

        warehouse.addProduction(ProductionID.LEFT, prod1);
        warehouse.addProduction(ProductionID.CENTER, prod2);

        assertEquals(prod1, warehouse.getProduction().get(ProductionID.LEFT));
        assertEquals(prod2, warehouse.getProduction().get(ProductionID.CENTER));

        //Moving the resources to activate the first production (prod1)
        warehouse.moveInProduction(DepotSlot.BOTTOM,ProductionID.LEFT,ResourceBuilder.buildCoin(2));
        warehouse.moveInProduction(DepotSlot.MIDDLE, ProductionID.LEFT, ResourceBuilder.buildShield());

        assertTrue(warehouse.getProduction().get(ProductionID.LEFT).viewResourcesAdded().contains(ResourceBuilder.buildCoin(2)));
        assertTrue(warehouse.getProduction().get(ProductionID.LEFT).viewResourcesAdded().contains(ResourceBuilder.buildShield()));

        //Moving the resources to activate the second production (prod2)
        warehouse.moveInProduction(DepotSlot.STRONGBOX,ProductionID.CENTER,ResourceBuilder.buildShield(2));
        warehouse.moveInProduction(DepotSlot.STRONGBOX, ProductionID.CENTER, ResourceBuilder.buildStone(2));

        assertTrue(warehouse.getProduction().get(ProductionID.CENTER).viewResourcesAdded().contains(ResourceBuilder.buildShield(2)));
        assertTrue(warehouse.getProduction().get(ProductionID.CENTER).viewResourcesAdded().contains(ResourceBuilder.buildStone(2)));


        //Activating the productions
        warehouse.activateProductions();

        assertTrue(warehouse.viewResourcesInStrongbox().contains(ResourceBuilder.buildStone(28)));
        assertTrue(warehouse.viewResourcesInStrongbox().contains(ResourceBuilder.buildServant(10)));


    }

    @Test
    public void notEnoughResourceProduction() throws IllegalTypeInProduction, UnobtainableResourceException, NegativeResourcesDepotException, UnknownUnspecifiedException, IllegalNormalProduction, IllegalTypeInProduction, IllegalNormalProduction, UnobtainableResourceException, WrongPointsException, IllegalMovesException {

        PlayerReactEffect player = new Player("Dummy", null);
        Warehouse warehouse = new Warehouse(player);
        List<Resource> req = Arrays.asList(ResourceBuilder.buildCoin(2), ResourceBuilder.buildUnknown());
        List<Resource> out = Arrays.asList(ResourceBuilder.buildStone(10));

        List<Resource> req2 = Arrays.asList(ResourceBuilder.buildShield(2), ResourceBuilder.buildStone(2));
        List<Resource> out2 = Arrays.asList(ResourceBuilder.buildServant(10));

        warehouse.insertInDepot(DepotSlot.BOTTOM, ResourceBuilder.buildCoin(3));
        warehouse.insertInDepot(DepotSlot.MIDDLE, ResourceBuilder.buildShield(2));
        warehouse.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildCoin(20));
        warehouse.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildShield(20));
        warehouse.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildStone(20));


        //Testing also the unknownProduction
        Production prod1 = new UnknownProduction(req, out);
        Production prod2 = new NormalProduction(req2, out2);


        warehouse.addProduction(ProductionID.LEFT, prod1);

        warehouse.setNormalProduction(ProductionID.LEFT, new NormalProduction(
                Arrays.asList(ResourceBuilder.buildCoin(2), ResourceBuilder.buildShield()),
                Arrays.asList(ResourceBuilder.buildStone(10))
        ));


        warehouse.addProduction(ProductionID.CENTER, prod2);

        assertEquals(prod1, warehouse.getProduction().get(ProductionID.LEFT));
        assertEquals(prod2, warehouse.getProduction().get(ProductionID.CENTER));

        //Moving not enough resources to activate the first production (prod1)
        warehouse.moveInProduction(DepotSlot.BOTTOM,ProductionID.LEFT,ResourceBuilder.buildCoin(1));
        warehouse.moveInProduction(DepotSlot.MIDDLE, ProductionID.LEFT, ResourceBuilder.buildShield());

        //System.out.println(warehouse.getProduction().get(ProductionID.LEFT).viewResourcesAdded());
        assertTrue(warehouse.getProduction().get(ProductionID.LEFT).viewResourcesAdded().contains(ResourceBuilder.buildCoin(1)));
        assertTrue(warehouse.getProduction().get(ProductionID.LEFT).viewResourcesAdded().contains(ResourceBuilder.buildShield()));

        //Moving enough resources to activate the second production (prod2)
        warehouse.moveInProduction(DepotSlot.STRONGBOX,ProductionID.CENTER,ResourceBuilder.buildShield(2));
        warehouse.moveInProduction(DepotSlot.STRONGBOX, ProductionID.CENTER, ResourceBuilder.buildStone(2));

        assertTrue(warehouse.getProduction().get(ProductionID.CENTER).viewResourcesAdded().contains(ResourceBuilder.buildShield(2)));
        assertTrue(warehouse.getProduction().get(ProductionID.CENTER).viewResourcesAdded().contains(ResourceBuilder.buildStone(2)));

        System.out.println(warehouse.viewResourcesInStrongbox());
        System.out.println(warehouse.viewResourcesInDepot(DepotSlot.BOTTOM));
        System.out.println(warehouse.viewResourcesInDepot(DepotSlot.MIDDLE));
        System.out.println(warehouse.viewResourcesInDepot(DepotSlot.TOP));

        //Activating the productions
        warehouse.activateProductions();

        System.out.println(warehouse.viewResourcesInStrongbox());
        System.out.println(warehouse.viewResourcesInDepot(DepotSlot.BOTTOM));
        System.out.println(warehouse.viewResourcesInDepot(DepotSlot.MIDDLE));
        System.out.println(warehouse.viewResourcesInDepot(DepotSlot.TOP));

    }
}
