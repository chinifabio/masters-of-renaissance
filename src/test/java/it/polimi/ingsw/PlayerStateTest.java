package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;
import it.polimi.ingsw.model.exceptions.IllegalMovesException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.state.MainActionDoneState;
import it.polimi.ingsw.model.player.state.NoActionDoneState;
import it.polimi.ingsw.model.player.state.NotHisTurnState;
import org.junit.jupiter.api.Test;

public class PlayerStateTest {
    /**
     * test a possible lifecycle of a player in his turn testing if throw exception to wrong input
     * @throws IllegalMovesException
     */
    @Test
    public void allInput() throws IllegalMovesException { /*
        Player player = new Player("dummy", null);

        // not his turn
        try {
            player.doMainActionInput();
            fail();
        } catch (IllegalMovesException e) {
            System.out.println(e.getMsg());
        }

        try {
            player.endTurnInput();
            fail();
        } catch (IllegalMovesException e) {
            System.out.println(e.getMsg());
        }

        try {
            player.startTurnInput();
        } catch (IllegalMovesException e) {
            System.out.println(e.getMsg());
            fail();
        }
        assertTrue(player.getPlayerState() instanceof NoActionDoneState);

        // no action done
        try {
            player.startTurnInput();
            fail();
        } catch (IllegalMovesException e) {
            System.out.println(e.getMsg());
        }

        try {
            player.endTurnInput();
        } catch (IllegalMovesException e) {
            System.out.println(e.getMsg());
            fail();
        }

        assertTrue(player.getPlayerState() instanceof NotHisTurnState);
        player.startTurnInput();

        assertTrue(player.getPlayerState() instanceof NoActionDoneState);

        try {
            player.doMainActionInput();
        } catch (IllegalMovesException e) {
            System.out.println(e.getMsg());
            fail();
        }

        assertTrue(player.getPlayerState() instanceof MainActionDoneState);
        // main action done

        try {
            player.startTurnInput();
            fail();
        } catch (IllegalMovesException e) {
            System.out.println(e.getMsg());
        }

        try {
            player.startTurnInput();
            fail();
        } catch (IllegalMovesException e) {
            System.out.println(e.getMsg());
        }

        try {
            player.endTurnInput();
        } catch (IllegalMovesException e) {
            System.out.println(e.getMsg());
            fail();
        }
        assertTrue(player.getPlayerState() instanceof NotHisTurnState);*/
    }
}
