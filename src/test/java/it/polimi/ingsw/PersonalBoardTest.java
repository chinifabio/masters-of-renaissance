package it.polimi.ingsw;


import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.DevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.exceptions.EmptyDeckException;
import it.polimi.ingsw.model.player.personalBoard.DevCardSlot;
import it.polimi.ingsw.model.player.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.Production;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.model.requisite.Requisite;
import it.polimi.ingsw.model.requisite.ResourceRequisite;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * test collectors for PersonalBoard
 */
public class PersonalBoardTest {

    /**
     * This test creates a PersonalBoard and add
     */
    @Test
    void DevCards() {
        //    List<Requisite> req = new ArrayList<>();
        //    Resource coin = ResourceBuilder.buildCoin(2);
        //    ResourceRequisite rr = new ResourceRequisite(coin);
        //    req.add(rr);
        //    List<ResourceRequisite> sample = new ArrayList<>();
        //    Production p = new Production(ProductionID.BASIC,sample,sample);
//
        //    DevCard c1 = new DevCard("000", new AddProduction(p), 2, LevelDevCard.LEVEL1, ColorDevCard.GREEN,req);
        //    DevCard c2 = new DevCard("111", new AddProduction(p), 4, LevelDevCard.LEVEL2, ColorDevCard.YELLOW,req);
//
        //    DevCardSlot dcsLeft = DevCardSlot.LEFT;
        //    DevCardSlot dcsCenter = DevCardSlot.CENTER;
        //    DevCardSlot dcsRight = DevCardSlot.RIGHT;
//
        //    PersonalBoard personalBoard = new PersonalBoard();
//
        //    if(personalBoard.addDevCard(dcsLeft,c1)){
        //        assertTrue(c1.equals(personalBoard.viewDevCards().get(dcsLeft)));
        //    }
        //    else{
//
        //    }
//
        //    if(personalBoard.addDevCard(dcsLeft,c2)){
        //        assertTrue(c2.equals(personalBoard.viewDevCards().get(dcsLeft)));
        //    }
        //    else{
        //        fail();
        //    }
//
        //    if(personalBoard.addDevCard(dcsCenter,c2)){
        //        fail();
        //    }
        //    else{
        //        try {
        //            assertTrue(personalBoard.viewDevCards().get(dcsCenter).equals(null));
        //            fail();
        //        }catch (NullPointerException e){
        //            System.out.println("Sto cercando di guardare una carta che non esiste!");
        //        }
        //    }
        //}

    }
}