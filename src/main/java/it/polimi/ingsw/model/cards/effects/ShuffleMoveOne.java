package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.model.player.Player;

public class ShuffleMoveOne implements Effect {

    /**
     *
     */
    @Override
    public void use(Player p) {
        p.moveLorenzo();
        // mischiare soloactiontoken
    }
}
