package it.polimi.ingsw.model.match.match;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.communication.packet.updates.DevSetupUpdater;
import it.polimi.ingsw.communication.packet.updates.NewPlayerUpdater;
import it.polimi.ingsw.communication.packet.updates.TrayUpdater;
import it.polimi.ingsw.model.Dispatcher;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.exceptions.PlayerStateException;
import it.polimi.ingsw.model.exceptions.card.AlreadyInDeckException;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.exceptions.requisite.LootTypeException;
import it.polimi.ingsw.model.exceptions.tray.OutOfBoundMarketTrayException;
import it.polimi.ingsw.model.exceptions.tray.UnpaintableMarbleException;
import it.polimi.ingsw.model.exceptions.warehouse.UnobtainableResourceException;
import it.polimi.ingsw.model.cards.DevSetup;
import it.polimi.ingsw.model.exceptions.warehouse.WrongDepotException;
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

/**
 * This class represents the Match
 */
public abstract class Match implements PlayerToMatch {

    /**
     * the number of possible players in the game
     */
    public final int gameSize;

    /**
     * the minimum player number to start the game
     */
    public final int minimumPlayer;

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
     * The view to send all the changes in the state
     */
    protected final Dispatcher view;

    /**
     * This method is the constructor of the class
     * @param gameSize indicates the number of players that can play this match
     * @param min indicates the minimum number of players to start this match
     */
    protected Match(int gameSize, int min, Dispatcher view) {
        this.gameSize = gameSize;
        this.minimumPlayer = min;

        this.view = view;

        this.turn = new Turn();
        gameOnAir = false;

        this.vaticanSpaceCheck = new EnumMap<>(VaticanSpace.class);

        this.vaticanSpaceCheck.put(VaticanSpace.FIRST, true);
        this.vaticanSpaceCheck.put(VaticanSpace.SECOND, true);
        this.vaticanSpaceCheck.put(VaticanSpace.THIRD, true);

        this.marketTray = new MarketTray();

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

        this.devSetup = new DevSetup();

        updateDevSetup();
        updateTray();
    }

    /**
     * add a new player to the game
     * @param joined player who join
     * @return true if success, false instead
     */
    public boolean playerJoin(Player joined) {
        if (this.turn.playerInGame() > gameSize) return false;
        if (gameOnAir) return false;
        if (this.turn.joinPlayer(joined)) {
            this.view.publish(new NewPlayerUpdater(joined.getNickname()));
            startGame();
            return true;
        }
        return false;
    }

    /**
     * start the game: start the turn of the first player
     * @return true if success, false instead
     */
    private void startGame() {
        if(this.turn.playerInGame() != this.gameSize || gameOnAir) return;
        this.gameOnAir = true;
        this.view.disableHistory();
        this.turn.randomizeInkwellPlayer();
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
    public void useMarketTray(RowCol rc, int index) throws OutOfBoundMarketTrayException, UnobtainableResourceException, EndGameException, WrongDepotException {
        switch (rc) {
            case COL: this.marketTray.pushCol(index, turn.getCurPlayer());
            case ROW: this.marketTray.pushRow(index, turn.getCurPlayer());
        }

        // update lite model
        this.updateTray();
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
                    temp.add(this.devSetup.showDevDeck(levelDevCard, colorDevCard));
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Failed to find the Deck: " + colorDevCard + " " + levelDevCard);
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
    public boolean buyDevCard(LevelDevCard row, ColorDevCard col) throws PlayerStateException, EmptyDeckException, LootTypeException, AlreadyInDeckException, EndGameException {
        // todo check if non current players actually can't buy a card
        if (this.turn.getCurPlayer().hasRequisite(this.devSetup.showDevDeck(row, col).getCost(),row,col,this.devSetup.showDevDeck(row,col))) {
            this.turn.getCurPlayer().receiveDevCard(this.devSetup.drawFromDeck(row, col));

            // update lite model
            this.updateDevSetup();

            return true;
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
        for (Player x : turn.getOtherPlayer()) {
            try {
                x.moveFaithMarker(amount);
            } catch (EndGameException e) {
                this.startEndGameLogic();
            }
        }
    }


    /**
     * Tells to the match the end of the player turn;
     */
    @Override
    public void endMyTurn() {
        this.marketTray.unPaint();
        try {
            this.turn.nextPlayer();
        } catch (EndGameException e) {
            gameOnAir = false;
            this.turn.countingPoints();
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
        //this.turn.getCurPlayer().endThisTurn();
    }

    /**
     * Return the number of player in the game
     * @return the number of player
     */
    public int playerInGame() {
        return this.turn.playerInGame();
    }

    protected void updateTray() {
        this.view.publish(new TrayUpdater(this.marketTray.liteVersion()));
    }

    protected void updateDevSetup() {
        this.view.publish(new DevSetupUpdater(this.devSetup.liteVersion()));
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
