package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.exceptions.AlreadyInDeckException;
import it.polimi.ingsw.model.exceptions.EmptyDeckException;
import it.polimi.ingsw.model.exceptions.MissingCardException;

import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

/**
 * This class contains every method to manage a deck of cards. Generics are useful to use methods inside the Card sublcasses.
 * @param <T>
 */
public class Deck<T extends Card>{
    /**
     * This method create a deck of card starting from an array of given cards
     * @param newCards array of all the cards to add to the deck
     */
    public Deck(List<T> newCards) {
        this.cards.addAll(newCards);
        this.updateNumberOfCards();
    }

    /**
     * This constructor creates a deck with a Card inside.
     * @param card to insert.
     */
    public Deck(T card) {
        this.cards.add(card);
        this.updateNumberOfCards();
    }

    /**
     * This constructor creates a deck without any Card inside.
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
    private List<T> cards = new ArrayList<>();

    /**
     * This attribute is a list of Card, the discarded ones. Used on single player matches, to shuffle every SoloActionToken.
     */
    private List<T> discardedCards = new ArrayList<>();

    /**
     * This method shuffles the deck. DevCards, LeaderCards and SoloActionToken's deck are shuffled at the start of the game.
     * The SoloActionToken deck needs to get back his discardedCards when it shuffles during the game.
     */
    public void shuffle(){
        this.cards.addAll(this.discardedCards);
        this.discardedCards.clear();
        this.updateNumberOfCards();
        List<T> tempCards = new ArrayList<>();
        for(int i=this.numberOfCards;i>0;i--){
            tempCards.add(this.cards.remove((int)(Math.random()*this.numberOfCards)));
            this.updateNumberOfCards();
        }
        this.cards = tempCards;
        this.updateNumberOfCards();
    }

    /**
     * This method takes the first card from the List of Card and returns a Card.
     * @return the first card of the list.
     * @throws EmptyDeckException if the list is empty.
     */
    public T draw() throws EmptyDeckException{
        if(this.cards.isEmpty()){
            throw new EmptyDeckException("exception: draw from empty deck");
        }
        T tempCard;
        tempCard = this.cards.remove(0);
        this.updateNumberOfCards();
        return tempCard;
    }

    /**
     * This method takes the first card from the List of Card and adds it to the List of discardedCards.
     * @throws EmptyDeckException if the deck is empty.
     */
    public void discard() throws EmptyDeckException {
        if(this.cards.isEmpty()){
            throw new EmptyDeckException("exception: discard from empty deck");
        }
        this.discardedCards.add(this.cards.remove(0));
        this.updateNumberOfCards();
    }

    /**
     * This method takes a card (based on cardID) from the List of Card and adds it to the List of discardedCards.
     * @param cardID the ID of the card to discard.
     * @throws EmptyDeckException if the deck is empty.
     * @throws MissingCardException if the card is not in the deck.
     */
    public void discard(String cardID) throws EmptyDeckException, MissingCardException{
        if(this.cards.isEmpty()){
            throw new EmptyDeckException("exception: discard from empty deck");
        }
        for(int i=0;i<this.numberOfCards;i++){
            if(this.peekCard(cardID).equals(cards.get(i))){
                this.discardedCards.add(this.cards.remove(i));
            }
        }
        this.updateNumberOfCards();
    }

    /**
     * This method inserts a card into the deck.
     * @param card to insert.
     * @throws AlreadyInDeckException if the deck already contains this card.
     */
    public void insertCard(T card) throws AlreadyInDeckException{
        for(int i=0;i<this.numberOfCards;i++){
            if(card.equals(this.cards.get(i))){
                throw new AlreadyInDeckException("exception: two cards with the same ID");
            }
        }
        this.cards.add(0,card);
        this.updateNumberOfCards();
    }

    /**
     * This method return the first card (position 0) of the deck.
     * @return the top card of the deck.
     */
    public T peekFirstCard() throws EmptyDeckException{
        if(this.cards.isEmpty()) throw new EmptyDeckException("exception: peeking from an empty deck");
        return this.cards.get(0);
    }

    /**
     * This method search a card inside the deck using cardID.
     * @param cardID to search.
     * @return the card which cardID matches the parameter.
     * @throws MissingCardException when the card you are searching for is not inside the deck.
     */
    public T peekCard(String cardID) throws MissingCardException{
        for(int i=this.numberOfCards;i>0;i--) {
            if (this.cards.get(i-1).getCardID().equals(cardID)) return this.cards.get(i-1);
        }
        throw new MissingCardException("exception: missing card to peek");
    }

    /**
     * This method update the number of Cards of the deck. It's called only from other methods.
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

    @Override
    public String toString() {
        return "Deck{" +
                "numberOfCards=" + numberOfCards +
                ", cards=" + cards +
                ", discardedCards=" + discardedCards +
                '}';
    }
}