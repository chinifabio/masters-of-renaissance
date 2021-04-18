package it.polimi.ingsw.matchTests;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.cards.SoloActionToken;
import it.polimi.ingsw.model.exceptions.PlayerStateException;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;
import it.polimi.ingsw.model.exceptions.card.MissingCardException;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.exceptions.game.GameException;
import it.polimi.ingsw.model.exceptions.requisite.NoRequisiteException;
import it.polimi.ingsw.model.exceptions.tray.OutOfBoundMarketTrayException;
import it.polimi.ingsw.model.exceptions.warehouse.UnobtainableResourceException;
import it.polimi.ingsw.model.exceptions.warehouse.WrongPointsException;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
import it.polimi.ingsw.model.match.markettray.RowCol;
import it.polimi.ingsw.model.match.match.Match;
import it.polimi.ingsw.model.match.match.MultiplayerMatch;
import it.polimi.ingsw.model.match.match.SingleplayerMatch;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerAction;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MatchTest {

    /**
     * create a single player match, a lorenzo player and some other test player.
     * Then try to do some operation to the match and with assertion check if the return value of it is right,
     * so you can know if the operation is succeed of failed.
     */
    @Test
    public void buildSingleplayerTest() throws OutOfBoundMarketTrayException, EndGameException,  UnobtainableResourceException, IllegalTypeInProduction, PlayerStateException {
        Match sp = new SingleplayerMatch();

        Player dummy1 = new Player("Dobby", sp);
        Player dummy2 = new Player("Bobby", sp);

        assertFalse(sp.startGame());
        assertTrue(sp.playerJoin(dummy1));
        assertFalse(sp.playerJoin(dummy2));
        assertTrue(sp.startGame());
        assertFalse(sp.playerJoin(dummy1));

        assertTrue(dummy1.canDoStuff());

        assertFalse(sp.startGame());
        assertFalse(sp.playerJoin(dummy2));

        assertTrue(dummy1.canDoStuff());

        PlayerAction player = dummy1;

        dummy1.discardLeader_test();
        dummy1.discardLeader_test();

        try {
            assertTrue(player.buyDevCard(LevelDevCard.LEVEL1, ColorDevCard.BLUE, DevCardSlot.CENTER));

        } catch(NoRequisiteException e1){
            System.out.println("problema, manca il requisite della carta");
        } catch(IndexOutOfBoundsException e2){
            System.out.println("Pescata da un mazzo vuoto!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //TODO change to assertTrue when devSetup in match is completed

        assertTrue(player.useMarketTray(RowCol.COL, 2));

        assertTrue(player.endThisTurn());

        assertTrue(dummy1.canDoStuff());

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
    public void buildMultiplayerTest() throws OutOfBoundMarketTrayException, EndGameException, UnobtainableResourceException, IllegalTypeInProduction, PlayerStateException {
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

        List<Player> order = new LinkedList<>();

        order.add(p1);
        order.add(p2);
        order.add(p3);
        order.add(p4);

        Collections.rotate(order, -order.indexOf(order.stream().filter(Player::canDoStuff).findAny().get()));

        order.get(0).discardLeader_test();
        order.get(0).discardLeader_test();

        order.get(1).discardLeader_test();
        order.get(1).discardLeader_test();

        order.get(2).discardLeader_test();
        order.get(2).discardLeader_test();

        order.get(3).discardLeader_test();
        order.get(3).discardLeader_test();

        assertTrue(order.get(0).canDoStuff());

        assertTrue(order.get(0).useMarketTray(RowCol.COL, 2));
        assertFalse(order.get(0).useMarketTray(RowCol.COL, 2));
        assertTrue(order.get(0).endThisTurn());

        assertTrue(order.get(1).useMarketTray(RowCol.COL, 2));
        assertFalse(order.get(1).useMarketTray(RowCol.COL, 2));
        assertTrue(order.get(1).endThisTurn());

        assertTrue(order.get(2).useMarketTray(RowCol.COL, 2));
        assertFalse(order.get(2).useMarketTray(RowCol.COL, 2));
        assertTrue(order.get(2).endThisTurn());

        assertTrue(order.get(3).useMarketTray(RowCol.COL, 2));
        assertFalse(order.get(3).useMarketTray(RowCol.COL, 2));
        assertTrue(order.get(3).endThisTurn());
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
