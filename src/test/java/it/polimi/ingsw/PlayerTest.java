package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.matchTests.MultiplayerMatchTest;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;
import it.polimi.ingsw.model.exceptions.requisite.LootTypeException;
import it.polimi.ingsw.model.exceptions.requisite.NoRequisiteException;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
import it.polimi.ingsw.model.match.match.Match;
import it.polimi.ingsw.model.match.match.MultiplayerMatch;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerState;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;
import org.junit.jupiter.api.Test;

public class PlayerTest {
    @Test
    public void turnLifeCycle() { /*
        Player dummy = new Player("dummy", null);

        assertFalse(dummy.canDoStuff());

        dummy.startHisTurn();

        assertTrue(dummy.canDoStuff());

        try {
            dummy.doMainActionInput();
        } catch (IllegalMovesException e) {
            fail();
        }

        assertTrue(dummy.canDoStuff());

        try {
            dummy.endTurnInput();
        } catch (IllegalMovesException e) {
            fail();
        }

        assertFalse(dummy.canDoStuff());*/
    }

    @Test
    public void DevCard()  {
        // todo
    }
}
