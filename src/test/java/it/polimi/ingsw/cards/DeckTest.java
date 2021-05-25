package it.polimi.ingsw.cards;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.cards.effects.AddProductionEffect;
import it.polimi.ingsw.model.exceptions.card.AlreadyInDeckException;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;
import it.polimi.ingsw.model.exceptions.card.MissingCardException;
import it.polimi.ingsw.model.exceptions.warehouse.production.IllegalTypeInProduction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.NormalProduction;
import it.polimi.ingsw.model.player.personalBoard.warehouse.production.Production;
import it.polimi.ingsw.model.requisite.Requisite;
import it.polimi.ingsw.model.requisite.ResourceRequisite;
import it.polimi.ingsw.model.resource.*;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * test collector for cards, decks and DevSetup.
 */
public class DeckTest {

    /**
     * This test creates a series of different cards and checks their CardID.
     */
    @Test
    void cardIDCheck(){
        String ID = "105", ID1 = "120",ID2 = "145";

        List<Requisite> requisite = new ArrayList<>();
        Resource coin = ResourceBuilder.buildCoin(2);
        ResourceRequisite rr = new ResourceRequisite(coin);
        requisite.add(rr);


        DevCard c1 = new DevCard(ID, new AddProductionEffect(null), 3, LevelDevCard.LEVEL3, ColorDevCard.BLUE,requisite);
        DevCard c3 = new DevCard("100", new AddProductionEffect(null), 3, LevelDevCard.LEVEL3, ColorDevCard.BLUE,requisite);
        LeaderCard c2 = new LeaderCard(ID1, new AddProductionEffect(null),6,requisite);
        SoloActionToken token = new SoloActionToken(ID2, new AddProductionEffect(null));

        assertEquals(ID,c1.getCardID());
        assertEquals(ID1,c2.getCardID());
        assertEquals(ID2,token.getCardID());
        assertNotEquals(c3, c1);
    }

    /**
     * This test creates a DevCard and checks the victory point of the DevCard.
     */
    @Test
    void victoryPointDevCard(){
        int n = 7;

        List<Requisite> req = new ArrayList<>();
        Resource coin = ResourceBuilder.buildCoin(2);
        ResourceRequisite rr = new ResourceRequisite(coin);
        req.add(rr);

        DevCard c1 = new DevCard("000", new AddProductionEffect(null), n, LevelDevCard.LEVEL3, ColorDevCard.BLUE,req);

        assertEquals(n,c1.getVictoryPoint());
    }

    /**
     * This test creates a DevCard and checks its color and level.
     */
    @Test
    void colorLevelDevCard(){
        LevelDevCard lev = LevelDevCard.LEVEL2;
        ColorDevCard col = ColorDevCard.BLUE;
        List<Requisite> req = new ArrayList<>();
        Resource coin = ResourceBuilder.buildCoin(2);
        ResourceRequisite rr = new ResourceRequisite(coin);
        req.add(rr);


        DevCard c1 = new DevCard("000", new AddProductionEffect(null), 2, lev, col,req);

        assertEquals(lev,c1.getLevel());
        assertEquals(col,c1.getColor());
    }

    /**
     * This test creates a LeaderCard and checks its victory points.
     */
    @Test
    void victoryPointLeaderCard(){
        int n = 12;
        List<Requisite> req = new ArrayList<>();
        Resource coin = ResourceBuilder.buildCoin(2);
        ResourceRequisite rr = new ResourceRequisite(coin);
        req.add(rr);

        LeaderCard c = new LeaderCard("010", new AddProductionEffect(null), n, req);

        assertEquals(n,c.getVictoryPoint());
    }

    /**
     * This test creates a LeaderCard and checks its activated parameter before and after its activation.
     */
    @Test
    void activatedLeaderCard(){
        List<Requisite> req = new ArrayList<>();
        Resource coin = ResourceBuilder.buildCoin(2);
        ResourceRequisite rr = new ResourceRequisite(coin);
        req.add(rr);

        LeaderCard c = new LeaderCard("010", new AddProductionEffect(null), 6, req);

        assertFalse(c.isActivated());
        c.activate();
        assertTrue(c.isActivated());
        c.activate();
        assertTrue(c.isActivated());
    }

    /**
     * This test creates two cards and checks their requirements.
     */
    @Test
    void CardCost(){
        List<Requisite> req1 = new ArrayList<>();
        List<Requisite> req2 = new ArrayList<>();
        Resource twoCoin = ResourceBuilder.buildCoin(2);
        Resource oneCoin = ResourceBuilder.buildCoin(1);
        ResourceRequisite rr1 = new ResourceRequisite(oneCoin);
        ResourceRequisite rr2 = new ResourceRequisite(twoCoin);
        req1.add(rr1);
        req2.add(rr2);

        LeaderCard c1 = new LeaderCard("010", new AddProductionEffect(null), 6, req1);
        DevCard c2 = new DevCard("000", new AddProductionEffect(null),10, LevelDevCard.LEVEL1, ColorDevCard.GREEN, req2);

        assertEquals(req1,c1.getCost());
        assertNotEquals(req2,c1.getCost());

        assertEquals(req2,c2.getCost());
        assertNotEquals(req1,c2.getCost());
    }

    /**
     * This test creates an empty deck and adds one card to it. It checks if the number of card is correct before and after adding the card.
     */
    @Test
    void insertCard() {
        Deck<DevCard> d = new Deck<>();

        List<Requisite> req = new ArrayList<>();
        Resource coin = ResourceBuilder.buildCoin(2);
        ResourceRequisite rr = new ResourceRequisite(coin);
        req.add(rr);

        assertEquals(0,d.getNumberOfCards());

        DevCard c1 = new DevCard("000", new AddProductionEffect(null),10, LevelDevCard.LEVEL1, ColorDevCard.GREEN, req);
        DevCard c2 = new DevCard("000", new AddProductionEffect(null),5, LevelDevCard.LEVEL2, ColorDevCard.GREEN, req);
        DevCard c3 = new DevCard("111", new AddProductionEffect(null),5, LevelDevCard.LEVEL2, ColorDevCard.GREEN, req);

        assertDoesNotThrow(()->d.insertCard(c1));
        try {
            d.insertCard(c2);
        } catch (AlreadyInDeckException ignore) {}
        assertDoesNotThrow(()->d.insertCard(c3));

        assertEquals(2,d.getNumberOfCards());
    }

    /**
     * This test creates a deck and passes to it a List of Card. It insert another list of Card and checks then the number of cards that the deck contains.
     */
    @Test
    void addListOfCards(){
        int n=2;

        List<Requisite> req = new ArrayList<>();
        Resource coin = ResourceBuilder.buildCoin(2);
        ResourceRequisite rr = new ResourceRequisite(coin);
        req.add(rr);

        Card c1 = new DevCard("000", new AddProductionEffect(null),10, LevelDevCard.LEVEL1, ColorDevCard.GREEN,req);
        Card c2 = new DevCard("001", new AddProductionEffect(null),5, LevelDevCard.LEVEL2, ColorDevCard.YELLOW,req);

        List<Card> cards = new ArrayList<>();
        cards.add(c1);
        cards.add(c2);
        Deck<Card> d = new Deck<>();

        for(int i=0;i<n;i++){
            int finalI = i;
            assertDoesNotThrow(()->d.insertCard(cards.get(finalI)));
        }

        assertEquals(n,d.getNumberOfCards());
    }

    /**
     * This test creates a deck and passes to it a single Card. It then add another two and check the number of card.
     */
    @Test
    void addOneInsertCard() {
        int n=3;

        List<Requisite> req = new ArrayList<>();
        Resource coin = ResourceBuilder.buildCoin(2);
        ResourceRequisite rr = new ResourceRequisite(coin);
        req.add(rr);

        DevCard c1 = new DevCard("000", new AddProductionEffect(null), 10, LevelDevCard.LEVEL3, ColorDevCard.GREEN,req);
        DevCard c2 = new DevCard("000",  new AddProductionEffect(null),5, LevelDevCard.LEVEL2, ColorDevCard.YELLOW,req);
        DevCard c3 = new DevCard("002",  new AddProductionEffect(null),2, LevelDevCard.LEVEL1, ColorDevCard.PURPLE,req);

        List<DevCard> cards = new ArrayList<>();
        cards.add(c2);
        cards.add(c3);

        Deck<DevCard> d1 = new Deck<>(c1);

        for(int i=0;i<n-1;i++){
            try {
                d1.insertCard(cards.get(i));
            } catch (AlreadyInDeckException ignore){}
        }

        assertEquals(n-1,d1.getNumberOfCards());
    }

    /**
     * This test creates two decks, one empty and one with 3 cards. It checks if the method draw throws an exception when called by an empty deck.
     * It then checks if the number of cards of the second deck are updated once one is drawn and if that card is the one on top.
     */
    @Test
    void drawFromDeck(){
        List<Requisite> req = new ArrayList<>();
        Resource coin = ResourceBuilder.buildCoin(2);
        ResourceRequisite rr = new ResourceRequisite(coin);
        req.add(rr);

        DevCard c1 = new DevCard("000", new AddProductionEffect(null), 10, LevelDevCard.LEVEL3, ColorDevCard.GREEN,req);
        DevCard c2 = new DevCard("001",  new AddProductionEffect(null),5, LevelDevCard.LEVEL2, ColorDevCard.YELLOW,req);
        DevCard c3 = new DevCard("002",  new AddProductionEffect(null),2, LevelDevCard.LEVEL1, ColorDevCard.PURPLE,req);
        List<DevCard> cards = new ArrayList<>();
        cards.add(c1);
        cards.add(c2);
        cards.add(c3);
        Deck<Card> d1 = new Deck<>();
        Deck<DevCard> d2 = new Deck<>();

        for(int i=0;i<3;i++) {
            int finalI = i;
            assertDoesNotThrow(()->d2.insertCard(cards.get(finalI)));
        }

        assertEquals(0,d1.getNumberOfCards());
        assertEquals(3,d2.getNumberOfCards());
        try {
            d1.draw();
            fail();
        } catch (EmptyDeckException ignore) {}
        try {
            DevCard cTop = d2.draw();
            assertEquals(2,d2.getNumberOfCards());
            assertEquals("002",cTop.getCardID());
        } catch (EmptyDeckException e) {
            fail();
        }
    }

    /**
     * This test creates two decks, one empty and one with 3 cards. It checks if the method discard throws an exception when called by an empty deck.
     * It then checks if the number of cards of the second deck are updated once one is discarded.
     */
    @Test
    void discardCards(){
        List<Requisite> req = new ArrayList<>();
        Resource coin = ResourceBuilder.buildCoin(2);
        ResourceRequisite rr = new ResourceRequisite(coin);
        req.add(rr);

        DevCard c1 = new DevCard("100", new AddProductionEffect(null), 10, LevelDevCard.LEVEL3, ColorDevCard.GREEN,req);
        DevCard c2 = new DevCard("001",  new AddProductionEffect(null),5, LevelDevCard.LEVEL2, ColorDevCard.YELLOW,req);
        DevCard c3 = new DevCard("002",  new AddProductionEffect(null),2, LevelDevCard.LEVEL1, ColorDevCard.PURPLE,req);
        List<DevCard> cards = new ArrayList<>();
        cards.add(c1);
        cards.add(c2);
        cards.add(c3);
        Deck<Card> d1 = new Deck<>();
        Deck<DevCard> d2 = new Deck<>();
        for(int i=0;i<3;i++) {
            int finalI = i;
            assertDoesNotThrow(()->d2.insertCard(cards.get(finalI)));
        }
        assertEquals(0,d1.getNumberOfCards());
        assertEquals(3,d2.getNumberOfCards());
        try {
            d1.discard();
            fail();
        } catch (EmptyDeckException ignore) {}
        try {
            d2.discard();
            assertEquals(2,d2.getNumberOfCards());
        } catch (EmptyDeckException e) {
            fail();
        }
    }

    /**
     * This test creates a deck and tries to peek a card passing a cardID.
     */
    @Test
    void peekCardFromDeck(){
        List<Requisite> req = new ArrayList<>();
        Resource coin = ResourceBuilder.buildCoin(2);
        ResourceRequisite rr = new ResourceRequisite(coin);
        req.add(rr);
        String ID = "100", ID1 = "010",ID2 = "001";

        DevCard c1 = new DevCard(ID, new AddProductionEffect(null), 10, LevelDevCard.LEVEL3, ColorDevCard.GREEN,req);
        DevCard c2 = new DevCard(ID1, new AddProductionEffect(null),5, LevelDevCard.LEVEL2, ColorDevCard.YELLOW,req);
        DevCard c3 = new DevCard(ID2, new AddProductionEffect(null),2, LevelDevCard.LEVEL1, ColorDevCard.PURPLE,req);
        List<DevCard> cards = new ArrayList<>();
        cards.add(c1);
        cards.add(c2);
        cards.add(c3);
        Deck<DevCard> d = new Deck<>();

        for(int i=0;i<3;i++) {
            int finalI = i;
            assertDoesNotThrow(()->d.insertCard(cards.get(finalI)));
        }

        assertDoesNotThrow(()->assertEquals(ID2,d.peekFirstCard().getCardID()));

        try {
            d.peekCard("002");
            fail();
        } catch (MissingCardException ignore) {}

        assertDoesNotThrow(()->assertEquals(LevelDevCard.LEVEL3,d.peekCard(ID).getLevel()));
    }

    /**
     * This test creates a deck and shuffles it at the start. It then discard two cards, shuffle again and check if the size of the deck is 3.
     */
    @Test
    void shuffleDeck(){
        List<Requisite> req = new ArrayList<>();
        Resource coin = ResourceBuilder.buildCoin(2);
        ResourceRequisite rr = new ResourceRequisite(coin);
        req.add(rr);

        LeaderCard c1 = new LeaderCard("111", new AddProductionEffect(null), 1, req);
        LeaderCard c2 = new LeaderCard("222", new AddProductionEffect(null), 2, req);
        LeaderCard c3 = new LeaderCard("333", new AddProductionEffect(null), 3, req);
        LeaderCard c4 = new LeaderCard("444", new AddProductionEffect(null), 4, req);
        LeaderCard c5 = new LeaderCard("555", new AddProductionEffect(null), 5, req);
        LeaderCard c6 = new LeaderCard("666", new AddProductionEffect(null), 6, req);
        LeaderCard c7 = new LeaderCard("777", new AddProductionEffect(null), 7, req);
        Deck<LeaderCard> d = new Deck<>(c1);
        List<LeaderCard> cards = new ArrayList<>();
        cards.add(c2);
        cards.add(c3);
        cards.add(c4);
        cards.add(c5);
        cards.add(c6);
        cards.add(c7);
        for(int i=0;i<6;i++) {
            int finalI = i;
            assertDoesNotThrow(()->d.insertCard(cards.get(finalI)));
        }

        assertEquals(7,d.getNumberOfCards());
        d.shuffle();
        assertEquals(7,d.getNumberOfCards());
        try {
            d.discard();
            assertEquals(6,d.getNumberOfCards());
        } catch (EmptyDeckException e) {
            fail(e.getMessage());
        }
        try {
            d.discard();
            assertEquals(5,d.getNumberOfCards());
        } catch (EmptyDeckException e) {
            fail(e.getMessage());
        }
        try {
            d.discard();
            assertEquals(4,d.getNumberOfCards());
        } catch (EmptyDeckException e) {
            fail(e.getMessage());
        }
        try {
            d.discard();
            assertEquals(3,d.getNumberOfCards());
        } catch (EmptyDeckException e) {
            fail(e.getMessage());
        }
        d.shuffle();
        assertEquals(7,d.getNumberOfCards());
    }

    /**
     * This test creates two deck of devCards and checks if the method drawFromDecks works.
     */
    @Test
    void DevSetup() throws IllegalTypeInProduction {
        List<Resource> sample = new ArrayList<>();
        Production p = new NormalProduction(sample,sample);
        List<Requisite> req = new ArrayList<>();
        Resource coin = ResourceBuilder.buildCoin(2);
        ResourceRequisite rr = new ResourceRequisite(coin);
        req.add(rr);

        DevCard cg1 = new DevCard("111", new AddProductionEffect(p), 1, LevelDevCard.LEVEL3, ColorDevCard.GREEN,req);
        DevCard cg2 = new DevCard("222", new AddProductionEffect(p),2, LevelDevCard.LEVEL3, ColorDevCard.GREEN,req);
        DevCard cg3 = new DevCard("333", new AddProductionEffect(p),3, LevelDevCard.LEVEL3, ColorDevCard.GREEN,req);

        DevCard cy1 = new DevCard("110", new AddProductionEffect(p), 1, LevelDevCard.LEVEL1, ColorDevCard.YELLOW,req);
        DevCard cy2 = new DevCard("220", new AddProductionEffect(p),2, LevelDevCard.LEVEL1, ColorDevCard.YELLOW,req);
        DevCard cy3 = new DevCard("330", new AddProductionEffect(p),3, LevelDevCard.LEVEL1, ColorDevCard.YELLOW,req);

        Deck<DevCard> greenDeck = new Deck<>();
        Deck<DevCard> yellowDeck = new Deck<>();
        List<DevCard> cards1 = new ArrayList<>();
        List<DevCard> cards2 = new ArrayList<>();
        cards1.add(cg1);
        cards1.add(cg2);
        cards1.add(cg3);
        cards2.add(cy1);
        cards2.add(cy2);
        cards2.add(cy3);

        for(int i=0;i<3;i++) {
            int finalI = i;
            assertDoesNotThrow(()->greenDeck.insertCard(cards1.get(finalI)));
        }
        for(int i=0;i<3;i++) {
            int finalI = i;
            assertDoesNotThrow(()->yellowDeck.insertCard(cards2.get(finalI)));
        }

        List<Deck<DevCard>> deckList = new ArrayList<>();

        DevSetup empty = new DevSetup(deckList);

        assertNull(empty.showDevDeck(LevelDevCard.LEVEL3,ColorDevCard.GREEN));

        deckList.add(greenDeck);
        deckList.add(yellowDeck);

        DevSetup grid = new DevSetup(deckList);

        assertDoesNotThrow(()->grid.showDevDeck(LevelDevCard.LEVEL3,ColorDevCard.GREEN));
        assertDoesNotThrow(()->grid.drawFromDeck(LevelDevCard.LEVEL3,ColorDevCard.GREEN));
        assertDoesNotThrow(()->grid.drawFromDeck(LevelDevCard.LEVEL3,ColorDevCard.GREEN));

    }

    @RepeatedTest(10)
    public void cardsFromJSONTest() {
        DevSetup devSetup = null;
        try {
            devSetup = new DevSetup();
        } catch (IOException e) {
            fail(e.getMessage());
        }

        String Card1 = "";
        String Card2 = "";
        String Card3 = "";
        String Card4 = "";

        for(ColorDevCard color : ColorDevCard.values()){
            DevCard card = devSetup.showDevDeck(LevelDevCard.LEVEL1,color);
            if (Card1.equals("")) {
                Card1 = card.getCardID();
            } else if (Card2.equals("")) {
                Card2 = card.getCardID();
            } else if (Card3.equals("")) {
                Card3 = card.getCardID();
            } else if (Card4.equals("")) {
                Card4 = card.getCardID();
            }
        }

        assertEquals(Card1,devSetup.showDevDeck(LevelDevCard.LEVEL1,ColorDevCard.GREEN).getCardID());
        assertEquals(Card2,devSetup.showDevDeck(LevelDevCard.LEVEL1,ColorDevCard.YELLOW).getCardID());
        assertEquals(Card3,devSetup.showDevDeck(LevelDevCard.LEVEL1,ColorDevCard.BLUE).getCardID());
        assertEquals(Card4,devSetup.showDevDeck(LevelDevCard.LEVEL1,ColorDevCard.PURPLE).getCardID());
    }

    @Test
    public void leaderCardJSON() throws MissingCardException {
        Deck<LeaderCard> deckLeader;
        List<LeaderCard> init = new ArrayList<>();

        try {
            init = new ObjectMapper().readValue(
                    getClass().getResourceAsStream("/json/LeaderCards.json"),
                    new TypeReference<List<LeaderCard>>(){});
        }catch (IOException e){
            fail();
        }
        deckLeader = new Deck<>(init);

        int i = 1;
        for (int j = 0; j<init.size(); j++){
            assertEquals("LC"+ i,deckLeader.peekCard("LC"+ i).getCardID());
            i++;
        }
    }
}
