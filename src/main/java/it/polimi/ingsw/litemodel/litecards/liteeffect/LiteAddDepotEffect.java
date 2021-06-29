package it.polimi.ingsw.litemodel.litecards.liteeffect;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.view.cli.CLI;

/**
 * This class represents on of the effect of the cards
 */
public class LiteAddDepotEffect extends LiteEffect {

    /**
     * This attribute is the resource type of the special depot
     */
    private final ResourceType resource;

    /**
     * This is the constructor of the class:
     * @param resource of the special depot
     */
    @JsonCreator
    public LiteAddDepotEffect(@JsonProperty("resource") ResourceType resource) {
        this.resource = resource;
    }

    /**
     * This method returns the resource type of the depot
     * @return the ResourceType of the depot
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
        int initcol = y + 1;
        for (int i = 0; i < 2; i++){
            leaderCard[x + 4][initcol] = "[";
            leaderCard[x + 4][initcol+1] = " ";
            leaderCard[x + 4][initcol+2] = CLI.colorResource.get(resource);
            leaderCard[x + 4][initcol+3] = " ";
            leaderCard[x + 4][initcol+4] = "]";
            initcol = initcol+5;
        }
    }
}
