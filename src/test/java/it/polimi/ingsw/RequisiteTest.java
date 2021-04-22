package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.requisite.CardRequisite;
import it.polimi.ingsw.model.requisite.Requisite;
import it.polimi.ingsw.model.requisite.ResourceRequisite;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import org.junit.jupiter.api.Test;

/**
 * test collector for loot and resources tests
 */
public class RequisiteTest {

    /**
     * testing ResourceLoot class returning the right amount given
     */
    @Test
    public void resLootAmount(){
        int amount = 3;
        Resource res = ResourceBuilder.buildCoin(amount);

        Requisite test = new ResourceRequisite(res);
        boolean result = test.getAmount() == amount;

        assertTrue(result);
    }

    /**
     * testing ResourceLoot class throwing exception when getColor method is invoked
     */
    @Test
    public void resLootColor() {
        int amount = 3;
        Resource res = ResourceBuilder.buildCoin(amount);

        Requisite test = new ResourceRequisite(res);
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
        Resource res = ResourceBuilder.buildCoin(amount);

        Requisite test = new ResourceRequisite(res);
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
        Resource res = ResourceBuilder.buildCoin(amount);

        Requisite test = new ResourceRequisite(res);
        boolean result = false;

        try {
            if(test.getType() == res.type()) result = true;
        } catch (Exception e) {
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

        Requisite test = new CardRequisite(lev, col, 1);
        boolean result = test.getAmount() == 1;

        assertTrue(result);
    }

    /**
     * testing CardLoot class returning the right color given
     */
    @Test
    public void cardLootColor() {
        LevelDevCard lev = LevelDevCard.LEVEL1;
        ColorDevCard col = ColorDevCard.BLUE;

        Requisite test = new CardRequisite(lev, col,1);
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

        Requisite test = new CardRequisite(lev, col,1);
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

        Requisite test = new CardRequisite(lev, col,1);
        boolean result = false;

        try {
            test.getType();
        } catch (Exception e) {
            result = true;
        }

        assertTrue(result);
    }
}
