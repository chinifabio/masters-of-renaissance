package it.polimi.ingsw;
import static org.junit.jupiter.api.Assertions.*;
import it.polimi.ingsw.model.player.personalBoard.warehouse.Warehouse;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import org.junit.jupiter.api.Test;

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
        Resource coin = ResourceBuilder.buildCoin(2);

        //Testing resources in BOTTOM Depot
        test.insertInDepot(DepotSlot.BOTTOM,ResourceBuilder.buildCoin(2));
        assertEquals(coin,test.viewResourcesInDepot(DepotSlot.BOTTOM));
        test.insertInDepot(DepotSlot.BOTTOM,ResourceBuilder.buildServant(2));
        assertEquals(coin,test.viewResourcesInDepot(DepotSlot.BOTTOM));
        test.insertInDepot(DepotSlot.BOTTOM,ResourceBuilder.buildCoin());
        assertEquals(ResourceBuilder.buildCoin(3),test.viewResourcesInDepot(DepotSlot.BOTTOM));
        test.insertInDepot(DepotSlot.BOTTOM,ResourceBuilder.buildCoin());
        assertEquals(ResourceBuilder.buildCoin(3),test.viewResourcesInDepot(DepotSlot.BOTTOM));

        //Testing resources in MIDDLE Depot
        test.insertInDepot(DepotSlot.MIDDLE,ResourceBuilder.buildCoin(2));
        assertEquals(coin,test.viewResourcesInDepot(DepotSlot.MIDDLE));
        test.insertInDepot(DepotSlot.MIDDLE,ResourceBuilder.buildServant(2));
        assertEquals(coin,test.viewResourcesInDepot(DepotSlot.MIDDLE));
        test.insertInDepot(DepotSlot.MIDDLE,ResourceBuilder.buildCoin());
        assertEquals(coin,test.viewResourcesInDepot(DepotSlot.MIDDLE));
        test.insertInDepot(DepotSlot.MIDDLE,ResourceBuilder.buildCoin());
        assertEquals(coin,test.viewResourcesInDepot(DepotSlot.MIDDLE));

        //Testing resources in TOP Depot
        test.insertInDepot(DepotSlot.TOP,ResourceBuilder.buildCoin(2));
        assertEquals(ResourceBuilder.buildEmpty(),test.viewResourcesInDepot(DepotSlot.TOP));
        test.insertInDepot(DepotSlot.TOP,ResourceBuilder.buildServant(2));
        assertEquals(ResourceBuilder.buildEmpty(),test.viewResourcesInDepot(DepotSlot.TOP));
        test.insertInDepot(DepotSlot.TOP,ResourceBuilder.buildCoin());
        assertEquals(ResourceBuilder.buildCoin(),test.viewResourcesInDepot(DepotSlot.TOP));
        test.insertInDepot(DepotSlot.TOP,ResourceBuilder.buildCoin());
        assertEquals(ResourceBuilder.buildCoin(),test.viewResourcesInDepot(DepotSlot.TOP));

    }
}
