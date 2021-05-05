package it.polimi.ingsw;

import it.polimi.ingsw.view.cli.FaithTrackPrinter;
import it.polimi.ingsw.view.cli.NamePrinter;
import it.polimi.ingsw.communication.packet.HeaderTypes;
import it.polimi.ingsw.communication.packet.commands.SetNumberCommand;
import it.polimi.ingsw.communication.server.ClientController;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.match.match.Match;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.personalBoard.warehouse.depot.DepotSlot;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.view.litemodel.LiteFaithTrack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class AsciiArtTest {

    private Model model;
    private Match multiplayer;

    ClientController pino = new ClientController(null, "TheDarkVinz");
    ClientController gino = new ClientController(null, "MadCini");
    ClientController dino = new ClientController(null, "Gino");
    ClientController tino = new ClientController(null, "LastBuddy");

    List<Player> order = new ArrayList<>();

    @BeforeEach
    public void initializeMatch() {

        model = new Model();
        assertDoesNotThrow(()->model.start(pino));
        model.handleClientCommand(pino, new SetNumberCommand(4));
        assertTrue(model.connectController(gino));
        assertTrue(model.connectController(dino));
        assertTrue(model.connectController(tino));

        assertNotNull(model.getMatch());
        this.multiplayer = model.getMatch();

        // initializing player section
        /*
        1st - 0 resource to choose and 0 faith points
        2nd - 1 resource to choose and 0 faith points
        3rd - 1 resource to choose and 1 faith points
        4th - 2 resource to choose and 1 faith points
         */

        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().test_discardLeader());
        this.order.add(multiplayer.test_getCurrPlayer());
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().endThisTurn());

        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().chooseResource(DepotSlot.BOTTOM, ResourceType.COIN));
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(() -> assertEquals(multiplayer.test_getCurrPlayer().test_getPB().test_getDepots().get(DepotSlot.BOTTOM).viewResources().get(0), ResourceBuilder.buildCoin()));
        this.order.add(multiplayer.test_getCurrPlayer());
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().endThisTurn());

        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().chooseResource(DepotSlot.BOTTOM, ResourceType.COIN));
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().test_discardLeader());
        assertEquals(1, multiplayer.test_getCurrPlayer().test_getPB().getFT_forTest().getPlayerPosition());
        assertDoesNotThrow(() -> assertEquals(multiplayer.test_getCurrPlayer().test_getPB().test_getDepots().get(DepotSlot.BOTTOM).viewResources().get(0), ResourceBuilder.buildCoin()));
        this.order.add(multiplayer.test_getCurrPlayer());
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().endThisTurn());

        assertDoesNotThrow(()-> assertEquals(HeaderTypes.OK, multiplayer.test_getCurrPlayer().chooseResource(DepotSlot.BOTTOM, ResourceType.COIN).header));
        assertDoesNotThrow(()-> assertEquals(HeaderTypes.INVALID, multiplayer.test_getCurrPlayer().chooseResource(DepotSlot.BOTTOM, ResourceType.STONE).header));
        assertDoesNotThrow(()-> assertEquals(HeaderTypes.OK, multiplayer.test_getCurrPlayer().chooseResource(DepotSlot.MIDDLE, ResourceType.STONE).header));
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().test_discardLeader());
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().test_discardLeader());
        assertEquals(1, multiplayer.test_getCurrPlayer().test_getPB().getFT_forTest().getPlayerPosition());
        assertDoesNotThrow(() -> assertEquals(multiplayer.test_getCurrPlayer().test_getPB().test_getDepots().get(DepotSlot.BOTTOM).viewResources().get(0), ResourceBuilder.buildCoin()));
        assertDoesNotThrow(() -> assertEquals(multiplayer.test_getCurrPlayer().test_getPB().test_getDepots().get(DepotSlot.MIDDLE).viewResources().get(0), ResourceBuilder.buildStone()));
        this.order.add(multiplayer.test_getCurrPlayer());
        assertDoesNotThrow(()-> multiplayer.test_getCurrPlayer().endThisTurn());

        // creating a list of the players in order to have player(0) = inkwell player
        Collections.rotate(order, -order.indexOf(multiplayer.test_getTurn().getCurPlayer()));
    }

    @Test
    public void PrintName(){
        NamePrinter test = new NamePrinter();
        //test.titleName();
    }


    @Test
    public void PrintFaithTrack() throws EndGameException, IOException {
        LiteFaithTrack faith = new LiteFaithTrack();
        FaithTrackPrinter test = new FaithTrackPrinter(faith);

        List<Player> players = new ArrayList<>();
        players.addAll(order);
        List<String> nickname = new ArrayList<>();
        List<Integer> positions = new ArrayList<>();

        players.get(0).moveFaithMarker(10);
        players.get(1).moveFaithMarker(15);

        players.get(2).moveFaithMarker(10);
        players.get(3).moveFaithMarker(19);

        for (Player pla : players){
            nickname.add(pla.getNickname());
            positions.add(pla.getFT_forTest().getPlayerPosition());
        }

        //test.printFaithTrack(nickname, positions);
    }

    @Test
    public void printTrack(){
    }
}
