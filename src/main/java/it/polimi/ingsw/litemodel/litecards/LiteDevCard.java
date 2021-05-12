package it.polimi.ingsw.litemodel.litecards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.litecards.literequirements.LiteRequirementsType;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.litemodel.litecards.liteeffect.LiteEffect;

import java.util.List;

public class LiteDevCard extends LiteCard {


    private final int victoryPoint;

    private final LevelDevCard level;

    private final ColorDevCard color;

    private final List<LiteRequirementsType> cost;

    @JsonCreator
    public LiteDevCard(@JsonProperty("cardID") String cardID, @JsonProperty("effect") LiteEffect effect,@JsonProperty("victoryPoint") int victoryPoint,@JsonProperty("level") LevelDevCard level,@JsonProperty("color") ColorDevCard color,@JsonProperty("cost") List<LiteRequirementsType> cost) {
        super(cardID, effect);
        this.victoryPoint = victoryPoint;
        this.level = level;
        this.color = color;
        this.cost = cost;
    }

    public String getId(){
        return super.getCardID();
    }

    @JsonIgnore
    public LiteEffect getEffect(){
        return super.getEffect();
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

    public List<LiteRequirementsType> getCost() {
        return cost;
    }
}
