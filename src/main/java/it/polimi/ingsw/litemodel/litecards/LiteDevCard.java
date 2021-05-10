package it.polimi.ingsw.litemodel.litecards;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.litemodel.litecards.liteeffect.LiteEffect;
import it.polimi.ingsw.litemodel.litecards.literequirements.LiteRequisite;

import java.util.List;

public class LiteDevCard extends LiteCard {
    private final int victoryPoint;

    private final LevelDevCard level;

    private final ColorDevCard color;

    private final List<LiteRequisite> cost;

    public LiteDevCard(@JsonProperty("cardID") String cardID, @JsonProperty("effect") LiteEffect effect, int victoryPoint, LevelDevCard level, ColorDevCard color, List<LiteRequisite> cost) {
        super(cardID, effect);
        this.victoryPoint = victoryPoint;
        this.level = level;
        this.color = color;
        this.cost = cost;
    }

    public int getVictoryPoint() {
        return victoryPoint;
    }

    public LevelDevCard getLevel() {
        return level;
    }

    public ColorDevCard getColor() {
        return color;
    }

    public List<LiteRequisite> getCost() {
        return cost;
    }
}
