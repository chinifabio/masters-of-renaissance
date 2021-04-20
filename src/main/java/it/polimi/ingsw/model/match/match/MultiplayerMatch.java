package it.polimi.ingsw.model.match.match;

import it.polimi.ingsw.TextColors;
import it.polimi.ingsw.model.exceptions.PlayerStateException;

public class MultiplayerMatch extends Match{
    /**
     * build a multiplayer match.
     * A multiplayer match has a game size of 4 players
     * and require at least 2 player to be started
     */
    public MultiplayerMatch() {
        super(4, 2);
    }


    /**
     * This method starts the end game logic
     */
    @Override
    public void startEndGameLogic() {

        // ----- END GAME ZONE -----
        this.turn.endGame();
        try {
            this.turn.getCurPlayer().endThisTurn();
        } catch (PlayerStateException e) {
            // todo lancia un eccezione al model
        }
        // -------------------------

    }

    /**
     * This method is used to calculate and notify the winner of the match.
     * On each player is call the method to calculate the points obtained and the higher one wins
     */
    public void winnerCalculator(){
        System.out.println("THE WINNER IS... " + TextColors.colorText(TextColors.RED_BACKGROUND_BRIGHT, "ancora da implementare la logica :("));
        // todo logica di vittoria
    }
}
