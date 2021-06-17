package it.polimi.ingsw.model.match.match;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.Pair;
import it.polimi.ingsw.communication.packet.updates.FaithTrackUpdater;
import it.polimi.ingsw.communication.packet.updates.NewPlayerUpdater;
import it.polimi.ingsw.communication.packet.updates.TokenUpdater;
import it.polimi.ingsw.model.Dispatcher;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.exceptions.card.AlreadyInDeckException;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.match.SoloTokenReaction;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.FaithTrack;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.VaticanSpace;

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
     * Nickname of lorenzo
     */
    public final String lorenzoNickname = "Lorenzo il Magnifico";

    /**
     * The representation of lorenzo faith tack
     */
    private final FaithTrack lorenzo = new FaithTrack(this.view, lorenzoNickname);

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
     * the player who are playing the game
     */
    private Player player = null;

    /**
     * Build a single player game instance: the number of player that the game accept is 1 and the minimum 1
     */
    public SingleplayerMatch(Dispatcher view) throws IOException {
        super(1, view);

        List<SoloActionToken> init = new ObjectMapper().readValue(
                getClass().getResourceAsStream("/json/SoloActionTokens.json"),
                new TypeReference<>() {
                });

        this.soloToken = new Deck<>(init);
        this.soloToken.shuffle();

        updateToken();

        this.discardedFromToken = new Deck<>();

        this.view.publish(new NewPlayerUpdater(lorenzoNickname));
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
            startEndGameLogic();
        }
        view.sendMessage("Lorenzo moved " + i + " tiles in the faith track");
    }

    /**
     * This method shuffle the solo action token
     */
    @Override
    public void shuffleToken() {
        this.soloToken.shuffle();
        view.sendMessage("The solo token where shuffled");
    }

    /**
     * add a new player to the game
     *
     * @param joined player who join
     * @return true if success, false instead
     */
    @Override
    public boolean playerJoin(Player joined) {
        if (player != null) return false;

        player = joined;
        player.initialSetup = new Pair<>(0, 0);
        player.startHisTurn();
        gameOnAir = true;
        return true;
    }

    /**
     * request to other player to flip the pope tile passed in the parameter
     *
     * @param toCheck the vatican space to check
     */
    @Override
    public void vaticanReport(VaticanSpace toCheck) {
        if(checkedPopeTile.get(toCheck)) return; // already checked tile
        player.flipPopeTile(toCheck);
        this.lorenzo.flipPopeTile(toCheck);
        checkedPopeTile.put(toCheck, true);
    }

    /**
     * discard a develop card from the dev setup
     *
     * @param color the color of discarded cards in dev setup
     */
    @Override
    public void discardDevCard(ColorDevCard color) {
        int toDiscard = 2;
        List<LevelDevCard> levels = new ArrayList<>(Arrays.asList(LevelDevCard.values()));
        levels.remove(LevelDevCard.NOLEVEL);

        for(int i = 0; i < toDiscard; i++) {
            Iterator<LevelDevCard> levelDevCardIterator = levels.iterator();
            boolean res = false;

            LevelDevCard level = levelDevCardIterator.next();
            while (!res) {
                try {
                    this.discardedFromToken.insertCard(this.devSetup.drawFromDeck(level, color));
                    res = true;
                } catch (EmptyDeckException e) {
                    // discard another card but with a different level
                    if(levelDevCardIterator.hasNext())
                        level = levelDevCardIterator.next();

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
        this.updateDevSetup();

        if (this.devSetup.showDevDeck(levels.get(levels.size() - 1), color) == null) { // try to watch if there is cards in the top level deck
            System.out.println("end of the game: Lorenzo discarded all the dev cards of a color");
            this.lorenzoWinner = true;
            startEndGameLogic(); // start end game logic if there is no card to discard
        }
    }

    /**
     * Tells to the match the end of the player turn;
     *
     */
    @Override
    public void turnDone() {
        try {
            SoloActionToken s = this.soloToken.useAndDiscard();
            s.useEffect(this);
            view.sendMessage("Lorenzo used " + s.getCardID());
            updateToken();
        } catch (EmptyDeckException ignore) {
            System.out.println(">.<");
        }

        this.marketTray.unPaint();
        updateTray();
        if (gameOnAir) player.startHisTurn();
        view.sendMessage("It is you turn!");
    }

    /**
     * Tells to the match that a player has done the init phase
     */
    @Override
    public void initialSelectionDone() {
        player.startHisTurn();
        if (model != null) model.gameSetupDone();
    }

    /**
     * This method starts the end game logic
     */
    @Override
    public void startEndGameLogic() {
        gameOnAir = false;
        player.endThisTurn();
    }

    /**
     * Return the number of player in the game
     *
     * @return the number of player
     */
    @Override
    public int playerInGame() {
        return 1;
    }

    /**
     * disconnect a player from the match
     *
     * @param player the disconnected player
     */
    @Override
    public boolean disconnectPlayer(Player player) {
        return true;
    }

    /**
     * reconnect a player to the game
     *
     * @param nickname the nickname of the player who need to be reconnected
     * @return the reconnected player
     */
    @Override
    public Player reconnectPlayer(String nickname) {
        player.startHisTurn();
        view.sendMessage("Welcome back");
        return player;
    }

    /**
     * Method called when player do action such that other players obtain faith point
     *
     * @param amount faith point given to other player
     */
    @Override
    public void othersPlayersObtainFaithPoint(int amount) {
        try {
            lorenzo.movePlayer(amount, this);
        } catch (EndGameException e) {
            System.out.println("end of the game: Lorenzo reach the end of faith track");
            lorenzoWinner = true;
            startEndGameLogic();
        }
    }

    /**
     * This method is used to calculate and notify the winner of the match.
     * On each player is call the method to calculate the points obtained and the higher one wins
     */
    @Override
    public void winnerCalculator() {
        // todo look for lorenzo winning flag or calculate the player points
    }

    private void updateToken() {
        this.view.publish(new TokenUpdater(this.soloToken.peekFirstCard().liteVersion()));
    }

    /**
     * Obtain the solo action token in the game
     * @return a new deck of solo action tocken
     */
    public Deck<SoloActionToken> obtainSoloTokens() {
        return this.soloToken;
    }

    // for testing
    public Deck<DevCard> test_getDiscarded() {
        return this.discardedFromToken;
    }

    /**
     * Obtain the position of lorenzo in the game
     * @return the position of lorenzo il magnifico
     */
    public int lorenzoPosition() {
        return lorenzo.getPlayerPosition();
    }

    /**
     * used to view the value of the flag lorenzo winner
     * @return the lorenzo winner flag value
     */
    public boolean isLorenzoWinner() {
        return lorenzoWinner;
    }

    /**
     * Used to obtain the current player in the match
     * @return the current player
     */
    public Player currentPlayer(){
        return this.player;
    }
}
