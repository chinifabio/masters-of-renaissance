package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;
import it.polimi.ingsw.model.exceptions.IllegalMovesException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.state.MainActionDoneState;
import it.polimi.ingsw.model.player.state.NoActionDoneState;
import it.polimi.ingsw.model.player.state.NotHisTurnState;
import org.junit.jupiter.api.Test;

public class PlayerStateTest {
    @Test
    public void allInput() throws IllegalMovesException {
        Player player = new Player("dummy");

        boolean exception = false;

        // not his turn
        try {
            player.mainActionDone();
        } catch (IllegalMovesException e) {
            System.out.println(e.getMsg());
            exception = true;
        }
        assertTrue(exception);

        exception = false;
        try {
            player.endTurn();
        } catch (IllegalMovesException e) {
            System.out.println(e.getMsg());
            exception = true;
        }
        assertTrue(exception);

        exception = true;
        try {
            player.startTurn();
        } catch (IllegalMovesException e) {
            System.out.println(e.getMsg());
            exception = false;
        }
        assertTrue(exception);

        assertTrue(player.getPlayerState() instanceof NoActionDoneState);

        // no action done
        exception = false;
        try {
            player.startTurn();
        } catch (IllegalMovesException e) {
            System.out.println(e.getMsg());
            exception = true;
        }
        assertTrue(exception);

        exception = false;
        try {
            player.endTurn();
        } catch (IllegalMovesException e) {
            System.out.println(e.getMsg());
            exception = true;
        }
        assertFalse(exception);
        assertTrue(player.getPlayerState() instanceof NotHisTurnState);
        player.startTurn();

        assertTrue(player.getPlayerState() instanceof NoActionDoneState);
        exception = false;
        try {
            player.mainActionDone();
        } catch (IllegalMovesException e) {
            System.out.println(e.getMsg());
            exception = true;
        }
        assertFalse(exception);

        assertTrue(player.getPlayerState() instanceof MainActionDoneState);
        // main action done

        exception = false;
        try {
            player.startTurn();
        } catch (IllegalMovesException e) {
            System.out.println(e.getMsg());
            exception = true;
        }
        assertTrue(exception);

        exception = false;
        try {
            player.startTurn();
        } catch (IllegalMovesException e) {
            System.out.println(e.getMsg());
            exception = true;
        }
        assertTrue(exception);

        exception = false;
        try {
            player.endTurn();
        } catch (IllegalMovesException e) {
            System.out.println(e.getMsg());
            exception = true;
        }
        assertFalse(exception);
        assertTrue(player.getPlayerState() instanceof NotHisTurnState);
    }
}
