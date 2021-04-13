package it.polimi.ingsw;

import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.DevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.cards.effects.AddProductionEffect;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.personalBoard.PersonalBoard;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.Production;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.ProductionID;
import it.polimi.ingsw.model.requisite.Requisite;
import it.polimi.ingsw.model.requisite.ResourceRequisite;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.ResourceBuilder;
import it.polimi.ingsw.model.resource.ResourceType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test collector for cards effects.
 */
public class EffectTest {

    /**
     *
     */
    @Test
    void addProduction() {
        //       String ID = "000";
        //       Resource unknown = ResourceBuilder.buildUnknown();
        //       List<ResourceRequisite> requisiteEffect = new ArrayList<>();
        //       ResourceRequisite req1 = new ResourceRequisite(unknown);
        //       requisiteEffect.add(req1);
        //       requisiteEffect.add(req1);
        //       List<ResourceRequisite> outputProd = new ArrayList<>();
        //       outputProd.add(req1);
//
        //       Production p = new Production(ProductionID.BASIC,requisiteEffect,outputProd);
//
        //       List<Requisite> cardRequisite = new ArrayList<>();
        //       Resource twoCoin = ResourceBuilder.buildCoin(2);
        //       ResourceRequisite cardReq = new ResourceRequisite(twoCoin);
        //       cardRequisite.add(cardReq);
//
        //       DevCard dc = new DevCard(ID, new AddProductionEffect(p), 3, LevelDevCard.LEVEL3, ColorDevCard.BLUE,cardRequisite);
//
        //   }
//
        //   void addDepot(){
        //       String ID = "000";
//
        //       List<Requisite> cardRequisite = new ArrayList<>();
        //       Resource twoCoin = ResourceBuilder.buildCoin(4);
        //       ResourceRequisite cardReq = new ResourceRequisite(twoCoin);
        //       cardRequisite.add(cardReq);
//
        //       DevCard dc = new DevCard(ID, new AddDepot(ResourceType.COIN), 3, LevelDevCard.LEVEL3, ColorDevCard.BLUE,cardRequisite);
//
        //       //dc.useEffect();
        //   }
    }
}