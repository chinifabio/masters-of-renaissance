package it.polimi.ingsw.model.match.match;

import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.exceptions.game.GameTypeException;
import it.polimi.ingsw.model.player.Lorenzo;
import it.polimi.ingsw.model.player.Player;

public class SingleplayerMatch extends Match{

    public SingleplayerMatch() {
        super(2);
    }

    @Override
    public boolean playerJoin(Player joined) {
        int num = turn.playerInGame();

        if(num > this.gameSize || gameOnAir) return false;
        else if (num == 1 && joined.getNickname().equals(Lorenzo.lorenzoNickname)) return turn.joinPlayer(joined);
        else if (num == 0 && !joined.getNickname().equals(Lorenzo.lorenzoNickname)) return turn.joinPlayer(joined);
        else return false;
    }

    /**
     * start the game: start the turn of the first player and set the gameOnAir as true
     * @return true if success, false instead
     */
    @Override
    public boolean startGame() {
        if (this.turn.playerInGame() != 2 || gameOnAir) return false;
        this.turn.getCurPlayer().startHisTurn();
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
