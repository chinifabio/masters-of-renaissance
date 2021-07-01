package it.polimi.ingsw.litemodel.litecards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.litecards.literequirements.LiteRequisite;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.model.cards.LevelDevCard;
import it.polimi.ingsw.litemodel.litecards.liteeffect.LiteEffect;

import java.util.List;

/**
 * This class represents the lite version of a DevCard
 */
public class LiteDevCard extends LiteCard {

    /**
     * This attribute is the victory point of the DevCard
     */
    private final int victoryPoint;

    /**
     * This attribute is the level of the DevCard
     */
    private final LevelDevCard level;

    /**
     * This attribute is the color of the DevCard
     */
    private final ColorDevCard color;

    /**
     * This attribute is the list of requisite of the DevCard
     */
    private final List<LiteRequisite> cost;

    /**
     * This is the constructor of the class:
     * @param cardID String ID of the card
     * @param effect LiteEffect of the card
     * @param victoryPoint number of victory points
     * @param level LevelDevCard of the card
     * @param color ColorDevCard of the card
     * @param cost list of LiteRequisite of the card
     */
    @JsonCreator
    public LiteDevCard(@JsonProperty("cardID") String cardID, @JsonProperty("effect") LiteEffect effect,@JsonProperty("victoryPoint") int victoryPoint,@JsonProperty("level") LevelDevCard level,@JsonProperty("color") ColorDevCard color,@JsonProperty("cost") List<LiteRequisite> cost) {
        super(cardID, effect);
        this.victoryPoint = victoryPoint;
        this.level = level;
        this.color = color;
        this.cost = cost;
    }

    /**
     * This method returns the ID of the DevCard
     * @return the String ID
     */
    public String getId(){
        return super.getCardID();
    }

    /**
     * This method returns the effect of the DevCard
     * @return the LiteEffect of the card
     */
    @JsonIgnore
    public LiteEffect getEffect(){
        return super.getEffect();
    }

    /**
     * This method returns the victory point of the DevCard
     * @return the victory point
     */
    public int getVictoryPoint() {
        return victoryPoint;
    }

    /**
     * This method returns the level of the DevCard
     * @return the LevelDevCard
     */
    public LevelDevCard getLevel() {
        return level;
    }

    /**
     * This method returns the color of the DevCard
     * @return the ColorDevCard
     */
    public ColorDevCard getColor() {
        return color;
    }

    /**
     * This method returns the cost of the DevCard
     * @return the list of LiteRequisite of the DevCard
     */
    public List<LiteRequisite> getCost() {
        return cost;
    }
}
