package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.exceptions.IllegalMovesException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import org.junit.jupiter.api.Test;

public class PlayerTest {
    @Test
    public void test1() {
        Player dummy = new Player("dummy");

        assertFalse(dummy.canDoStuff());

        dummy.startHisTurn();

        assertTrue(dummy.canDoStuff());

        try {
            dummy.doMainAction();
        } catch (IllegalMovesException e) {
            fail();
        }

        assertTrue(dummy.canDoStuff());

        try {
            dummy.endTurn();
        } catch (IllegalMovesException e) {
            fail();
        }

        assertFalse(dummy.canDoStuff());


    }
}
