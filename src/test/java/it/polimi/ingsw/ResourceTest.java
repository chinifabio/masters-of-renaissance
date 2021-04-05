package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.exceptions.UnobtainableResourceException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerModifier;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.model.resource.builder.Resource;
import it.polimi.ingsw.model.resource.builder.ResourceDirector;
import org.junit.jupiter.api.Test;

public class ResourceTest {
    @Test
    public void createAndCheckType() {
        Resource coin = ResourceDirector.buildCoin();
        assertTrue(coin.type() == ResourceType.COIN && coin.amount() == 1);

        Resource stone = ResourceDirector.buildStone();
        assertTrue(stone.type() == ResourceType.STONE && stone.amount() == 1);

        Resource shield = ResourceDirector.buildShield();
        assertTrue(shield.type() == ResourceType.SHIELD && shield.amount() == 1);

        Resource servant = ResourceDirector.buildServant();
        assertTrue(servant.type() == ResourceType.SERVANT && servant.amount() == 1);

        Resource faithpoint = ResourceDirector.buildFaithPoint();
        assertTrue(faithpoint.type() == ResourceType.FAITHPOINT && faithpoint.amount() == 1);

        Resource unknown = ResourceDirector.buildUnknown();
        assertTrue(unknown.type() == ResourceType.UNKNOWN && unknown.amount() == 1);
    }

    @Test
    public void checkAmount() {
        int amount = 3;

        Resource coin = ResourceDirector.buildCoin(amount);
        assertTrue(coin.type() == ResourceType.COIN && coin.amount() == amount);

        Resource stone = ResourceDirector.buildStone(amount);
        assertTrue(stone.type() == ResourceType.STONE && stone.amount() == amount);

        Resource shield = ResourceDirector.buildShield(amount);
        assertTrue(shield.type() == ResourceType.SHIELD && shield.amount() == amount);

        Resource servant = ResourceDirector.buildServant(amount);
        assertTrue(servant.type() == ResourceType.SERVANT && servant.amount() == amount);

        Resource faithpoint = ResourceDirector.buildFaithPoint(amount);
        assertTrue(faithpoint.type() == ResourceType.FAITHPOINT && faithpoint.amount() == amount);

        Resource unknown = ResourceDirector.buildUnknown(amount);
        assertTrue(unknown.type() == ResourceType.UNKNOWN && unknown.amount() == amount);
    }

    @Test
    public void nonObtainable() {
        Resource unknown = ResourceDirector.buildUnknown();
        PlayerModifier player = new Player("dummy");
        boolean result = false;

        try {
            unknown.onObtain(player);
        } catch (UnobtainableResourceException e) {
            e.printStackTrace();
            result = true;
        }
        assertTrue(result);
    }
}
