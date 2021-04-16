package it.polimi.ingsw.model.match.match;

import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;
import it.polimi.ingsw.model.exceptions.faithtrack.IllegalMovesException;
import it.polimi.ingsw.model.exceptions.game.LorenzoMovesException;
import it.polimi.ingsw.model.exceptions.game.movesexception.NotHisTurnException;
import it.polimi.ingsw.model.exceptions.game.movesexception.TurnStartedException;
import it.polimi.ingsw.model.exceptions.warehouse.WrongPointsException;
import it.polimi.ingsw.model.player.Player;

public class MultiplayerMatch extends Match{
    /**
     * build a multiplayer match
     */
    public MultiplayerMatch() {
        super(4);
    }

    @Override
    public boolean playerJoin(Player joined) {
        if( turn.playerInGame() < this.gameSize || gameOnAir) return turn.joinPlayer(joined);
        else return false;
    }

    /**
     * start the game: start the turn of the first player
     * @return true if success, false instead
     */
    @Override
    public boolean startGame() throws NotHisTurnException, TurnStartedException, EmptyDeckException, LorenzoMovesException, WrongPointsException, IllegalMovesException {
        if(this.turn.playerInGame() < 2 || gameOnAir) return false;
        turn.getInkwellPlayer().startHisTurn();
        gameOnAir = true;
        return true;
    }

    /**
     * discard a develop card from the dev setup
     *
     * @param color the color of discarded cards in dev setup
     */
    @Override
    public void discardDevCard(ColorDevCard color) {

    }
}
