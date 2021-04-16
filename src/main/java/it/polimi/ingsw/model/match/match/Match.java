package it.polimi.ingsw.model.match.match;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.exceptions.NoRequisiteException;
import it.polimi.ingsw.model.exceptions.OutOfBoundMarketTrayException;
import it.polimi.ingsw.model.exceptions.UnpaintableMarbleException;
import it.polimi.ingsw.model.exceptions.gameexception.movesexception.MainActionDoneException;
import it.polimi.ingsw.model.cards.DevSetup;
import it.polimi.ingsw.model.match.PlayerToMatch;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.Marble;
import it.polimi.ingsw.model.match.markettray.MarketTray;
import it.polimi.ingsw.model.match.markettray.RowCol;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.VaticanSpace;
import it.polimi.ingsw.model.requisite.Requisite;
import it.polimi.ingsw.model.resource.Resource;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        List<Deck<DevCard>> decks = new ArrayList<>(); //TODO da aggiungere al costruttore! sia qui che nelle sottoclassi

        this.devSetup = new DevSetup(decks);
        this.leaderCardDeck = new Deck<>();
        // legge e crea tutte le carte leader
    }

    //TODO cancellare questo costruttore: usato nei test prima dell'implementazione json delle devCard
    protected Match(int gameSize, List<Deck<DevCard>> decks) {
        this.gameSize = gameSize;

        this.turn = new Turn();
        gameOnAir = false;

        this.marketTray = new MarketTray();

        this.devSetup = new DevSetup(decks);
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
     * paint a marble in market tray
     *
     * @param newColor    the new marble color
     * @param marbleIndex
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
                temp.add(this.devSetup.showDevDeck(levelDevCard,colorDevCard));
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
    public boolean buyDevCard(LevelDevCard row, ColorDevCard col) {
        System.out.println("Match: " + this.turn.getCurPlayer().getNickname() + " tries to buy DevCard -> " + "level: " + row + " color: " + col);
        try {
            if (this.turn.getCurPlayer().hasRequisite(this.devSetup.showDevDeck(row, col).getCost(),row,col)){
                this.turn.getCurPlayer().receiveDevCard(this.devSetup.drawFromDeck(row, col));
                System.out.println();
                return true;
            }
        } catch(NoRequisiteException e1){
            System.out.println("problema, manca il requisite della carta");
            return false; //TODO it should never enter here
        } catch(IndexOutOfBoundsException e2){
            System.out.println("Pescata da un mazzo vuoto!");
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
