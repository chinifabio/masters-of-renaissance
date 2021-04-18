package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class contains a List of Deck of DevCards. It contains the 12 decks of DevCards divided by level and color.
 */
public class DevSetup {
    /**
     * This is the constructor of the devSetup class.
     * @param decks to insert.
     */
    public DevSetup(List<Deck<DevCard>> decks){
        this.devDeckGrid = decks;
    }

    /**
     * This is the constructor of the devSetup class.
     */
    public DevSetup(){
        // todo leggere e piazzare i vari deck
    }

    /**
     * This attribute is the list of decks.
     */
    private List<Deck<DevCard>> devDeckGrid;

    /**
     * This method returns the first card of a single deck of DevCards. It needs the level and the color of the Deck.
     * @return the top card of the deck which color and level matches the one of the parameters.
     */
    public DevCard showDevDeck(LevelDevCard row, ColorDevCard col) throws IndexOutOfBoundsException, EmptyDeckException {
        Deck<DevCard> tempDeck = takeOneDeck(row, col);
        return tempDeck.peekFirstCard();
    }

    /**
     * This method removes and returns
     * @param row is the level of the searched deck
     * @param col is the color of the searched deck
     * @return the top card of the deck which color and level matches the one of the parameters.
     * @throws IndexOutOfBoundsException if you are trying to get a card from an empty deck with the chosen colors.
     */
    public DevCard drawFromDeck(LevelDevCard row, ColorDevCard col) throws IndexOutOfBoundsException, EmptyDeckException {
        return takeOneDeck(row, col).draw();
    }

    /**
     * This method returns the deck  selected with level and color from the list of decks.
     * @param row is the level of the searched deck
     * @param col is the color of the searched deck
     * @return the top card of the deck which color and level matches the one of the parameters.
     * @throws IndexOutOfBoundsException if you are trying to get a card from an empty deck with the chosen colors.
     */
    private Deck<DevCard> takeOneDeck(LevelDevCard row, ColorDevCard col) throws IndexOutOfBoundsException{
        List<Deck<DevCard>> tempDeck = devDeckGrid
                .stream()
                .filter(c -> {
                    try {
                        return col.equals(c.peekFirstCard().getColor());
                    } catch (EmptyDeckException e) {
                        return false;
                    }
                })
                .filter(c -> {
                    try {
                        return row.equals(c.peekFirstCard().getLevel());
                    } catch (EmptyDeckException e) {
                        e.printStackTrace();
                        return false;
                    }
                })
                .collect(Collectors.toList());

            return tempDeck.get(0);
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return devDeckGrid.toString();
    }
}