package it.polimi.ingsw.model.match;

import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.effects.CardReaction;

/**
 * This interface contains the method to react to a solo action token effect
 */
public interface SoloTokenReaction extends CardReaction {

    /**
     * This method shuffle the solo action token
     */
    void shuffleToken();

    /**
     * This method discard two card of the color passed in the dev setup
     * @param color color of the dev card to discard
     */
    void discardDevCard(ColorDevCard color);

    /**
     * This method move lorenzo by a certaian amount passed as parameter
     * @param i the amount of cells to move Lorenzo
     */
    void moveLorenzo(int i);
}
