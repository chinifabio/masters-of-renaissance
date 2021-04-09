package it.polimi.ingsw;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.exceptions.NegativeResourcesDepotException;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.Depot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotBuilder;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class DepotTest {
    /**
     * Testing if resources are correctly removed by NormalDepot
     * @throws NegativeResourcesDepotException if the depot doesn't have enough resources to be removed
     */
    @Test
    public void removeResourcesFromNormalDepot() throws NegativeResourcesDepotException {
        Depot depot = DepotBuilder.buildBottomDepot();
        Resource toRemove = ResourceBuilder.buildCoin();
        Resource notInDepot = ResourceBuilder.buildServant();
        boolean exception = false;

        depot.insert(ResourceBuilder.buildCoin(2));
        assertEquals(ResourceBuilder.buildCoin(2), depot.viewResources());
        depot.withdraw(toRemove);
        assertEquals(ResourceBuilder.buildCoin(), depot.viewResources());
        depot.withdraw(notInDepot);
        assertEquals(ResourceBuilder.buildCoin(), depot.viewResources());
        depot.withdraw(toRemove);

        try{
            depot.withdraw(toRemove);
        }catch (NegativeResourcesDepotException e){
            exception = true;
        }
        assertTrue(exception);

        depot.insert(ResourceBuilder.buildServant(3));
        assertEquals(ResourceBuilder.buildServant(3),depot.viewResources());
        depot.withdraw(ResourceBuilder.buildServant(2));
        assertEquals(ResourceBuilder.buildServant(),depot.viewResources());
    }

    /**
     * Testing if resources are correctly removed by Strongbox
     * @throws NegativeResourcesDepotException if the Strongbox doesn't have enough resources to be removed
     */
    @Test
    public void removeFromStrongbox() throws NegativeResourcesDepotException{
        Depot depot = DepotBuilder.buildStrongBoxDepot();
        List<Resource> list = new ArrayList<>();
        boolean exc = false;

        list.add(0,ResourceBuilder.buildShield(5));
        list.add(1, ResourceBuilder.buildStone(0));
        list.add(2,ResourceBuilder.buildServant(2));
        list.add(3,ResourceBuilder.buildCoin(4));

        depot.insert(ResourceBuilder.buildServant(2));
        depot.insert(ResourceBuilder.buildCoin(4));
        depot.insert(ResourceBuilder.buildShield(5));
        assertArrayEquals(list.toArray(), depot.viewAllResources().toArray());

        depot.withdraw(ResourceBuilder.buildCoin(3));
        list.set(3,ResourceBuilder.buildCoin(1));
        assertArrayEquals(list.toArray(), depot.viewAllResources().toArray());


        depot.withdraw(ResourceBuilder.buildCoin());
        list.set(3,ResourceBuilder.buildCoin(0));
        assertArrayEquals(list.toArray(), depot.viewAllResources().toArray());


        depot.insert(ResourceBuilder.buildServant(2));
        depot.insert(ResourceBuilder.buildStone(2));
        list.set(2,ResourceBuilder.buildServant(4));
        list.set(1, ResourceBuilder.buildStone(2));
        assertArrayEquals(list.toArray(), depot.viewAllResources().toArray());


        depot.withdraw(ResourceBuilder.buildServant(4));
        list.set(2,ResourceBuilder.buildServant(0));
        assertArrayEquals(list.toArray(), depot.viewAllResources().toArray());

        try {
            depot.withdraw(ResourceBuilder.buildShield(20));
        } catch (NegativeResourcesDepotException e){
            exc = true;
        }
        assertTrue(exc);
        assertArrayEquals(list.toArray(), depot.viewAllResources().toArray());
    }

    @Test
    public void removeFromSpecialDepot() throws NegativeResourcesDepotException{
        Depot special = DepotBuilder.buildSpecialDepot(ResourceBuilder.buildShield());
        boolean exc = false;

        special.insert(ResourceBuilder.buildShield(2));
        special.withdraw(ResourceBuilder.buildShield());
        assertEquals(ResourceBuilder.buildShield(),special.viewResources());

        special.withdraw(ResourceBuilder.buildShield());
        assertEquals(ResourceBuilder.buildShield(0),special.viewResources());

        special.insert(ResourceBuilder.buildStone(2));
        assertEquals(ResourceBuilder.buildShield(0),special.viewResources());

        special.insert(ResourceBuilder.buildShield(1));
        assertEquals(ResourceBuilder.buildShield(1),special.viewResources());

        try {
            special.withdraw(ResourceBuilder.buildShield(3));
        } catch (NegativeResourcesDepotException e){
            exc = true;
        }
        assertTrue(exc);
        assertEquals(ResourceBuilder.buildShield(1),special.viewResources());


    }
}
