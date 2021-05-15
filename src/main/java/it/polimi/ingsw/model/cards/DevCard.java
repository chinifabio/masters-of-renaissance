package it.polimi.ingsw.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.litemodel.litecards.LiteDevCard;
import it.polimi.ingsw.model.cards.effects.Effect;
import it.polimi.ingsw.model.requisite.Requisite;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is the representation of the DevCard
 */
public class DevCard extends Card{

    /**
     * This is the constructor of the class.
     * @param cardID requested from the upper class.
     * @param effect requested from the upper class.
     * @param victoryPoint that the card will give at the end of the game.
     * @param level of the card.
     * @param color of the card.
     */
    @JsonCreator
    public DevCard(@JsonProperty("cardID") String cardID, @JsonProperty("effect") Effect effect, @JsonProperty("victoryPoint") int victoryPoint, @JsonProperty("level") LevelDevCard level, @JsonProperty("color") ColorDevCard color, @JsonProperty("cost") List<Requisite> cost){
        super(cardID, effect);
        this.victoryPoint = victoryPoint;
        this.level = level;
        this.color = color;
        this.cost.addAll(cost);
    }

    /**
     * This attribute is the victory point of the card.
     */
    private final int victoryPoint;

    /**
     * This attribute uses the enumeration LevelDeckCard to represent the level of the DevCard.
     */
    private final LevelDevCard level;

    /**
     * This attribute uses the enumeration ColorDeckCard to represent the color of the DevCard.
     */
    private final ColorDevCard color;

    /**
     * This attribute is the list of Requisite that the DevCard requires to be bought.
     */
    private final List<Requisite> cost = new ArrayList<>();

    /**
     * This method is used to get the value of victoryPoint of the Card.
     * @return the victoryPoint of the DevCard.
     */
    public int getVictoryPoint() {
        return victoryPoint;
    }

    /**
     * This method is used to get the value of level of the Card.
     * @return the level of the DevCard.
     */
    public LevelDevCard getLevel() {
        return level;
    }

    /**
     * This method is used to get the color of the Card.
     * @return the color of the DevCard.
     */
    public ColorDevCard getColor() {
        return color;
    }

    /**
     * This method is used to get the List of Requisite which are the requirements to buy the DevCard.
     * @return the cost of the DevCard.
     */
    public List<Requisite> getCost() {
        return cost;
    }

    /**
     * Create a lite version of the class and serialize it in json
     *
     * @return the json representation of the lite version of the class
     */
    @Override
    public LiteDevCard liteVersion() {
        return new LiteDevCard(this.cardID, this.effect.liteVersion(), this.victoryPoint, this.level, this.color,
                this.cost.stream().map(Requisite::liteVersion).collect(Collectors.toList()));
    }
}
