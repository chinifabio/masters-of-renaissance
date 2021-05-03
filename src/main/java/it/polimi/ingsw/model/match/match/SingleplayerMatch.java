package it.polimi.ingsw.model.match.match;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.cards.*;
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
     * This attribute indicates if Lorenzo is the winner of the Match
     */
    private boolean lorenzoWinner = false;

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
     * This method move lorenzo by a certain amount passed as parameter
     *
     * @param i the amount of cells to move Lorenzo
     */
    @Override
    public void moveLorenzo(int i) {
        try {
            this.lorenzo.movePlayer(i, this);
        } catch (EndGameException e) {
            System.out.println("end of the game: Lorenzo reach the end of faith track");
            lorenzoWinner = true;
            this.turn.endGame();
        }
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
    public void discardDevCard(ColorDevCard color) {
        int toDiscard = 2;
        List<LevelDevCard> list = Arrays.asList(LevelDevCard.values());

        for(int i = 0; i < toDiscard; i++) {
            Iterator<LevelDevCard> levels = list.iterator();
            boolean res = false;

            LevelDevCard level = levels.next();
            while (!res) {
                try {
                    this.discardedFromToken.insertCard(this.devSetup.drawFromDeck(level, color));
                    res = true;
                } catch (EmptyDeckException e) {
                    // discard another card but with a different level
                    if(levels.hasNext())
                        level = levels.next();

                    // starts the end game logic because there is no card available
                    else {
                        this.lorenzoWinner = true;
                        System.out.println("end of the game: Lorenzo discarded dev cards of a color");
                        this.startEndGameLogic();
                    }
                } catch (AlreadyInDeckException e) {
                    // todo error to controller
                }
            }
        }

        try {
            this.devSetup.showDevDeck(list.get(list.size() - 1), color);
        } catch (EmptyDeckException e) {
            this.lorenzoWinner = true;
            System.out.println("end of the game: Lorenzo discarded dev cards of a color");
            this.startEndGameLogic(); // start end game logic if there is no card to discard
        }
    }

    /**
     * Tells to the match the end of the player turn;
     *
     */
    @Override
    public void endMyTurn() {
        try {
            SoloActionToken s = this.soloToken.useAndDiscard();
            s.useEffect(this);
        } catch (EmptyDeckException e) {
            // solo token stack is empty
            // todo end the game with error
        }
        super.endMyTurn();
    }

    /**
     * This method is used to calculate and notify the winner of the match.
     * On each player is call the method to calculate the points obtained and the higher one wins
     */
    @Override
    public void winnerCalculator() {
        // todo look for lorenzo winning flag or calculate the player points
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

    // for testing
    public boolean test_getLorenzoWinner() {
        return lorenzoWinner;
    }
}
