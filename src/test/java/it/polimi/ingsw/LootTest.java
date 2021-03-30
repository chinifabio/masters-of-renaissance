package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.exceptions.LootTypeException;
import it.polimi.ingsw.model.resource.CardLoot;
import it.polimi.ingsw.model.resource.Loot;
import it.polimi.ingsw.model.resource.ResourceLoot;
import it.polimi.ingsw.model.resource.resourceTypes.ResourceType;
import org.junit.jupiter.api.Test;

/**
 * test collector for loot and resources tests
 */
public class LootTest {

    /**
     * testing ResourceLoot class returning the right amount given
     */
    @Test
    public void resLootAmount(){
        int amount = 3;
        ResourceType res = ResourceType.COIN;

        Loot test = new ResourceLoot(res, amount);
        boolean result = false;

        if (test.getAmount() == amount) result = true;

        assertTrue(result);
    }

    /**
     * testing ResourceLoot class throwing exception when getColor method is invoked
     */
    @Test
    public void resLootColor() {
        int amount = 3;
        ResourceType res = ResourceType.COIN;

        Loot test = new ResourceLoot(res, amount);
        boolean result = false;

        try {
            test.getColor();
        } catch (Exception e) {
            result = true;
        }

        assertTrue(result);
    }

    /**
     * testing ResourceLoot class throwing exception when getLevel method is invoked
     */
    @Test
    public void resLootLevel() {
        int amount = 3;
        ResourceType res = ResourceType.COIN;

        Loot test = new ResourceLoot(res, amount);
        boolean result = false;

        try {
            test.getLevel();
        } catch (Exception e) {
            result = true;
        }

        assertTrue(result);
    }

    /**
     * testing ResourceLoot class returning the right ResourceType given
     */
    @Test
    public void resLootType() {
        int amount = 3;
        ResourceType res = ResourceType.COIN;

        Loot test = new ResourceLoot(res, amount);
        boolean result = false;

        try {
            if(test.getType() == res) result = true;
        } catch (LootTypeException e) {
            e.printStackTrace();
        }

        assertTrue(result);
    }

    /**
     * testing CardLoot class returning 1 when getAmount is invoked
     */
    @Test
    public void cardLootAmount(){
        LevelDevCard lev = LevelDevCard.LEVEL1;
        ColorDevCard col = ColorDevCard.BLUE;

        Loot test = new CardLoot(lev, col);
        boolean result = false;

        if (test.getAmount() == 1) result = true;

        assertTrue(result);
    }

    /**
     * testing CardLoot class returning the right color given
     */
    @Test
    public void cardLootColor() {
        LevelDevCard lev = LevelDevCard.LEVEL1;
        ColorDevCard col = ColorDevCard.BLUE;

        Loot test = new CardLoot(lev, col);
        boolean result = false;

        try {
            if(test.getColor() == col) result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(result);
    }

    /**
     * testing CardLoot class returning the right level given
     */
    @Test
    public void cardLootLevel() {
        LevelDevCard lev = LevelDevCard.LEVEL1;
        ColorDevCard col = ColorDevCard.BLUE;

        Loot test = new CardLoot(lev, col);
        boolean result = false;

        try {
            if(test.getLevel() == lev) result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(result);
    }

    /**
     * testing CardLoot class throwing exception when getType method is invoked
     */
    @Test
    public void cardLootType() {
        LevelDevCard lev = LevelDevCard.LEVEL1;
        ColorDevCard col = ColorDevCard.BLUE;

        Loot test = new CardLoot(lev, col);
        boolean result = false;

        try {
            test.getType();
        } catch (Exception e) {
            result = true;
        }

        assertTrue(result);
    }
}
