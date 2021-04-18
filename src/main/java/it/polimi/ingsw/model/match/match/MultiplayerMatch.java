package it.polimi.ingsw.model.match.match;

import it.polimi.ingsw.model.cards.ColorDevCard;

public class MultiplayerMatch extends Match{
    /**
     * build a multiplayer match
     */
    public MultiplayerMatch() {
        super(4, 2);
    }

    /**
     * discard a develop card from the dev setup
     *
     * @param color the color of discarded cards in dev setup
     */
    @Override
    public void discardDevCard(ColorDevCard color) {
        //todo throw GameModeException
    }

    /**
     * This method shuffle the solo token deck;
     */
    @Override
    public void shuffleSoloToken() {
        //todo throw GameModeException
    }
}
