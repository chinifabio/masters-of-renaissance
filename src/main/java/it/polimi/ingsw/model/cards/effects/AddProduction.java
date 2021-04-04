package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.model.player.Player;

public class AddProduction implements Effect{

    /**
     *
     */
    @Override
    public void use(Player p) {
        p.addProduction();
    }
}
