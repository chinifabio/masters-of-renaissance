package it.polimi.ingsw.model.cards.effects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.exceptions.card.AlreadyInDeckException;
import it.polimi.ingsw.model.exceptions.card.EmptyDeckException;
import it.polimi.ingsw.model.exceptions.faithtrack.EndGameException;
import it.polimi.ingsw.model.match.SoloTokenReaction;


/**
 * This class is a part of the strategy pattern, it implements the interface Effect.
 */
public class DestroyCardsEffect extends Effect {
    /**
     * This attribute saves the color of the card that will be destroyed.
     */
    private ColorDevCard color;


    /**
     * This is the constructor of the class. It needs a ColoDevCard that will be destroyed.
     * @param c color of the card that will be destroyed.
     */
    @JsonCreator
    public DestroyCardsEffect(@JsonProperty("color") ColorDevCard c) {
        this.color = c;
    }



    /**
     * This method is used only during single player matches: it discard two cards of the same color, starting from level 1 to 3, from the devSetup.
     * @param p the player
     */
    public void use(CardReaction p) throws EmptyDeckException, EndGameException, AlreadyInDeckException {
        ((SoloTokenReaction) p).discardDevCard(this.color);
    }
}
