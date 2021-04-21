package it.polimi.ingsw.model.match.match;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.exceptions.PlayerStateException;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.exceptions.requisite.NoRequisiteException;
import it.polimi.ingsw.model.exceptions.tray.OutOfBoundMarketTrayException;
import it.polimi.ingsw.model.exceptions.tray.UnpaintableMarbleException;
import it.polimi.ingsw.model.exceptions.warehouse.UnobtainableResourceException;
import it.polimi.ingsw.model.cards.DevSetup;
import it.polimi.ingsw.model.match.PlayerToMatch;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.Marble;
import it.polimi.ingsw.model.match.markettray.MarketTray;
import it.polimi.ingsw.model.match.markettray.RowCol;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.VaticanSpace;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public abstract class Match implements PlayerToMatch {
    /**
     * the number of possible players in the game
     */
    protected final int gameSize;

    /**
     * the minimum player number to start the game
     */
    private final int minimumPlayer;

    /**
     * this flag is used to check if the game is started
     */
    protected boolean gameOnAir;

    /**
     * This is the market tray of the match
     */
    protected final MarketTray marketTray;

    /**
     * This is the dev setup of the match
     */
    protected final DevSetup devSetup;

    /**
     * this is the players handler of the match
     */
    protected Turn turn;

    /**
     * this is the leader card of the match shuffled and given to player during the starting of the game
     */
    protected final Deck<LeaderCard> leaderCardDeck;

    /**
     * This attribute indicates if the VaticanSpace is activated and the Player can take the victoryPoints of the PopeTile
     */
    protected Map<VaticanSpace, Boolean> vaticanSpaceCheck;

    /**
     * initialize the match
     */
    protected Match(int gameSize, int min) {
        this.gameSize = gameSize;
        this.minimumPlayer = min;

        this.turn = new Turn();
        gameOnAir = false;

        this.vaticanSpaceCheck = new EnumMap<>(VaticanSpace.class);

        this.vaticanSpaceCheck.put(VaticanSpace.FIRST, true);
        this.vaticanSpaceCheck.put(VaticanSpace.SECOND, true);
        this.vaticanSpaceCheck.put(VaticanSpace.THIRD, true);

        this.marketTray = new MarketTray();

        this.devSetup = new DevSetup();

        List<LeaderCard> init = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            init = objectMapper.readValue(
                    new File("src/resources/LeaderCards.json"),
                    new TypeReference<List<LeaderCard>>(){});
        }catch (IOException e) {
            e.printStackTrace();
        }
        this.leaderCardDeck = new Deck<>(init);
        this.leaderCardDeck.shuffle();
    }

    /**
     * add a new player to the game
     * @param joined player who join
     * @return true if success, false instead
     */
    public boolean playerJoin(Player joined) {
        if( turn.playerInGame() < this.gameSize && !gameOnAir) return turn.joinPlayer(joined);
        else return false;
    }


    /**
     * start the game: start the turn of the first player
     * @return true if success, false instead
     */
    public boolean startGame() {
        if(this.turn.playerInGame() < this.minimumPlayer || gameOnAir) return false;
        turn.getInkwellPlayer().startHisTurn();
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
        if(vaticanSpaceCheck.get(toCheck)) {
            this.turn.getOtherPlayer().forEach(x -> x.flipPopeTile(toCheck));
            this.turn.getCurPlayer().flipPopeTile(toCheck);
            vaticanSpaceCheck.put(toCheck, false);
        }
    }

    /**
     * return a view of the MarketTray
     *
     * @return list of marble in the market tray
     */
    @Override
    public List<Marble> viewMarketTray() {
        return this.marketTray.showMarketTray();
    }

    /**
     * Use the market tray
     *
     * @param rc enum to identify if I am pushing row or col
     * @param index the index of the row or column of the tray
     */
    @Override
    public void useMarketTray(RowCol rc, int index) throws OutOfBoundMarketTrayException, UnobtainableResourceException, EndGameException {
        switch (rc) {
            case COL: this.marketTray.pushCol(index, turn.getCurPlayer());
            case ROW: this.marketTray.pushRow(index, turn.getCurPlayer());
            //default: throw new GameException();
        }
    }

    /**
     * paint a marble in market tray
     *
     * @param newColor the new marble color
     * @param marbleIndex the index of the marble to color
     */
    @Override
    public void paintMarbleInTray(Marble newColor, int marbleIndex) throws UnpaintableMarbleException {
        this.marketTray.paintMarble(newColor, marbleIndex);
    }

    /**
     * return a view of the dev setup. It is shown only the first card of each decks
     *
     * @return the list representation of the dev setup
     */
    @Override
    public List<DevCard> viewDevSetup() {
        List<DevCard> temp = new ArrayList<>();
        for(ColorDevCard colorDevCard : ColorDevCard.values()){
            for(LevelDevCard levelDevCard : LevelDevCard.values()){
                try {
                    temp.add(this.devSetup.showDevDeck(levelDevCard,colorDevCard));
                } catch (EmptyDeckException e) {
                    // if there is no card skip to next deck
                }
            }
        }
        return temp;
        //TODO testing: what if the level1 deck is empty? does it break?
    }

    /**
     * player ask to buy the first card of the deck in position passed as parameter
     *
     * @param row the row of the card required
     * @param col the column of the card required
     * @return true if there where no issue, false instead
     */
    @Override
    public boolean buyDevCard(LevelDevCard row, ColorDevCard col) throws NoRequisiteException, PlayerStateException, EmptyDeckException {
        if (this.turn.getCurPlayer().hasRequisite(this.devSetup.showDevDeck(row, col).getCost(),row,col)) {
            try {
                this.turn.getCurPlayer().receiveDevCard(this.devSetup.drawFromDeck(row, col));
                return true;
            } catch (EmptyDeckException e) {
                // todo exception to model
            }
        }
        return false;
    }

    /**
     * Method called when player do action such that other players obtain faith point
     *
     * @param amount faith point given to other player
     */
    @Override
    public void othersPlayersObtainFaithPoint(int amount) {
        for (Player x : turn.getOtherPlayer()) x.moveFaithMarker(amount);
    }


    /**
     * Tells to the match the end of the player turn;
     * @return true if success
     */
    @Override
    public boolean endMyTurn() {
        this.marketTray.unPaint();
        try {
            return this.turn.nextPlayer();
        } catch (EndGameException e) {
            gameOnAir = false;
            this.turn.countingPoints();
            return true;
            // todo tells to the worker to calculate the points
        }
    }

    /**
     * This method return a Leader Card Deck
     *
     * @return Leader Card Deck
     */
    @Override
    public List<LeaderCard> requestLeaderCard() {
        int size = 4;
        List<LeaderCard> ret = new ArrayList<>(size);
        for(int i = 0; i < size; i++) {
            try {
                ret.add(this.leaderCardDeck.draw());
            } catch (EmptyDeckException e) {
                // todo end the game with error state
            }
        }
        return ret;
    }

    /**
     * This method starts the end game logic
     */
    @Override
    public void startEndGameLogic(){
        this.turn.endGame();
    }

    /**
     * This method is used to calculate and notify the winner of the match.
     * On each player is call the method to calculate the points obtained and the higher one wins
     */
    public abstract void winnerCalculator();

    // for test
    public Player test_getCurrPlayer(){
        return turn.getCurPlayer();
    }

    // for test
    public Turn test_getTurn() {
        return this.turn;
    }

    // for test
    public boolean test_getGameOnAir() {
        return gameOnAir;
    }
}
