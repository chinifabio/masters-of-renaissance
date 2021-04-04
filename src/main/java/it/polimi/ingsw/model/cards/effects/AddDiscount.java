package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.model.player.Player;

public class AddDiscount implements Effect{

    /**
     *
     */
    @Override
    public void use(Player p) {
        p.addDiscount();
    }
}