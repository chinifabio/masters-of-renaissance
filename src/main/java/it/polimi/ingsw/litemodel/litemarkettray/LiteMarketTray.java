package it.polimi.ingsw.litemodel.litemarkettray;

public class LiteMarketTray {

    private final LiteMarble[][] marbles;

    private final LiteMarble slideMarble;

    public LiteMarketTray(LiteMarble[][] marbles, LiteMarble slideMarble) {
        this.marbles = marbles;
        this.slideMarble = slideMarble;
    }

    public LiteMarble[][] getMarbles() {
        return marbles;
    }

    public LiteMarble getSlideMarble() {
        return slideMarble;
    }
}
