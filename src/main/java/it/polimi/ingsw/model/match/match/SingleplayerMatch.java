package it.polimi.ingsw.model.match.match;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.FaithTrack;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * The single player game to start need only one player.
 * It contains a Lorenzo representation as a faith track and as got a deck of solo action token
 * Lorenzo and solo token are managed automatically once the player end its turn.
 * All lorenzo status change will be sent as "StateHandler" object to remote client player to be shown
 */
public class SingleplayerMatch extends Match {

    /**
     * The representation of lorenzo faith tack
     */
    private final FaithTrack lorenzo;

    /**
     * The solo action token used at end turn of a single player match
     */
    private final Deck<SoloActionToken> soloToken;

    /**
     * Build a single player game instance: the number of player that the game accept is 1 and the minimum 1
     */
    public SingleplayerMatch() {
        super(1, 1);

        this.lorenzo = new FaithTrack();

        List<SoloActionToken> init = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            init = objectMapper.readValue(
                    new File("src/resources/SoloActionTokens.json"),
                    new TypeReference<List<SoloActionToken>>(){});
        }catch (IOException e) {
            e.printStackTrace();
        }

        this.soloToken = new Deck<>(init);
        this.soloToken.shuffle();
    }

    /**
     * discard a develop card from the dev setup
     *
     * @param color the color of discarded cards in dev setup
     */
    @Override
    public void discardDevCard(ColorDevCard color) throws EndGameException {
        // todo da mettere nell'effect come amount
        int toDiscard = 2;
        List<LevelDevCard> list = Arrays.asList(LevelDevCard.values());

        for(int i = 0; i < toDiscard; i++) {
            Iterator levels = list.iterator();
            boolean res = false;

            LevelDevCard level = (LevelDevCard) levels.next();
            while (!res) {
                try {
                    this.devSetup.drawFromDeck(level, color);
                    res = true;
                } catch (EmptyDeckException e) {
                    if(levels.hasNext()) level = (LevelDevCard) levels.next();
                    else throw new EndGameException();
                }
            }
        }

        try {
            this.devSetup.showDevDeck(list.get(list.size()-1), color);
        } catch (EmptyDeckException e) {
            throw new EndGameException();
        }
    }

    /**
     * This method shuffle the solo token deck;
     */
    @Override
    public void shuffleSoloToken() {
        this.soloToken.shuffle();
    }
}
