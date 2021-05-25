package it.polimi.ingsw.matchTests;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.model.Dispatcher;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.SoloActionToken;
import it.polimi.ingsw.model.exceptions.card.MissingCardException;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
import it.polimi.ingsw.model.match.markettray.RowCol;
import it.polimi.ingsw.model.match.match.Match;
import it.polimi.ingsw.model.match.match.SingleplayerMatch;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SingleplayerMatchTest {
    private Player gino;

    Dispatcher view = new Dispatcher();
    private Match singleplayer;

    int oldLorenzoPos = 0;
    int oldNumDiscarded = 0;

    @BeforeEach
    public void initializeMatch() throws IllegalTypeInProduction, IOException {
        try {
            singleplayer = new SingleplayerMatch(view);
        } catch (IOException e) {
            fail(e.getMessage());
        }

        gino = new Player("gino", singleplayer, view);
        assertTrue(singleplayer.playerJoin(gino));

        //assertTrue(singleplayer.startGame());

        // the player discard the first two leader card
        assertDoesNotThrow(()->singleplayer.currentPlayer().test_discardLeader());
        assertDoesNotThrow(()->singleplayer.currentPlayer().test_discardLeader());
        assertDoesNotThrow(()->singleplayer.currentPlayer().endThisTurn());
        // once the second leader is discarded the turn end and match manage lorenzo automatically
    }

    /**
     * create a single player match, a lorenzo player and some other test player.
     * Then try to do some operation to the match and with assertion check if the return value of it is right,
     * so you can know if the operation is succeed of failed.
     */
    @RepeatedTest(10)
    public void simpleLifeCycleOfMatch() {
        // need to count all the discarded resources to correctly count the lorenzo points

        //assertEquals(singleplayer.currentPlayer().useMarketTray(RowCol.ROW, 0).header, HeaderTypes.OK);
        //assertEquals(singleplayer.currentPlayer().useMarketTray(RowCol.ROW, 0).header, HeaderTypes.INVALID);

        singleplayer.currentPlayer().test_endTurnNoMain();
        this.testLorenzoAction(((SingleplayerMatch) this.singleplayer).obtainSoloTokens().getDiscarded().getCardID());

        //assertEquals(singleplayer.currentPlayer().useMarketTray(RowCol.ROW, 0).header, HeaderTypes.OK);
        //assertEquals(singleplayer.currentPlayer().useMarketTray(RowCol.ROW, 0).header, HeaderTypes.INVALID);

        singleplayer.currentPlayer().test_endTurnNoMain();
        this.testLorenzoAction(((SingleplayerMatch) this.singleplayer).obtainSoloTokens().getDiscarded().getCardID());
    }

    @Test
    public void CreateTokensTest() throws MissingCardException {
        Deck<SoloActionToken> soloToken;
        List<SoloActionToken> init = new ArrayList<>();

        try {
            init = new ObjectMapper().readValue(
                    getClass().getResourceAsStream("/SoloActionTokens.json"),
                    new TypeReference<List<SoloActionToken>>(){});
        }catch (IOException e){
            fail("can't find solo token file");
        }
        soloToken = new Deck<>(init);

        assertEquals(7, soloToken.getNumberOfCards());
        assertEquals("ST1", soloToken.peekFirstCard().getCardID());
        assertEquals("ST5", soloToken.peekCard("ST5").getCardID());
    }

    @Test
    public void endGameByLorenzo() {
        assertDoesNotThrow(()-> {
            while (singleplayer.isGameOnAir()) {
                assertEquals(HeaderTypes.OK, singleplayer.currentPlayer().useMarketTray(RowCol.ROW, 1).header);
                singleplayer.currentPlayer().test_endTurnNoMain();
            }
        });

        assertTrue(((SingleplayerMatch) singleplayer).isLorenzoWinner());
    }

    private void testLorenzoAction(String idToken) {
        switch (idToken) {
            case "ST1":
            case "ST3":
            case "ST2":
            case "ST4":
                assertEquals(this.oldNumDiscarded += 2, ((SingleplayerMatch) this.singleplayer).test_getDiscarded().getNumberOfCards());
                break;
            case "ST5":
            case "ST6":
                assertEquals(this.oldLorenzoPos += 2, ((SingleplayerMatch) this.singleplayer).lorenzoPosition());
                break;
            default:
                assertEquals(this.oldLorenzoPos += 1, ((SingleplayerMatch) this.singleplayer).lorenzoPosition());
                break;
            // st7 makes the discarded token stack empty
        }
    }
}
