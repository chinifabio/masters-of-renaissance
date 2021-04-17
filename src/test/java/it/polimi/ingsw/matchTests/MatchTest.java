package it.polimi.ingsw.matchTests;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.cards.SoloActionToken;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;
import it.polimi.ingsw.model.exceptions.card.MissingCardException;
import it.polimi.ingsw.model.exceptions.faithtrack.IllegalMovesException;
import it.polimi.ingsw.model.exceptions.game.GameException;
import it.polimi.ingsw.model.exceptions.game.movesexception.MainActionDoneException;
import it.polimi.ingsw.model.exceptions.game.movesexception.NotHisTurnException;
import it.polimi.ingsw.model.exceptions.game.movesexception.TurnStartedException;
import it.polimi.ingsw.model.exceptions.requisite.NoRequisiteException;
import it.polimi.ingsw.model.exceptions.tray.OutOfBoundMarketTrayException;
import it.polimi.ingsw.model.exceptions.warehouse.UnobtainableResourceException;
import it.polimi.ingsw.model.exceptions.warehouse.WrongPointsException;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
import it.polimi.ingsw.model.match.markettray.RowCol;
import it.polimi.ingsw.model.match.match.Match;
import it.polimi.ingsw.model.match.match.MultiplayerMatch;
import it.polimi.ingsw.model.match.match.SingleplayerMatch;
import it.polimi.ingsw.model.player.Lorenzo;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerAction;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MatchTest {

    /**
     * create a single player match, a lorenzo player and some other test player.
     * Then try to do some operation to the match and with assertion check if the return value of it is right,
     * so you can know if the operation is succeed of failed.
     */
    @Test
    public void buildSingleplayerTest() throws NotHisTurnException, MainActionDoneException, OutOfBoundMarketTrayException, GameException, IllegalMovesException, TurnStartedException, WrongPointsException, EmptyDeckException, UnobtainableResourceException, IllegalTypeInProduction {
        Match sp = new SingleplayerMatch();

        Lorenzo lorenzo = new Lorenzo(sp);
        Player dummy1 = new Player("Dobby", sp);
        Player dummy2 = new Player("Bobby", sp);

        assertFalse(sp.playerJoin(lorenzo));
        assertFalse(sp.startGame());
        assertTrue(sp.playerJoin(dummy1));
        assertFalse(sp.startGame());
        assertTrue(sp.playerJoin(lorenzo));
        assertFalse(sp.playerJoin(dummy2));
        assertFalse(sp.playerJoin(dummy1));

        assertFalse(dummy1.canDoStuff());

        assertTrue(sp.startGame());
        assertFalse(sp.playerJoin(dummy2));

        assertFalse(lorenzo.canDoStuff());
        assertTrue(dummy1.canDoStuff());

        // i need to use player action to simulate the server that call player action methods
        PlayerAction lore = lorenzo;
        PlayerAction player = dummy1;

        try {
            assertTrue(player.buyDevCard(LevelDevCard.LEVEL1, ColorDevCard.BLUE, DevCardSlot.CENTER));

        } catch(NoRequisiteException | IndexOutOfBoundsException | NotHisTurnException | MainActionDoneException e1){
            e1.printStackTrace();
        }
        //TODO change to assertTrue when devSetup in match is completed

        assertTrue(player.useMarketTray(RowCol.COL, 2));

        assertTrue(player.endThisTurn());

        // must be false because lorenzo is manage automatically
        assertFalse(lorenzo.canDoStuff());

        assertTrue(player.canDoStuff());

        assertTrue(player.endThisTurn());
        assertTrue(player.endThisTurn());
        assertTrue(player.endThisTurn());
        assertTrue(player.endThisTurn());
        assertTrue(player.endThisTurn());
    }

    /**
     * create a single player match, a lorenzo player and some other test player.
     * Then try to do some operation to the match and with assertion check if the return value of it is right,
     * so you can know if the operation is succeed of failed.
     */
    @Test
    public void buildMultiplayerTest() throws NotHisTurnException, MainActionDoneException, OutOfBoundMarketTrayException, GameException, IllegalMovesException, TurnStartedException, WrongPointsException, EmptyDeckException, UnobtainableResourceException, IllegalTypeInProduction {
        Match match = new MultiplayerMatch();

        Player p1 = new Player("gino", match);
        Player p2 = new Player("pino", match);
        Player p3 = new Player("dino", match);
        Player p4 = new Player("lino", match);

        Player looser = new Player("looser", match);

        assertTrue(match.playerJoin(p1));
        assertFalse(match.playerJoin(p1));

        assertTrue(match.playerJoin(p2));
        assertTrue(match.playerJoin(p3));
        assertTrue(match.playerJoin(p4));

        assertFalse(match.playerJoin(looser));

        assertTrue(match.startGame());
        assertFalse(match.startGame());

        List<PlayerAction> order = new LinkedList<>();

        order.add(p1);
        order.add(p2);
        order.add(p3);
        order.add(p4);

        for(int i = 0; i < 5; i++) {
            for (PlayerAction x : order) {
                assertEquals(x.canDoStuff(), x.useMarketTray(RowCol.COL, 2));
                if (x.canDoStuff()) {
                    try {
                        assertFalse(x.activateProductions());
                    } catch (NotHisTurnException e) {
                        e.printStackTrace();
                    } catch (MainActionDoneException e) {
                        e.printStackTrace();
                    }
                    x.endThisTurn();
                }
            }
        }
    }

    @Test
    public void CreateTokensTest() throws MissingCardException, EmptyDeckException {
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
    public void DevSetup(){
        Match match = new MultiplayerMatch();
        //TODO in attesa del cambio nel costruttore del match
    }
}
