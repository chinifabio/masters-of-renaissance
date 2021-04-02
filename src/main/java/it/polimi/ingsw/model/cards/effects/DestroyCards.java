package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.model.cards.ColorDevCard;

public class DestroyCards implements Effect {
    /**
     * This method is used only during single player matches: it discard two cards, starting from level 1 to level 3, from the devSetup.
     * @param color of the cards that are going to get discarded.
     */
    void DestroyCards(ColorDevCard color){
    }

    @Override
    public void use() {

    }
}
