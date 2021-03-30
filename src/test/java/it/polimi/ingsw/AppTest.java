package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.exceptions.LootTypeException;
import it.polimi.ingsw.model.resource.Loot;
import it.polimi.ingsw.model.resource.ResourceLoot;
import it.polimi.ingsw.model.resource.resourceTypes.ResourceType;
import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * test if it throw an Exception when someone calls get color on ResourceLoot
     */
    @Test
    public void testLootException(){
        Loot toTest = new ResourceLoot(ResourceType.COIN, 3);
        boolean result = false;
        try {
            toTest.getColor();
        } catch (LootTypeException e) {
            System.out.println(e.getMsg());
            result = true;
        }
        assertTrue(result);
    }
}
