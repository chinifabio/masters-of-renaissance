package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.DevCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.model.exceptions.PlayerStateException;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.exceptions.requisite.LootTypeException;
import it.polimi.ingsw.model.exceptions.requisite.NoRequisiteException;
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
    void addLeader(LeaderCard leaderCard);

    /**
     * This method checks if the player has the requisite needed to buy a card and if that card can be placed
     * If it return true then the warehouse has eliminate the requisites yet
     * If it return false then the player has not the requisite;
     * @param req is the list of requisite needed
     * @param row is the row of the DevSetup where the DevCard is located
     * @param col is the column of the DevSetup where the DevCard is located
     * @param card is the DevCard
     * @return true if the Player has the requisites
     * @throws NoRequisiteException if the card doesn't have requisite
     * @throws LootTypeException if this attribute cannot be obtained from this Requisite
     */
    boolean hasRequisite(List<Requisite> req, LevelDevCard row, ColorDevCard col, DevCard card) throws NoRequisiteException, LootTypeException;

    /**
     * This method adds a DevCard to the player's personal board, using resources taken from the Warehouse
     * @param newDevCard the dev card received that need to be stored in the personal board
     * @throws PlayerStateException if the Player can't do this action
     * @throws EndGameException if the EndGameLogic is activated
     */
    void receiveDevCard(DevCard newDevCard) throws PlayerStateException, EndGameException;

    /**
     * This method flips the PopeTile when the Player is in a Vatican Space or passed the relative PopeSpace
     * @param popeTile the tile to check if it need to be flipped
     */
    void flipPopeTile(VaticanSpace popeTile);

    /**
     * Starts the turn of the player;
     * @return true if the turn starts correctly
     * @throws PlayerStateException if the Player can't do this action
     */
    boolean startHisTurn() throws PlayerStateException;
}
