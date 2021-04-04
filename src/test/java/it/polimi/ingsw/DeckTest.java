package it.polimi.ingsw;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.cards.effects.DestroyCards;
import it.polimi.ingsw.model.exceptions.MissingCardException;
import it.polimi.ingsw.model.requisite.Requisite;
import it.polimi.ingsw.model.requisite.ResourceRequisite;
import it.polimi.ingsw.model.resource.Coin;
import it.polimi.ingsw.model.resource.Resource;
import it.polimi.ingsw.model.resource.Servant;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test collector for decks and cards
 */
public class DeckTest {

    @Test
    void addCards() {
        int n = 0;
        Deck d = new Deck();
        List<Requisite> r = new ArrayList<Requisite>();
        Resource res = new Servant(3);
        Requisite one = new ResourceRequisite(res);
        r.add(one);
        Card c = new DevCard("000", new DestroyCards(),10, LevelDevCard.LEVEL1, ColorDevCard.GREEN, r);
        d.insertCard(c);
        assertEquals(1,d.getNumberOfCards());
    }

    @Test
    void addnCards(){
        List<Requisite> r = new ArrayList<Requisite>();
        Resource res = new Servant(3);
        Requisite one = new ResourceRequisite(res);
        r.add(one);
        Card c1 = new DevCard("000", new DestroyCards(),10, LevelDevCard.LEVEL1, ColorDevCard.GREEN, r);
        Card c2 = new DevCard("001", new DestroyCards(),5, LevelDevCard.LEVEL2, ColorDevCard.YELLOW, r);
        List<Card> cards = new ArrayList<Card>();
        cards.add(c1);
        cards.add(c2);
        Deck d = new Deck(cards);
        assertEquals(2,d.getNumberOfCards());
    }

    @Test
    void VP() {
        List<Requisite> req = new ArrayList<Requisite>();
        Resource res = new Coin(1);
        Requisite one = new ResourceRequisite(res);
        req.add(one);
        DevCard c1 = new DevCard("000", new DestroyCards(), 10, LevelDevCard.LEVEL1, ColorDevCard.GREEN, req);
        DevCard c2 = new DevCard("001",  new DestroyCards(),5, LevelDevCard.LEVEL2, ColorDevCard.YELLOW, req);
        SoloActionToken t1 = new SoloActionToken("002");

        List<DevCard> cards = new ArrayList<>();

        cards.add(c1);
        Deck<DevCard> d1 = new Deck<>(cards);

        d1.peekFirstCard();

        d1.insertCard(c2);
        d1.peekFirstCard();

        System.out.println(d1.toString());
        Deck<DevCard> d2 = new Deck<>(c2);

        try {
           d1.peekCard("001");
        }
        catch(MissingCardException e){
        }
        finally{
        }
    }
}
