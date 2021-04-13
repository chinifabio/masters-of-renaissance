package it.polimi.ingsw.model.cards.effects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.player.PlayerReactEffect;


/**
 * This class is a part of the strategy pattern, it implements the interface Effect.
 */
public class DestroyCardsEffect extends Effect {
    /**
     * This attribute saves the color of the card that will be destroyed.
     */
    private ColorDevCard color;

    public DestroyCardsEffect() {
    }

    /**
     * This is the constructor of the class. It needs a ColoDevCard that will be destroyed.
     * @param c color of the card that will be destroyed.
     */
    public DestroyCardsEffect(ColorDevCard c) {
        this.color = c;
    }



    /**
     * This method is used only during single player matches: it discard two cards of the same color, starting from level 1 to 3, from the devSetup.
     * @param p the player
     */
    public void use(PlayerReactEffect p) {
        System.out.println("Discarding cards: " + color);
         p.discardDevCard(this.color);
    }
}
