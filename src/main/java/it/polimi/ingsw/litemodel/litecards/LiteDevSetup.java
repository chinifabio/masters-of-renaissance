package it.polimi.ingsw.litemodel.litecards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.DevSetup;
import it.polimi.ingsw.model.cards.LevelDevCard;

import java.util.ArrayList;
import java.util.List;

public class LiteDevSetup {

    private static final int MAX_VERT = 3;

    private  static final int MAX_HOR = 4;

    private final List<LiteDevCard> devSetup = new ArrayList<>();

    public LiteDevSetup(@JsonProperty("cards") List<LiteDevCard> cards) {
        this.devSetup.addAll(cards);
    }

    public List<LiteDevCard> getDevSetup() {
        return devSetup;
    }

    @JsonIgnore
    public LiteDevCard[][] getDevSetup(int z) {
        LiteDevCard[][] res= new LiteDevCard[MAX_HOR][MAX_VERT];
        for (int i = 0; i < this.devSetup.size(); i++) {
            res[i/MAX_HOR][i%MAX_VERT] = this.devSetup.get(i);
        }
        return res;
    }

    @JsonIgnore
    public void setDevCard(LiteDevCard card) {
    }
    /*
    public LiteDevCard getDevCard(ColorDevCard color, LevelDevCard level){

    }



    public void setDevCard(LiteDevCard card){
        devSetup[card.getLevel().getDevSetupIndex()][card.getColor().getDevSetupIndex()] = card;
    }
    */

}
