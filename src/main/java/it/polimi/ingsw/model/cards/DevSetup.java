package it.polimi.ingsw.model.cards;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
        List<List<DevCard>> init = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            init = objectMapper.readValue(
                    new File("src/resources/DevCards.json"),
                    new TypeReference<List<List<DevCard>>>(){});
        }catch (IOException e) {
            e.printStackTrace();
        }
        this.devDeckGrid = new ArrayList<>();
        for(List<DevCard> deck : init){
            Deck<DevCard> temp = new Deck<>(deck);
            this.devDeckGrid.add(temp);
        }
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
     * This method returns the deck selected with level and color from the list of decks.
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

}