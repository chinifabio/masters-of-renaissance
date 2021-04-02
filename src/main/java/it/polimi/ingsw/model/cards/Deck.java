package it.polimi.ingsw.model.cards;


import it.polimi.ingsw.model.exceptions.EmptyDeckException;
import it.polimi.ingsw.model.exceptions.MissingCardException;

import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

/**
 * This class contains every method to manage a deck of cards.
 * @param <T>
 */
public class Deck<T extends Card> {

    /**
     * This constructor creates a deck with a list of Card inside.
     * @param cards to insert.
     */
    public Deck(List<Card> cards) {
        this.cards = cards;
        this.updateNumberOfCards();
    }

    /**
     * This constructor creates a deck with a Card inside.
     * @param card to insert.
     */
    public Deck(Card card) {
        this.cards.add(card);
        this.updateNumberOfCards();
    }

    /**
     * This constructor creates a deck without any card inside.
     */
    public Deck() {
        numberOfCards = 0;
    }

    /**
     * This attribute is the number of cards that exist in the deck at any given time.
     */
    private int numberOfCards;

    /**
     * This attribute is a list of Card, the deck.
     */
    private List<Card> cards = new ArrayList<Card>();

    /**
     * This attribute is a list of Card, the discarded ones. Used on single player matches, to shuffle every SoloActionToken.
     */
    private List<Card> discardedCards = new ArrayList<Card>();

    /**
     * This method shuffles the deck. DevCards, LeaderCards and SoloActionToken's deck are shuffled at the start of the game.
     * The SoloActionToken deck needs to get back his discardedCards when it shuffles during the game.
     */
    public void shuffle(){
        this.cards.addAll(this.discardedCards);
        this.discardedCards.clear();
        this.updateNumberOfCards();
        List<Card> tempCards = new ArrayList<Card>();
        for(int i=this.numberOfCards;i>0;i--){
            tempCards.add(this.cards.remove((int)(Math.random()*this.numberOfCards)));
            this.updateNumberOfCards();
        }
        this.cards = tempCards;
    }

    /**
     * This method takes the first card from the List of Card and returns Card.
     * @return the first card of the list.
     * @throws EmptyDeckException if the list is empty.
     */
    public Card draw() throws EmptyDeckException{
        if(this.cards.isEmpty()){
            throw new EmptyDeckException("exception: draw from empty deck");
        }
        Card tempCard;
        tempCard = this.cards.remove(0);
        this.updateNumberOfCards();
        return tempCard;
    }

    /**
     * This method takes the first card from the List of Card and add this card to the list of discardedCards.
     * @throws EmptyDeckException if the list is empty.
     */
    public void discard() throws EmptyDeckException {
        if(this.cards.isEmpty()){
            throw new EmptyDeckException("exception: discard from empty deck");
        }
        this.discardedCards.add(this.cards.remove(0));
        this.updateNumberOfCards();
    }

    /**
     * This method inserts a list of Card into the deck.
     * @param cards to insert.
     */
    public void insertCard(List<Card> cards){
        this.cards.addAll(cards);
        this.updateNumberOfCards();
    }

    /**
     * This method inserts a card into the deck.
     * @param card to insert.
     */
    public void insertCard(Card card){
        this.cards.add(card);
        this.updateNumberOfCards();
    }

    /**
     * This method return the first card (position 0) of the deck.
     * @return the top card of the deck.
     */
    public Card peekFirstCard(){
        return this.cards.get(0);
    }

    /**
     * This method search a card inside the deck using cardID.
     * @param cardID to search.
     * @return the card which cardID matches the parameter.
     * @throws MissingCardException when the card you are searching for is not inside the deck.
     */
    public Card peekCard(String cardID) throws MissingCardException{
        for(int i=this.numberOfCards;i>0;i--) {
            if (this.cards.get(i).getCardID() == cardID) return this.cards.get(i);
        }
        throw new MissingCardException("exception: missing card to peek");
    }

    /**
     * This method update the number of Cards of the deck. It's only called from other methods.
     */
    private void updateNumberOfCards(){
        this.numberOfCards = this.cards.size();
    }

    /**
     * This method returns the number of cards that contains the deck.
     * @return the attribute numberOfCards
     */
    public int getNumberOfCards(){
        return this.numberOfCards;
    }
}