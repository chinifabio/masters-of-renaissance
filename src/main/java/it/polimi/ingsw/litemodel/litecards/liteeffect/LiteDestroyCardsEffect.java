package it.polimi.ingsw.litemodel.litecards.liteeffect;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.cards.ColorDevCard;

public class LiteDestroyCardsEffect extends LiteEffect{

    private final ColorDevCard color;

    @JsonCreator
    public LiteDestroyCardsEffect(@JsonProperty("color") ColorDevCard c) {
        this.color = c;
    }

    public ColorDevCard getColor() {
        return color;
    }

    @Override
    public void printEffect(String[][] leaderCard, int x, int y) {

    }
}
