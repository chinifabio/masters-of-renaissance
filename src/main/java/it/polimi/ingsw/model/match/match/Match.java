package it.polimi.ingsw.model.match.match;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.communication.packet.updates.DevSetupUpdater;
import it.polimi.ingsw.communication.packet.updates.TrayUpdater;
import it.polimi.ingsw.litemodel.Scoreboard;
import it.polimi.ingsw.model.VirtualView;
import it.polimi.ingsw.model.Model;
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

import java.io.IOException;
import java.util.*;

/**
 * This class represents the Match
 */
public abstract class Match implements PlayerToMatch {

    /**
     * the number of possible players in the game
     */
    public final int gameSize;

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
     * this is the leader card of the match shuffled and given to player during the starting of the game
     */
    protected final Deck<LeaderCard> leaderCardDeck;

    /**
     * The view to send all the changes in the state
     */
    protected final VirtualView view;

    protected Model model;

    /**
     * checked pope tiles
     */
    protected final Map<VaticanSpace, Boolean> checkedPopeTile = new EnumMap<>(VaticanSpace.class);

    /**
     * This method is the constructor of the class
     * @param gameSize indicates the number of players that can play this match
     * @param view the view on which notify all changes
     */
    protected Match(int gameSize, VirtualView view) throws IOException {
        this.gameSize = gameSize;
        this.view = view;

        gameOnAir = false;

        this.marketTray = new ObjectMapper()
                .readerFor(MarketTray.class)
                .readValue(getClass().getResourceAsStream("/json/MarketTray.json"));

        List<LeaderCard> init = new ObjectMapper().readValue(
                getClass().getResourceAsStream("/json/LeaderCards.json"),
                new TypeReference<>() {}
        );

        this.leaderCardDeck = new Deck<>(init);
        this.leaderCardDeck.shuffle();

        this.devSetup = new DevSetup();

        this.checkedPopeTile.put(VaticanSpace.FIRST, false);
        this.checkedPopeTile.put(VaticanSpace.SECOND, false);
        this.checkedPopeTile.put(VaticanSpace.THIRD, false);

        updateDevSetup();
        updateTray();
    }

    /**
     * This method sets the Model
     * @param model is the model to set
     */
    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * add a new player to the game
     * @param joined player who join
     * @return true if success, false instead
     */
    public abstract boolean playerJoin(Player joined);

    /**
     * request to other player to flip the pope tile passed in the parameter
     *
     * @param toCheck the vatican space to check
     */
    @Override
    public abstract void vaticanReport(VaticanSpace toCheck);

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
            case COL: this.marketTray.pushCol(index, currentPlayer()); break;
            case ROW: this.marketTray.pushRow(index, currentPlayer()); break;
        }

        view.sendMessage(currentPlayer().getNickname() + " used market tray");

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
        view.sendMessage(currentPlayer().getNickname() + " painted a marble in market tray");

        updateTray();
    }

    /**
     * return a view of the dev setup. It is shown only the first card of each decks
     *
     * @return the list representation of the dev setup
     */
    @Override
    public List<DevCard> test_viewDevSetup() {
        List<DevCard> temp = new ArrayList<>();
        for(ColorDevCard colorDevCard : ColorDevCard.values()){
            if(colorDevCard.equals(ColorDevCard.NOCOLOR)) break;
            for(LevelDevCard levelDevCard : LevelDevCard.values()){
                if(levelDevCard.equals(LevelDevCard.NOLEVEL)) break;
                temp.add(devSetup.showDevDeck(levelDevCard, colorDevCard));
            }
        }
        return temp;
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
        if (this.currentPlayer().hasRequisite(this.devSetup.showDevDeck(row, col).getCost(),row,col,this.devSetup.showDevDeck(row,col))) {
            this.currentPlayer().receiveDevCard(this.devSetup.drawFromDeck(row, col));

            view.sendMessage(currentPlayer().getNickname() + " bought a card in the market");
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
    public abstract void othersPlayersObtainFaithPoint(int amount);


    /**
     * Tells to the match the end of the player turn;
     */
    @Override
    public abstract void turnDone();

    /**
     * Tells to the match that a player has done the init phase
     */
    @Override
    public abstract void initialSelectionDone();

    /**
     * This method return a Leader Card Deck
     *
     * @return Leader Card Deck
     */
    @Override
    public List<LeaderCard> requestLeaderCard() throws EmptyDeckException {
        int initialLeader = 4;
        List<LeaderCard> ret = new ArrayList<>(initialLeader);
        for(int i = 0; i < initialLeader; i++) ret.add(leaderCardDeck.draw());
        return ret;
    }

    /**
     * This method starts the end game logic
     */
    @Override
    public abstract void startEndGameLogic();

    /**
     * Return the number of player in the game
     * @return the number of player
     */
    public abstract int playerInGame();

    /**
     * disconnect a player from the match
     * @param player the disconnected player
     */
    public abstract boolean disconnectPlayer(Player player);

    /**
     * reconnect a player to the game
     * @param nickname the nickname of the player who need to be reconnected
     * @return the reconnected player
     */
    public abstract Player reconnectPlayer(String nickname);

    /**
     * This method is used to calculate and notify the winner of the match.
     * On each player is call the method to calculate the points obtained and the higher one wins
     */
    public abstract Scoreboard winnerCalculator();

    /**
     * Return the current player in the game
     * @return current player
     */
    public abstract Player currentPlayer();

    /**
     * Used to see if the game is running
     * @return true if game started, false if is waiting or finished
     */
    public boolean isGameOnAir() {
        return gameOnAir;
    }

    /**
     * Update in the lite model the market Tray
     */
    protected void updateTray() {
        this.view.publish(new TrayUpdater(this.marketTray.liteVersion()));
    }

    /**
     * Update in the lite model the dev setup
     */
    protected void updateDevSetup() {
        this.view.publish(new DevSetupUpdater(this.devSetup.liteVersion()));
    }

    /**
     * Randomize the inkwell player and give the initial resources
     */
    public abstract void initialize();
}
