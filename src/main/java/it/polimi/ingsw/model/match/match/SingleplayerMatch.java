package it.polimi.ingsw.model.match.match;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.exceptions.PlayerStateException;
import it.polimi.ingsw.model.exceptions.card.AlreadyInDeckException;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.match.SoloTokenReaction;
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
public class SingleplayerMatch extends Match implements SoloTokenReaction {

    /**
     * The representation of lorenzo faith tack
     */
    private final FaithTrack lorenzo;

    /**
     * The solo action token used at end turn of a single player match
     */
    private final Deck<SoloActionToken> soloToken;

    /**
     * All the DevCard discarded from solo token
     */
    private final Deck<DevCard> discardedFromToken;

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

        this.discardedFromToken = new Deck<>();
    }

    /**
     * This method move lorenzo by a certaian amount passed as parameter
     *
     * @param i the amount of cells to move Lorenzo
     */
    @Override
    public void moveLorenzo(int i) throws EndGameException {
        this.lorenzo.movePlayer(i, this);
    }

    /**
     * This method shuffle the solo action token
     */
    @Override
    public void shuffleToken() {
        this.soloToken.shuffle();
    }

    /**
     * discard a develop card from the dev setup
     *
     * @param color the color of discarded cards in dev setup
     */
    @Override
    public void discardDevCard(ColorDevCard color) throws EndGameException, EmptyDeckException, AlreadyInDeckException {
        // todo da mettere nell'effect come amount
        /*
        int toDiscard = 2;
        List<LevelDevCard> list = Arrays.asList(LevelDevCard.values());

        for(int i = 0; i < toDiscard; i++) {
            Iterator levels = list.iterator();
            boolean res = false;

            LevelDevCard level = (LevelDevCard) levels.next();
            while (!res) {
                try {
                    this.discardedFromToken.insertCard(this.devSetup.drawFromDeck(level, color));
                    res = true;
                } catch (EmptyDeckException e) {
                    if(levels.hasNext()) level = (LevelDevCard) levels.next();
                    else throw new EndGameException();
                }
            }
        }

        this.devSetup.showDevDeck(list.get(list.size()-1), color);
        // if exception is thrown then the end game logic need to be started
        */

        // todo temporaneo fino quando non sono state implementate tutte le dev card
        this.discardedFromToken.insertCard(new DevCard("riga da eliminare1", null, 0, null, null, null));
        this.discardedFromToken.insertCard(new DevCard("riga da eliminare2", null, 0, null, null, null));
    }

    /**
     * Tells to the match the end of the player turn;
     *
     * @return true if success
     */
    @Override
    public boolean endMyTurn() throws PlayerStateException {
        try {
            this.soloToken.useAndDiscard().useEffect(this);
        } catch (EndGameException e) {
            // todo logica di fine gioco
            e.printStackTrace();
        } catch (EmptyDeckException e) {
            // todo finire la partita con uno stato di errore
            e.printStackTrace();
        } catch (AlreadyInDeckException e) {
            e.printStackTrace();
            // todo finire la partita con uno stato di errore
        }
        return super.endMyTurn();
    }

    // for testing
    public Deck<SoloActionToken> test_getSoloDeck() {
        return this.soloToken;
    }

    // for testing
    public Deck<DevCard> test_getDiscarded() {
        return this.discardedFromToken;
    }

    // for testing
    public int test_getLorenzoPosition() {
        return this.lorenzo.getPlayerPosition();
    }
}
