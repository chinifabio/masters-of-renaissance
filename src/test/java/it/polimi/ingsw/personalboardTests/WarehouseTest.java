package it.polimi.ingsw.personalboardTests;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.CustomAssertion;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.exceptions.warehouse.*;
import it.polimi.ingsw.model.exceptions.warehouse.production.UnknownUnspecifiedException;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalNormalProduction;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.Warehouse;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.Depot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotBuilder;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.SpecialDepot;
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
    public void insertResources() throws IllegalTypeInProduction{
        Warehouse test = new Warehouse();

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
    public void viewResources() throws IllegalTypeInProduction{
        Warehouse test = new Warehouse();

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
    public void insertInStrongbox() throws IllegalTypeInProduction{
        Warehouse test = new Warehouse();
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
    public void InsertInExtraDepots() throws ExtraDepotsException, IllegalTypeInProduction{
        Warehouse test = new Warehouse();
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
    public void moveResourcesInDepots() throws NegativeResourcesDepotException, WrongDepotException, ExtraDepotsException, IllegalTypeInProduction, UnobtainableResourceException {
        Warehouse warehouse = new Warehouse();
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

        try{
                warehouse.moveBetweenDepot(DepotSlot.MIDDLE, DepotSlot.BOTTOM, ResourceBuilder.buildServant());
        } catch (WrongDepotException ignore){}
    }

    @Test
    public void activateProductions() throws IllegalTypeInProduction, UnobtainableResourceException, NegativeResourcesDepotException, UnknownUnspecifiedException, EndGameException {
        Warehouse warehouse = new Warehouse();
        List<Resource> req = Arrays.asList(ResourceBuilder.buildCoin(2), ResourceBuilder.buildShield());
        List<Resource> out = Collections.singletonList(ResourceBuilder.buildStone(10));

        List<Resource> req2 = Arrays.asList(ResourceBuilder.buildShield(2), ResourceBuilder.buildStone(2));
        List<Resource> out2 = Collections.singletonList(ResourceBuilder.buildServant(10));

        warehouse.insertInDepot(DepotSlot.BOTTOM, ResourceBuilder.buildCoin(3));
        warehouse.insertInDepot(DepotSlot.MIDDLE, ResourceBuilder.buildShield(2));
        warehouse.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildCoin(20));
        warehouse.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildShield(20));
        warehouse.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildStone(20));

        Production prod1 = new NormalProduction(req, out);
        Production prod2 = new NormalProduction(req2, out2);

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
    public void notEnoughResourceProduction() throws  NegativeResourcesDepotException, UnknownUnspecifiedException, IllegalTypeInProduction, IllegalNormalProduction, UnobtainableResourceException, EndGameException {
        Warehouse warehouse = new Warehouse();
        List<Resource> req = Arrays.asList(ResourceBuilder.buildCoin(2), ResourceBuilder.buildUnknown());
        List<Resource> out = Collections.singletonList(ResourceBuilder.buildStone(10));

        List<Resource> req2 = Arrays.asList(ResourceBuilder.buildShield(2), ResourceBuilder.buildStone(2));
        List<Resource> out2 = Collections.singletonList(ResourceBuilder.buildServant(10));

        warehouse.insertInDepot(DepotSlot.BOTTOM, ResourceBuilder.buildCoin(3));
        warehouse.insertInDepot(DepotSlot.MIDDLE, ResourceBuilder.buildShield(2));
        warehouse.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildCoin(20));
        warehouse.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildShield(20));
        warehouse.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildStone(20));


        //Testing also the unknownProduction
        Production prod1 = new UnknownProduction(req, out);
        Production prod2 = new NormalProduction(req2, out2);


        warehouse.addProduction(ProductionID.LEFT, prod1);

        assertTrue(warehouse.setNormalProduction(ProductionID.LEFT, new NormalProduction(
                Arrays.asList(ResourceBuilder.buildCoin(2), ResourceBuilder.buildShield()),
                Collections.singletonList(ResourceBuilder.buildStone(10))
        )));


        warehouse.addProduction(ProductionID.CENTER, prod2);

        assertEquals(prod1, warehouse.getProduction().get(ProductionID.LEFT));
        assertEquals(prod2, warehouse.getProduction().get(ProductionID.CENTER));

        //Moving not enough resources to activate the first production (prod1)
        warehouse.moveInProduction(DepotSlot.BOTTOM,ProductionID.LEFT,ResourceBuilder.buildCoin(1));
        warehouse.moveInProduction(DepotSlot.MIDDLE, ProductionID.LEFT, ResourceBuilder.buildShield());

        assertTrue(warehouse.getProduction().get(ProductionID.LEFT).viewResourcesAdded().contains(ResourceBuilder.buildCoin(1)));
        assertTrue(warehouse.getProduction().get(ProductionID.LEFT).viewResourcesAdded().contains(ResourceBuilder.buildShield()));

        //Moving enough resources to activate the second production (prod2)
        warehouse.moveInProduction(DepotSlot.STRONGBOX,ProductionID.CENTER,ResourceBuilder.buildShield(2));
        warehouse.moveInProduction(DepotSlot.STRONGBOX, ProductionID.CENTER, ResourceBuilder.buildStone(2));

        assertTrue(warehouse.getProduction().get(ProductionID.CENTER).viewResourcesAdded().contains(ResourceBuilder.buildShield(2)));
        assertTrue(warehouse.getProduction().get(ProductionID.CENTER).viewResourcesAdded().contains(ResourceBuilder.buildStone(2)));


        assertEquals(ResourceBuilder.buildCoin(2),warehouse.viewResourcesInDepot(DepotSlot.BOTTOM));
        assertEquals(ResourceBuilder.buildShield(),warehouse.viewResourcesInDepot(DepotSlot.MIDDLE));
        assertEquals(ResourceBuilder.buildEmpty(),warehouse.viewResourcesInDepot(DepotSlot.TOP));
        assertTrue(warehouse.viewResourcesInStrongbox().contains(ResourceBuilder.buildCoin(20)));
        assertTrue(warehouse.viewResourcesInStrongbox().contains(ResourceBuilder.buildShield(18)));
        assertTrue(warehouse.viewResourcesInStrongbox().contains(ResourceBuilder.buildStone(18)));
        assertTrue(warehouse.viewResourcesInStrongbox().contains(ResourceBuilder.buildServant(0)));


        //Activating the productions
        warehouse.activateProductions();
        //This production will failed so the resources will return to their original position


        assertEquals(ResourceBuilder.buildCoin(3),warehouse.viewResourcesInDepot(DepotSlot.BOTTOM));
        assertEquals(ResourceBuilder.buildShield(2),warehouse.viewResourcesInDepot(DepotSlot.MIDDLE));
        assertEquals(ResourceBuilder.buildEmpty(),warehouse.viewResourcesInDepot(DepotSlot.TOP));

        //The strongbox will not receive the resources of the productions
        assertFalse(warehouse.viewResourcesInStrongbox().contains(ResourceBuilder.buildStone(28)));
        assertFalse(warehouse.viewResourcesInStrongbox().contains(ResourceBuilder.buildServant(10)));

        //The strongbox will receive only the resources taken to use the production
        assertTrue(warehouse.viewResourcesInStrongbox().contains(ResourceBuilder.buildCoin(20)));
        assertTrue(warehouse.viewResourcesInStrongbox().contains(ResourceBuilder.buildShield(20)));
        assertTrue(warehouse.viewResourcesInStrongbox().contains(ResourceBuilder.buildStone(20)));
        assertTrue(warehouse.viewResourcesInStrongbox().contains(ResourceBuilder.buildServant(0)));

    }

    @Test
    public void countTotalResources() throws IllegalTypeInProduction, ExtraDepotsException {
        Warehouse warehouse = new Warehouse();

        warehouse.insertInDepot(DepotSlot.BOTTOM, ResourceBuilder.buildShield(2));
        warehouse.insertInDepot(DepotSlot.MIDDLE, ResourceBuilder.buildCoin(2));
        warehouse.insertInDepot(DepotSlot.STRONGBOX, ResourceBuilder.buildStone(5));

        assertEquals(1, warehouse.countPointsWarehouse());

        warehouse.insertInDepot(DepotSlot.STRONGBOX, ResourceBuilder.buildShield(10));
        assertEquals(3, warehouse.countPointsWarehouse());

        warehouse.addDepot(DepotBuilder.buildSpecialDepot(ResourceBuilder.buildServant()));
        warehouse.insertInDepot(DepotSlot.SPECIAL1, ResourceBuilder.buildServant(2));

        assertEquals(4, warehouse.countPointsWarehouse());

        warehouse.insertInDepot(DepotSlot.TOP, ResourceBuilder.buildServant());
        assertEquals(4, warehouse.countPointsWarehouse());

        warehouse.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildServant(20));
        assertEquals(8, warehouse.countPointsWarehouse());

    }

    @Test
    public void usingBuffer() throws IllegalTypeInProduction, UnobtainableResourceException, WrongDepotException, NegativeResourcesDepotException, ExtraDepotsException {
        Warehouse warehouse = new Warehouse();

        warehouse.insertInDepot(DepotSlot.BOTTOM, ResourceBuilder.buildServant(3));
        warehouse.moveBetweenDepot(DepotSlot.BOTTOM, DepotSlot.BUFFER, ResourceBuilder.buildServant());

        assertTrue(warehouse.viewResourcesInBuffer().contains(ResourceBuilder.buildServant()));

        warehouse.moveBetweenDepot(DepotSlot.BUFFER, DepotSlot.BOTTOM, ResourceBuilder.buildServant());

        assertTrue(warehouse.viewResourcesInBuffer().contains(ResourceBuilder.buildServant(0)));

        //trying some limit cases:
        //Buffer is empty:
        try {
            warehouse.moveBetweenDepot(DepotSlot.BUFFER, DepotSlot.BOTTOM, ResourceBuilder.buildServant());
            fail();
        } catch (NegativeResourcesDepotException e){
            e.printStackTrace();
        }
        assertEquals(ResourceBuilder.buildServant(3), warehouse.viewResourcesInDepot(DepotSlot.BOTTOM));

        //Dest depot doesn't have that type of resources:
        warehouse.insertInDepot(DepotSlot.BUFFER,ResourceBuilder.buildCoin(2));

        warehouse.removeFromDepot(DepotSlot.BOTTOM, ResourceBuilder.buildServant());
        assertEquals(ResourceBuilder.buildServant(2), warehouse.viewResourcesInDepot(DepotSlot.BOTTOM));

        assertTrue(warehouse.viewResourcesInBuffer().contains(ResourceBuilder.buildCoin(2)));

        warehouse.moveBetweenDepot(DepotSlot.BUFFER, DepotSlot.BOTTOM, ResourceBuilder.buildCoin(1));

        assertTrue(warehouse.viewResourcesInBuffer().contains(ResourceBuilder.buildCoin(2)));

        warehouse.moveBetweenDepot(DepotSlot.BOTTOM, DepotSlot.MIDDLE, ResourceBuilder.buildServant(2));
        warehouse.moveBetweenDepot(DepotSlot.BUFFER, DepotSlot.BOTTOM, ResourceBuilder.buildCoin(2));

        assertTrue(warehouse.viewResourcesInBuffer().contains(ResourceBuilder.buildCoin(0)));
        assertEquals(ResourceBuilder.buildCoin(2), warehouse.viewResourcesInDepot(DepotSlot.BOTTOM));
        assertEquals(ResourceBuilder.buildServant(2), warehouse.viewResourcesInDepot(DepotSlot.MIDDLE));

        //Moving resources from and to SpecialDepot
        warehouse.insertInDepot(DepotSlot.BUFFER, ResourceBuilder.buildStone(3));
        warehouse.insertInDepot(DepotSlot.BUFFER, ResourceBuilder.buildShield(2));

        Depot special = new SpecialDepot(ResourceBuilder.buildShield());
        warehouse.addDepot(special);

        warehouse.moveBetweenDepot(DepotSlot.BUFFER, DepotSlot.SPECIAL1, ResourceBuilder.buildShield(2));

        assertEquals(ResourceBuilder.buildShield(2), warehouse.viewResourcesInDepot(DepotSlot.SPECIAL1));
        assertTrue(warehouse.viewResourcesInBuffer().contains(ResourceBuilder.buildShield(0)));

        warehouse.moveBetweenDepot(DepotSlot.SPECIAL1, DepotSlot.BUFFER, ResourceBuilder.buildShield(1));

        assertEquals(ResourceBuilder.buildShield(1), warehouse.viewResourcesInDepot(DepotSlot.SPECIAL1));
        assertTrue(warehouse.viewResourcesInBuffer().contains(ResourceBuilder.buildShield(1)));

        //Moving resources from and to Strongbox
        warehouse.insertInDepot(DepotSlot.STRONGBOX, ResourceBuilder.buildCoin(3));
        assertTrue(warehouse.viewResourcesInStrongbox().contains(ResourceBuilder.buildCoin(3)));

        warehouse.moveBetweenDepot(DepotSlot.STRONGBOX,DepotSlot.BOTTOM, ResourceBuilder.buildCoin());

        assertTrue(warehouse.viewResourcesInStrongbox().contains(ResourceBuilder.buildCoin(3)));

        warehouse.moveBetweenDepot(DepotSlot.STRONGBOX, DepotSlot.BUFFER, ResourceBuilder.buildCoin(2));

        assertTrue(warehouse.viewResourcesInStrongbox().contains(ResourceBuilder.buildCoin(1)));
        assertTrue(warehouse.viewResourcesInBuffer().contains(ResourceBuilder.buildCoin(2)));

        warehouse.moveBetweenDepot(DepotSlot.BUFFER, DepotSlot.STRONGBOX, ResourceBuilder.buildStone(3));

        assertTrue(warehouse.viewResourcesInBuffer().contains(ResourceBuilder.buildStone(0)));
        assertTrue(warehouse.viewResourcesInStrongbox().contains(ResourceBuilder.buildStone(3)));

    }

    @Test
    public void totalResourcesTest() throws IllegalTypeInProduction, ExtraDepotsException {
        Warehouse warehouse = new Warehouse();
        Depot special1 = new SpecialDepot(ResourceBuilder.buildCoin());
        Depot special2 = new SpecialDepot(ResourceBuilder.buildServant());

        warehouse.insertInDepot(DepotSlot.BOTTOM, ResourceBuilder.buildShield(2));
        warehouse.insertInDepot(DepotSlot.MIDDLE, ResourceBuilder.buildCoin(2));
        warehouse.insertInDepot(DepotSlot.STRONGBOX, ResourceBuilder.buildStone(5));
        warehouse.insertInDepot(DepotSlot.TOP, ResourceBuilder.buildServant(1));
        warehouse.insertInDepot(DepotSlot.STRONGBOX, ResourceBuilder.buildCoin(3));
        warehouse.insertInDepot(DepotSlot.STRONGBOX, ResourceBuilder.buildShield(7));
        warehouse.insertInDepot(DepotSlot.STRONGBOX, ResourceBuilder.buildServant(5));

        warehouse.addDepot(special1);
        warehouse.addDepot(special2);

        warehouse.insertInDepot(DepotSlot.SPECIAL1, ResourceBuilder.buildCoin(1));
        warehouse.insertInDepot(DepotSlot.SPECIAL2, ResourceBuilder.buildServant(2));

        assertTrue(warehouse.getTotalResources().contains(ResourceBuilder.buildCoin(6)));
        assertTrue(warehouse.getTotalResources().contains(ResourceBuilder.buildServant(8)));
        assertTrue(warehouse.getTotalResources().contains(ResourceBuilder.buildShield(9)));
        assertTrue(warehouse.getTotalResources().contains(ResourceBuilder.buildStone(5)));

        //It doesn't change the resources inside the Depots
        assertEquals(ResourceBuilder.buildShield(2), warehouse.viewResourcesInDepot(DepotSlot.BOTTOM));
        assertEquals( ResourceBuilder.buildServant(1), warehouse.viewResourcesInDepot(DepotSlot.TOP));
        assertEquals(ResourceBuilder.buildCoin(2), warehouse.viewResourcesInDepot(DepotSlot.MIDDLE));
        assertEquals(ResourceBuilder.buildCoin(1), warehouse.viewResourcesInDepot(DepotSlot.SPECIAL1));
        assertEquals(ResourceBuilder.buildServant(2), warehouse.viewResourcesInDepot(DepotSlot.SPECIAL2));

        assertTrue(warehouse.viewResourcesInStrongbox().contains(ResourceBuilder.buildStone(5)));
        assertTrue(warehouse.viewResourcesInStrongbox().contains(ResourceBuilder.buildCoin(3)));
        assertTrue(warehouse.viewResourcesInStrongbox().contains(ResourceBuilder.buildShield(7)));
        assertTrue(warehouse.viewResourcesInStrongbox().contains(ResourceBuilder.buildServant(5)));

    }
}
