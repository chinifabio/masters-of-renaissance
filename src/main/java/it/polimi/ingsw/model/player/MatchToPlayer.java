package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.DevCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.exceptions.NoRequisiteException;
import it.polimi.ingsw.model.exceptions.gameexception.LorenzoMovesException;
import it.polimi.ingsw.model.player.personalBoard.faithTrack.VaticanSpace;
import it.polimi.ingsw.model.requisite.Requisite;

import java.util.List;

/**
 * The interface contains all the method that a match need to call on a player
 */
public interface MatchToPlayer {
    /**
     * This method adds a LeaderCard to the Players' PersonalBoard
     * @param leaderCard the leader card to assign to the hand of the player
     */
    void addLeader(LeaderCard leaderCard) throws LorenzoMovesException;

    /**
     * this method check if the player has the requisite needed to buy a card and if that card can be placed
     * If it return true then the warehouse has eliminate the requisites yet
     * If it return false then the player has not the requisite;
     * @param req the requisite
     * @return boolean indicating the succeed of the method
     */
    boolean hasRequisite(List<Requisite> req, LevelDevCard row, ColorDevCard col) throws NoRequisiteException;

    /**
     * This method adds a DevCard to the player's personal board, using resources taken from the Warehouse
     * @param newDevCard the dev card received that need to be stored in the personal board
     */
    void receiveDevCard(DevCard newDevCard);

    /**
     * This method flips the PopeTile when the Player is in a Vatican Space or passed the relative PopeSpace
     * @param popeTile the tile to check if it need to be flipped
     */
    void flipPopeTile(VaticanSpace popeTile);

    /**
     * starts the turn of the player;
     * @return true if success, false otherwise
     */
    boolean startHisTurn();
}
