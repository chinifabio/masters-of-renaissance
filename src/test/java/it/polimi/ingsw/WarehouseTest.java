package it.polimi.ingsw;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.exceptions.ExtraDepotsException;
import it.polimi.ingsw.model.player.personalBoard.warehouse.Warehouse;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class WarehouseTest {
    /**
     * Testing if the resources are correctly inserted into the Depots with all the constraints
     */
    @Test
    public void insertResources(){
        Warehouse test = new Warehouse();

        //Testing the bottom depot
        assertTrue(test.insertInDepot(DepotSlot.BOTTOM,ResourceBuilder.buildCoin(2)));
        assertFalse(test.insertInDepot(DepotSlot.BOTTOM,ResourceBuilder.buildServant()));
        assertTrue(test.insertInDepot(DepotSlot.BOTTOM,ResourceBuilder.buildCoin()));
        assertFalse(test.insertInDepot(DepotSlot.BOTTOM,ResourceBuilder.buildCoin()));

        //Testing the middle depot
        assertTrue(test.insertInDepot(DepotSlot.MIDDLE,ResourceBuilder.buildCoin(2)));
        assertFalse(test.insertInDepot(DepotSlot.MIDDLE,ResourceBuilder.buildCoin()));
        assertFalse(test.insertInDepot(DepotSlot.MIDDLE,ResourceBuilder.buildServant()));

        //Testing the top depot
        assertFalse(test.insertInDepot(DepotSlot.TOP,ResourceBuilder.buildCoin(2)));
        assertTrue(test.insertInDepot(DepotSlot.TOP, ResourceBuilder.buildServant()));
        assertFalse(test.insertInDepot(DepotSlot.TOP, ResourceBuilder.buildCoin(2)));
    }

    /**
     * Testing the warehouse returns the resources into the depots correctly
     */
    @Test
    public void viewResources(){
        Warehouse test = new Warehouse();

        //Testing resources in BOTTOM Depot
        test.insertInDepot(DepotSlot.BOTTOM,ResourceBuilder.buildShield(2));
        assertEquals(ResourceBuilder.buildShield(2), test.viewResourcesInDepot(DepotSlot.BOTTOM));
        test.insertInDepot(DepotSlot.BOTTOM,ResourceBuilder.buildShield(2)); //Should not add
        assertEquals(ResourceBuilder.buildShield(2), test.viewResourcesInDepot(DepotSlot.BOTTOM));
        test.insertInDepot(DepotSlot.BOTTOM,ResourceBuilder.buildShield());
        assertEquals(ResourceBuilder.buildShield(3), test.viewResourcesInDepot(DepotSlot.BOTTOM));

        //Testing resources in MIDDLE Depot
        test.insertInDepot(DepotSlot.MIDDLE,ResourceBuilder.buildShield(2));
        assertEquals(ResourceBuilder.buildShield(2), test.viewResourcesInDepot(DepotSlot.MIDDLE));
        test.insertInDepot(DepotSlot.MIDDLE,ResourceBuilder.buildShield(2)); //Should not add
        assertEquals(ResourceBuilder.buildShield(2), test.viewResourcesInDepot(DepotSlot.MIDDLE));
        test.insertInDepot(DepotSlot.MIDDLE,ResourceBuilder.buildStone());//Should not add
        assertEquals(ResourceBuilder.buildShield(2), test.viewResourcesInDepot(DepotSlot.MIDDLE));

        //Testing resources in TOP Depot
        test.insertInDepot(DepotSlot.TOP,ResourceBuilder.buildShield(2));//Should not add
        assertEquals(ResourceBuilder.buildEmpty(), test.viewResourcesInDepot(DepotSlot.TOP));
        test.insertInDepot(DepotSlot.TOP,ResourceBuilder.buildShield(1));
        assertEquals(ResourceBuilder.buildShield(1), test.viewResourcesInDepot(DepotSlot.TOP));
        test.insertInDepot(DepotSlot.TOP,ResourceBuilder.buildStone());//Should not add
        assertEquals(ResourceBuilder.buildShield(1), test.viewResourcesInDepot(DepotSlot.TOP));


    }

    /**
     * Testing if the resources are correctly inserted into the warehouse
     */
    @Test
    public void insertInStrongbox(){
        Warehouse test = new Warehouse();
        List<Resource> list = new ArrayList<>();

        list.add(0,ResourceBuilder.buildShield(0));
        list.add(1, ResourceBuilder.buildStone(3));
        list.add(2,ResourceBuilder.buildServant(3));
        list.add(3,ResourceBuilder.buildCoin(2));



        test.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildCoin(2));
        test.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildServant(3));
        test.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildStone(3));
        assertArrayEquals(list.toArray(), test.viewResourcesInStrongbox(DepotSlot.STRONGBOX).toArray());

        test.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildCoin(2));
        list.set(3,ResourceBuilder.buildCoin(4));
        assertArrayEquals(list.toArray(), test.viewResourcesInStrongbox(DepotSlot.STRONGBOX).toArray());

        test.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildServant(3));
        test.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildStone(3));
        test.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildStone(3));
        list.set(2,ResourceBuilder.buildServant(6));
        list.set(1,ResourceBuilder.buildStone(9));
        assertArrayEquals(list.toArray(), test.viewResourcesInStrongbox(DepotSlot.STRONGBOX).toArray());

        test.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildShield(4));
        test.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildShield(2));
        list.set(0,ResourceBuilder.buildShield(6));
        assertArrayEquals(list.toArray(), test.viewResourcesInStrongbox(DepotSlot.STRONGBOX).toArray());
        test.insertInDepot(DepotSlot.STRONGBOX,ResourceBuilder.buildFaithPoint(4));
        assertArrayEquals(list.toArray(), test.viewResourcesInStrongbox(DepotSlot.STRONGBOX).toArray());

    }

    /**
     * Testing if the extra Depots are correctly created
     */
    @Test
    public void InsertInExtraDepots() throws ExtraDepotsException {
        Warehouse test = new Warehouse();
        boolean exc = false;

        //Add depot SPECIAL1
        test.addDepot(ResourceBuilder.buildShield());
        test.insertInDepot(DepotSlot.SPECIAL1, ResourceBuilder.buildShield());
        assertEquals(ResourceBuilder.buildShield(1),test.viewResourcesInDepot(DepotSlot.SPECIAL1));
        test.insertInDepot(DepotSlot.SPECIAL1, ResourceBuilder.buildShield(3));
        test.insertInDepot(DepotSlot.SPECIAL1, ResourceBuilder.buildCoin());
        assertEquals(ResourceBuilder.buildShield(1),test.viewResourcesInDepot(DepotSlot.SPECIAL1));
        test.insertInDepot(DepotSlot.SPECIAL1, ResourceBuilder.buildShield());
        assertEquals(ResourceBuilder.buildShield(2),test.viewResourcesInDepot(DepotSlot.SPECIAL1));
        //Add depot SPECIAL2
        test.addDepot(ResourceBuilder.buildCoin());
        System.out.println();
        assertEquals(ResourceBuilder.buildCoin(0),test.viewResourcesInDepot(DepotSlot.SPECIAL2));
        test.insertInDepot(DepotSlot.SPECIAL2, ResourceBuilder.buildStone());
        assertEquals(ResourceBuilder.buildCoin(0),test.viewResourcesInDepot(DepotSlot.SPECIAL2));
        test.insertInDepot(DepotSlot.SPECIAL2, ResourceBuilder.buildCoin());
        assertEquals(ResourceBuilder.buildCoin(),test.viewResourcesInDepot(DepotSlot.SPECIAL2));
        test.insertInDepot(DepotSlot.SPECIAL2, ResourceBuilder.buildCoin());
        test.insertInDepot(DepotSlot.SPECIAL2, ResourceBuilder.buildCoin());
        assertEquals(ResourceBuilder.buildCoin(2),test.viewResourcesInDepot(DepotSlot.SPECIAL2));
        try{
            test.addDepot(ResourceBuilder.buildStone());
        } catch (ExtraDepotsException e){
            exc = true;
        }
        assertTrue(exc);


    }
}
