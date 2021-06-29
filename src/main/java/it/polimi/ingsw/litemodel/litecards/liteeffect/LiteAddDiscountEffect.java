package it.polimi.ingsw.litemodel.litecards.liteeffect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.view.cli.CLI;

/**
 * This class represents on of the effect of the cards
 */
public class LiteAddDiscountEffect extends LiteEffect{

    /**
     * This attribute is the resource type of the discounted resource
     */
    private final ResourceType resource;

    /**
     * This is the constructor of the class:
     * @param resource if the discounted resource
     */
    @JsonCreator
    public LiteAddDiscountEffect(@JsonProperty("resource") ResourceType resource) {
        this.resource = resource;
    }

    /**
     * This method returns the resource type of the discounted resource
     * @return the ResourceType of the discounted resource
     */
    public ResourceType getResource() {
        return resource;
    }

    /**
     * This method is used to print the effect in cli
     * @param leaderCard to print
     * @param x horizontal position
     * @param y vertical position
     */
    @Override
    public void printEffect(String[][] leaderCard, int x, int y) {
        leaderCard[x + 4][y + 4] = "";
        leaderCard[x + 4][y + 5] = "-1";
        leaderCard[x + 4][y + 6] = CLI.colorResource.get(resource);

    }
}
