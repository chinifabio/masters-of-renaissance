package it.polimi.ingsw.model.match;

import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.DevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.exceptions.gameexception.GameException;
import it.polimi.ingsw.model.exceptions.OutOfBoundMarketTrayException;
import it.polimi.ingsw.model.exceptions.gameexception.movesexception.MainActionDoneException;
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
    void useMarketTray(RowCol rc, int index) throws MainActionDoneException, OutOfBoundMarketTrayException, GameException;

    /**
     * return a view of the dev setup. It is shown only the first card of each decks
     * @return the list representation of the dev setup
     */
    List<DevCard> viewDevSetup();

    /**
     * player ask to buy the first card of the deck in position passed as parameter
     * @param col the column of the card required
     * @param row the row of the card required
     * @return true if there where no issue, false instead
     */
    boolean buyDevCard(LevelDevCard row, ColorDevCard col);

    /**
     * Method called when player do action such that other players obtain faith point
     * @param amount faith point given to other player
     */
    void othersPlayersObtainFaithPoint(int amount);

    /**
     * discard a develop card from the dev setup
     * @param color the color of discarded cards in dev setup
     */
    void discardDevCard(ColorDevCard color);

    /**
     * Tells to the match the end of the player turn;
     * @return
     */
    boolean endMyTurn();
}
