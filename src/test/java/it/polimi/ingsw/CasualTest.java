package it.polimi.ingsw;

import it.polimi.ingsw.litemodel.Scoreboard;
import it.polimi.ingsw.litemodel.litecards.LiteSoloActionToken;
import it.polimi.ingsw.model.VirtualView;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.SoloActionToken;
import it.polimi.ingsw.model.cards.effects.DestroyCardsEffect;
import it.polimi.ingsw.model.cards.effects.MoveTwoEffect;
import it.polimi.ingsw.model.cards.effects.ShuffleMoveOneEffect;
import it.polimi.ingsw.model.exceptions.requisite.LootTypeException;
import it.polimi.ingsw.model.exceptions.tray.UnpaintableMarbleException;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalNormalProduction;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.Marble;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.MarbleColor;
import it.polimi.ingsw.model.match.match.Match;
import it.polimi.ingsw.model.match.match.MultiplayerMatch;
import it.polimi.ingsw.model.match.match.SingleplayerMatch;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.Normal;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.PopeSpace;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.NormalProduction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionRecord;
import it.polimi.ingsw.model.requisite.ColorCardRequisite;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.view.cli.Colors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CasualTest {
    Player player1;
    Player player2;

    VirtualView view = new VirtualView();
    Match game;

    List<Player> order = new ArrayList<>();

    @BeforeEach
    public void initialization() {
        try {
            game = new MultiplayerMatch(2, view);
        } catch (IOException e) {
            fail(e.getMessage());
        }

        assertDoesNotThrow(() -> player1 = new Player("gino", game, view));
        assertTrue(game.playerJoin(player1));
        order.add(player1);

        assertDoesNotThrow(() -> player2 = new Player("pino", game, view));
        assertTrue(game.playerJoin(player2));
        order.add(player2);

        game.initialize();
        Collections.rotate(order, order.indexOf(game.currentPlayer()));
        assertTrue(game.isGameOnAir());

        assertDoesNotThrow(() -> order.get(0).test_discardLeader());
        assertDoesNotThrow(() -> order.get(0).test_discardLeader());
        order.get(0).endThisTurn();

        order.get(1).chooseResource(DepotSlot.BOTTOM, ResourceType.COIN);
        assertDoesNotThrow(() -> order.get(1).test_discardLeader());
        assertDoesNotThrow(() -> order.get(1).test_discardLeader());
        assertDoesNotThrow(() -> assertEquals(order.get(1).test_getPB().getDepots().get(DepotSlot.BOTTOM).viewResources().get(0), ResourceBuilder.buildCoin()));
        order.get(1).endThisTurn();
    }

    @Test
    public void Marbles(){
        Marble marble = new Marble(MarbleColor.BLUE,ResourceType.SHIELD);
        assertFalse(marble.isPaintable());
        try{
            marble.paint(new Marble(MarbleColor.RED, ResourceType.FAITHPOINT));
            fail();
        } catch (UnpaintableMarbleException e) {
            //se entra qui è corretto
        }
        assertEquals(Colors.BLUE_BRIGHT,marble.color().toColor());
        assertEquals("BLUE", marble.color().toString());
    }

    @Test
    public void FaithTrack(){
        Normal norm = new Normal();
        assertFalse(norm.isPopeSpace());
        PopeSpace ps = new PopeSpace();
        assertTrue(ps.isPopeSpace());
    }

    @Test
    public void Productions() throws IllegalTypeInProduction {
        final NormalProduction np = assertDoesNotThrow(() -> new NormalProduction(Collections.singletonList(ResourceBuilder.buildCoin()), Collections.singletonList(ResourceBuilder.buildShield())));
        try {
            np.setNormalProduction(new NormalProduction(Collections.singletonList(ResourceBuilder.buildCoin()), Collections.singletonList(ResourceBuilder.buildShield())));
            fail();
        } catch (IllegalNormalProduction e) {
            //se viene qui è corretto
        }

        ProductionRecord pr = new ProductionRecord(DepotSlot.BUFFER, ProductionID.BASIC,ResourceBuilder.buildCoin());
        assertEquals(pr.getDest(),ProductionID.BASIC);
    }

    @Test
    public void Requisite(){
        ColorCardRequisite ccr = new ColorCardRequisite(ColorDevCard.BLUE,1);
        try {
            ccr.getType();
            fail();
        } catch (LootTypeException e) {
            // qui è dove deve andare
        }
        try {
            ccr.getLevel();
            fail();
        } catch (LootTypeException e) {
            // qui è dove deve andare
        }
    }

    @Test
    public void Cards(){
        SoloActionToken sat = new SoloActionToken("111", new MoveTwoEffect());
        LiteSoloActionToken lsat = sat.liteVersion();
        assertEquals(lsat.getCardID(),sat.getCardID());

        SoloActionToken sat1 = new SoloActionToken("112", new DestroyCardsEffect(ColorDevCard.GREEN));
        LiteSoloActionToken lsat1 = sat1.liteVersion();
        assertEquals(lsat1.getCardID(),sat1.getCardID());

        SoloActionToken sat2 = new SoloActionToken("112", new ShuffleMoveOneEffect());
        LiteSoloActionToken lsat2 = sat2.liteVersion();
        assertEquals(lsat2.getCardID(),sat2.getCardID());

    }

    @Test
    public void Matches(){
        assertEquals(2,game.playerInGame());
        assertTrue(game.nicknames().contains("gino"));
        assertTrue(game.nicknames().contains("pino"));
        assertFalse(game.nicknames().contains("vino"));
        Player p = game.currentPlayer();
        game.disconnectPlayer(game.currentPlayer());
        game.reconnectPlayer(p.getNickname());

        Scoreboard sb = game.winnerCalculator();
        assertEquals(0,sb.getBoard().get(0).getScore());




        try {
            game = new SingleplayerMatch(view);
        } catch (IOException e) {
            fail(e.getMessage());
        }

        assertEquals(0,game.playerInGame());
        Player player3  = assertDoesNotThrow(() -> new Player("vino", game, view));
        assertTrue(game.playerJoin(player3));
        assertEquals(1,game.playerInGame());

        game.initialize();
        assertTrue(game.isGameOnAir());

        assertDoesNotThrow(player3::test_discardLeader);
        assertDoesNotThrow(player3::test_discardLeader);
        player3.endThisTurn();

        assertTrue(game.disconnectPlayer(player3));
        game.reconnectPlayer(player3.getNickname());

        assertEquals("vino",game.nicknames().get(0));

        player3.fpCheat(25);
        sb = game.winnerCalculator();
        assertEquals(29,sb.getBoard().get(0).getScore());
    }

    @Test
    public void Exceptions() throws IllegalTypeInProduction {

    }
}
