package it.polimi.ingsw.model.match.match;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.exceptions.OutOfBoundMarketTrayException;
import it.polimi.ingsw.model.exceptions.game.GameException;
import it.polimi.ingsw.model.exceptions.game.moves.MainActionDoneException;
import it.polimi.ingsw.model.match.DevSetup;
import it.polimi.ingsw.model.match.PlayerToMatch;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.Marble;
import it.polimi.ingsw.model.match.markettray.MarketTray;
import it.polimi.ingsw.model.match.markettray.RowCol;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.VaticanSpace;

import java.util.List;

public abstract class Match implements PlayerToMatch {
    /**
     * the number of possible players in the game
     */
    protected final int gameSize;

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
     * initialize the match
     */
    protected Match(int gameSize) {
        this.gameSize = gameSize;

        this.turn = new Turn();
        gameOnAir = false;

        this.marketTray = new MarketTray();
        this.devSetup = new DevSetup();
        this.leaderCardDeck = new Deck<>();
        // legge e crea tutte le carte leader
    }

    /**
     * add a new player to the game
     * @param joined player who join
     * @return true if success, false instead
     */
    public abstract boolean playerJoin(Player joined);

    /**
     * start the game: start the turn of the first player
     * @return true if success, false instead
     */
    public abstract boolean startGame();

    /**
     * request to other player to flip the pope tile passed in the parameter
     *
     * @param toCheck the vatian space to check
     */
    @Override
    public void vaticanReport(VaticanSpace toCheck) {
        this.turn.getOtherPlayer().forEach(x -> x.flipPopeTile(toCheck));
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
     * @param rc    enum to identify if I am pushing row or col
     * @param index the index of the row or column of the tray
     */
    @Override
    public void useMarketTray(RowCol rc, int index) throws MainActionDoneException, OutOfBoundMarketTrayException {
        switch (rc) {
            case COL: this.marketTray.pushCol(index, turn.getCurPlayer());
            case ROW: this.marketTray.pushRow(index, turn.getCurPlayer());
            //default: throw new GameException();
        }
    }

    /**
     * return a view of the dev setup. It is shown only the first card of each decks
     *
     * @return the list representation of the dev setup
     */
    @Override
    public List<DevCard> viewDevSetup() {
        return this.devSetup.viewDevSetup();
    }

    /**
     * player ask to buy the first card of the deck in position passed as parameter
     *
     * @param row the row of the card required
     * @param col the column of the card required
     * @return true if there where no issue, false instead
     */
    @Override
    public boolean buyDevCard(LevelDevCard row, ColorDevCard col) {
        // TODO aggiornare con devsetup fatto
        return false;
    }

    /**
     * Method called when player do action such that other players obtain faith point
     *
     * @param amount faith point given to other player
     */
    @Override
    public void othersPlayersObtainFaithPoint(int amount) {
        turn.getOtherPlayer().forEach(x -> x.moveFaithMarker(amount));
    }


    /**
     * Tells to the match the end of the player turn;
     * @return true if success
     */
    @Override
    public boolean endMyTurn() {
        return this.turn.nextPlayer();
    }
}
