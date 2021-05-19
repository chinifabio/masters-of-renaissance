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
    private Match singleplayer = new SingleplayerMatch(view);

    int oldLorenzoPos = 0;
    int oldNumDiscarded = 0;

    @BeforeEach
    public void initializeMatch() throws IllegalTypeInProduction {
        gino = new Player("gino", singleplayer, view);
        assertTrue(singleplayer.playerJoin(gino));

        //assertTrue(singleplayer.startGame());

        // the player discard the first two leader card
        assertDoesNotThrow(()->singleplayer.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()->singleplayer.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()->singleplayer.test_getCurrPlayer().endThisTurn());
        // once the second leader is discarded the turn end and match manage lorenzo automatically
    }

    /**
     * create a single player match, a lorenzo player and some other test player.
     * Then try to do some operation to the match and with assertion check if the return value of it is right,
     * so you can know if the operation is succeed of failed.
     */
    @RepeatedTest(10)
    public void simpleLifeCycleOfMatch() {
        this.testLorenzoAction(((SingleplayerMatch) this.singleplayer).test_getSoloDeck().getDiscarded().getCardID());

        assertTrue(singleplayer.test_getCurrPlayer().canDoStuff());

        assertDoesNotThrow(()-> assertEquals(singleplayer.test_getCurrPlayer().useMarketTray(RowCol.ROW, 0).header, HeaderTypes.OK));
        assertDoesNotThrow(()-> assertEquals(singleplayer.test_getCurrPlayer().useMarketTray(RowCol.ROW, 0).header, HeaderTypes.INVALID));

        assertDoesNotThrow(()->singleplayer.test_getCurrPlayer().endThisTurn());
        this.testLorenzoAction(((SingleplayerMatch) this.singleplayer).test_getSoloDeck().getDiscarded().getCardID());

        assertDoesNotThrow(()-> assertEquals(singleplayer.test_getCurrPlayer().useMarketTray(RowCol.ROW, 0).header, HeaderTypes.OK));
        assertDoesNotThrow(()-> assertEquals(singleplayer.test_getCurrPlayer().useMarketTray(RowCol.ROW, 0).header, HeaderTypes.INVALID));

        assertDoesNotThrow(()->singleplayer.test_getCurrPlayer().endThisTurn());
        this.testLorenzoAction(((SingleplayerMatch) this.singleplayer).test_getSoloDeck().getDiscarded().getCardID());
    }

    @Test
    public void CreateTokensTest() throws MissingCardException {
        Deck<SoloActionToken> soloToken;
        List<SoloActionToken> init = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            init = objectMapper.readValue(
                    new File("src/resources/SoloActionTokens.json"),
                    new TypeReference<List<SoloActionToken>>(){});
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("The file to create the SoloActionTokens wasn't found");
        }
        soloToken = new Deck<>(init);

        assertEquals(7, soloToken.getNumberOfCards());
        assertEquals("ST1", soloToken.peekFirstCard().getCardID());
        assertEquals("ST5", soloToken.peekCard("ST5").getCardID());
    }

    @Test
    public void endGameByLorenzo() {
        assertDoesNotThrow(()-> {
            while (singleplayer.test_getGameOnAir()) {
                singleplayer.test_getCurrPlayer().endThisTurn();
            }
        });

        assertTrue(((SingleplayerMatch) singleplayer).test_getLorenzoWinner());
    }

    private void testLorenzoAction(String idToken) {
        switch (idToken) {
            case "ST1":
            case "ST3":
            case "ST2":
            case "ST4":
                assertEquals(this.oldNumDiscarded + 2, ((SingleplayerMatch) this.singleplayer).test_getDiscarded().getNumberOfCards());
                this.oldNumDiscarded += 2;
                break;
            case "ST5":
            case "ST6":
                assertEquals(this.oldLorenzoPos + 2, ((SingleplayerMatch) this.singleplayer).test_getLorenzoPosition());
                this.oldLorenzoPos += 2;
                break;
            case "vuoto":
                assertEquals(this.oldLorenzoPos + 1, ((SingleplayerMatch) this.singleplayer).test_getLorenzoPosition());
                this.oldLorenzoPos += 1;
                break;
            default: fail();
            // st7 makes the discarded token stack empty
        }
    }
}
