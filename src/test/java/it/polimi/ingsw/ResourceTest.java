package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayableCardReaction;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import org.junit.jupiter.api.Test;

public class ResourceTest {
    @Test
    public void createAndCheckType() {
        Resource coin = ResourceBuilder.buildCoin();
        assertTrue(coin.type() == ResourceType.COIN && coin.amount() == 1);
        assertTrue(coin.isStorable());

        Resource stone = ResourceBuilder.buildStone();
        assertTrue(stone.type() == ResourceType.STONE && stone.amount() == 1);
        assertTrue(stone.isStorable());

        Resource shield = ResourceBuilder.buildShield();
        assertTrue(shield.type() == ResourceType.SHIELD && shield.amount() == 1);
        assertTrue(shield.isStorable());

        Resource servant = ResourceBuilder.buildServant();
        assertTrue(servant.type() == ResourceType.SERVANT && servant.amount() == 1);
        assertTrue(servant.isStorable());

        Resource faithpoint = ResourceBuilder.buildFaithPoint();
        assertTrue(faithpoint.type() == ResourceType.FAITHPOINT && faithpoint.amount() == 1);
        assertFalse(faithpoint.isStorable());

        Resource unknown = ResourceBuilder.buildUnknown();
        assertTrue(unknown.type() == ResourceType.UNKNOWN && unknown.amount() == 1);
        assertFalse(unknown.isStorable());

        Resource empty = ResourceBuilder.buildEmpty();
        assertTrue(empty.type() == ResourceType.EMPTY && empty.amount() == 0);
        assertFalse(empty.isStorable());
    }

    @Test
    public void checkAmount() {
        int amount = 3;

        Resource coin = ResourceBuilder.buildCoin(amount);
        assertTrue(coin.type() == ResourceType.COIN && coin.amount() == amount);

        Resource stone = ResourceBuilder.buildStone(amount);
        assertTrue(stone.type() == ResourceType.STONE && stone.amount() == amount);

        Resource shield = ResourceBuilder.buildShield(amount);
        assertTrue(shield.type() == ResourceType.SHIELD && shield.amount() == amount);

        Resource servant = ResourceBuilder.buildServant(amount);
        assertTrue(servant.type() == ResourceType.SERVANT && servant.amount() == amount);

        Resource faithpoint = ResourceBuilder.buildFaithPoint(amount);
        assertTrue(faithpoint.type() == ResourceType.FAITHPOINT && faithpoint.amount() == amount);
    }

    @Test
    public void nonObtainable() throws IllegalTypeInProduction {
        Resource unknown = ResourceBuilder.buildUnknown();
        PlayableCardReaction player = new Player("dummy", false);
        boolean result = false;

        try {
            unknown.onObtain(player);
            fail();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertFalse(unknown.isStorable());
    }

    @Test
    public void buildFromType() {
        assertEquals(ResourceBuilder.buildCoin(3), ResourceBuilder.buildFromType(ResourceType.COIN, 3));
    }

    @Test
    public void testBuildNewOne() {
        Resource res = ResourceBuilder.buildCoin();
        assertEquals(res, res.buildNewOne());

        res.merge(ResourceBuilder.buildCoin(2));
        assertEquals(res, res.buildNewOne(3));
    }
}
