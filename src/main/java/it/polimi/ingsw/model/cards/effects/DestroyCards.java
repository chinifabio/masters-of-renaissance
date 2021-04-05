package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.player.Player;

/**
 * This class is a part of the strategy pattern, it implements the interface Effect.
 */
public class DestroyCards implements Effect {

    /**
     * This is the constructor of the class. It needs a ColoDevCard that will be destroyed.
     * @param c color of the card that will be destroyed.
     */
    public DestroyCards(ColorDevCard c) {
        this.c = c;
    }

    /**
     * This attribute saves the color of the card that will be destroyed.
     */
    private ColorDevCard c;

    /**
     * This method is used only during single player matches: it discard two cards of the same color, starting from level 1 to 3, from the devSetup.
     * @param p
     */
    @Override
    public void use(Player p) {
         //deve scartare due carte di un colore
    }
}
