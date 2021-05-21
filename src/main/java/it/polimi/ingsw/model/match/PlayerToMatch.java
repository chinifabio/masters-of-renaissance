package it.polimi.ingsw.model.match;

import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.exceptions.PlayerStateException;
import it.polimi.ingsw.model.exceptions.card.AlreadyInDeckException;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.exceptions.requisite.LootTypeException;
import it.polimi.ingsw.model.exceptions.requisite.NoRequisiteException;
import it.polimi.ingsw.model.exceptions.tray.UnpaintableMarbleException;
import it.polimi.ingsw.model.exceptions.tray.OutOfBoundMarketTrayException;
import it.polimi.ingsw.model.exceptions.warehouse.UnobtainableResourceException;
import it.polimi.ingsw.model.exceptions.warehouse.WrongDepotException;
import it.polimi.ingsw.model.match.markettray.MarkerMarble.Marble;
import it.polimi.ingsw.model.match.markettray.RowCol;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.VaticanSpace;

import java.util.List;

/**
 * This interface contains all the method a player need to call on a Match
 */
public interface PlayerToMatch {
    /**
     * request to other player to flip the pope tile passed in the parameter
     * @param toCheck the vatian space to check
     */
    void vaticanReport(VaticanSpace toCheck);

    /**
     * return a view of the MarketTray
     * @return list of marble in the market tray
     */
    List<Marble> viewMarketTray();

    /**
     * Use the market tray
     * @param index the index of the row or column of the tray
     * @param rc enum to identify if I am pushing row or col
     */
    void useMarketTray(RowCol rc, int index) throws OutOfBoundMarketTrayException, UnobtainableResourceException, EndGameException, WrongDepotException;

    /**
     * return a view of the dev setup. It is shown only the first card of each decks
     * @return the list representation of the dev setup
     */
    List<DevCard> viewDevSetup();

    /**
     * player ask to buy the first card of the deck in position passed as parameter
     * @param row the column of the card required
     * @param col the row of the card required
     * @return true if there where no issue, false instead
     * @throws NoRequisiteException if the card has no requisite
     * @throws PlayerStateException if the Player can't do this action
     * @throws EmptyDeckException if the Player tries to buy a DevCard of an empty deck
     * @throws LootTypeException if the type of the requirements are wrong
     */
    boolean buyDevCard(LevelDevCard row, ColorDevCard col) throws NoRequisiteException, PlayerStateException, EmptyDeckException, LootTypeException, AlreadyInDeckException, EndGameException;

    /**
     * Method called when player do action such that other players obtain faith point
     * @param amount faith point given to other player
     */
    void othersPlayersObtainFaithPoint(int amount);

    /**
     * paint a Marble in market tray
     * @param newColor the new Marble color
     * @param marbleIndex indicates the position of the Marble
     */
    void paintMarbleInTray(Marble newColor, int marbleIndex) throws UnpaintableMarbleException;

    /**
     * Tells to the match the end of the player turn;
     * @return true if the player ended is turn
     */
    void turnDone();

    /**
     * This method return a Leader Card Deck
     * @return Leader Card Deck
     */
    List<LeaderCard> requestLeaderCard();

    /**
     * This method starts the end game logic
     */
    void startEndGameLogic();

    /**
     * Tells to the match that a player has done the init phase
     */
    void initialSelectionDone();
}
