package it.polimi.ingsw.model.cards;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.litemodel.litecards.LiteDevCard;
import it.polimi.ingsw.litemodel.litecards.LiteDevSetup;
import it.polimi.ingsw.model.MappableToLiteVersion;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains a List of Deck of DevCards. It contains the 12 decks of DevCards divided by level and color.
 */
public class DevSetup implements MappableToLiteVersion {
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
            temp.shuffle();
            this.devDeckGrid.add(temp);
        }
    }

    /**
     * This attribute is the list of decks.
     */
    private final List<Deck<DevCard>> devDeckGrid;

    /**
     * This method returns the first card of a single deck of DevCards. It needs the level and the color of the Deck.
     * @return the top card of the deck which color and level matches the one of the parameters.
     */
    public DevCard showDevDeck(LevelDevCard row, ColorDevCard col) throws IndexOutOfBoundsException {
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
        return devDeckGrid
                .stream()
                .filter(c -> col.equals(c.peekFirstCard() == null ? ColorDevCard.NOCOLOR : c.peekFirstCard().getColor()))
                .filter(c -> row.equals(c.peekFirstCard() == null ? LevelDevCard.NOLEVEL : c.peekFirstCard().getLevel()))
                .findAny().orElse(new Deck<>());
    }

    /**
     * Create a lite version of the class and serialize it in json
     *
     * @return the json representation of the lite version of the class
     */
    @Override
    public LiteDevSetup liteVersion() {
        LiteDevCard[][] result = new LiteDevCard[3][4]; // todo remove the hardcoded values
        LiteDevCard nullCard= new LiteDevCard("EMPTY", null, 0, LevelDevCard.NOLEVEL, ColorDevCard.NOCOLOR, new ArrayList<>());

        int i = 0;
        for(Deck<DevCard> deck : this.devDeckGrid){
            DevCard c = deck.peekFirstCard();
            result[i/4][i%4] = c == null ? nullCard : c.liteVersion();
            i++;
        }

        return new LiteDevSetup(result, 3, 4);
    }
}