package it.polimi.ingsw.litemodel.litecards;

import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;

public class LiteDevSetup {

    private static final int MAX_VERT = 3;

    private  static final int MAX_HOR = 4;

    private final LiteDevCard[][] devSetup = new LiteDevCard[MAX_VERT][MAX_HOR];

    public LiteDevCard getDevCard(ColorDevCard color, LevelDevCard level){
        return devSetup[level.getDevSetupIndex()][color.getDevSetupIndex()];
    }

    public LiteDevCard[][] getDevSetup() {
        return devSetup;
    }

    public void setDevCard(LiteDevCard card){
        devSetup[card.getLevel().getDevSetupIndex()][card.getColor().getDevSetupIndex()] = card;
    }


}
