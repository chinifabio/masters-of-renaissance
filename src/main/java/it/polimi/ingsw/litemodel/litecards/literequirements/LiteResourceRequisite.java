package it.polimi.ingsw.litemodel.litecards.literequirements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.LiteResource;
import it.polimi.ingsw.view.cli.CLI;

/**
 * This class represents the requisite to activate a DevCard
 */
public class LiteResourceRequisite extends LiteRequisite {

    /**
     * This attribute is the required LiteResource for a DevCard
     */
    private final LiteResource resource;

    /**
     * This is the constructor of the class:
     * @param resource required
     */
    @JsonCreator
    public LiteResourceRequisite(@JsonProperty("resource") LiteResource resource) {
        this.resource = resource;
    }

    /**
     * This method returns the required resource
     * @return the LiteResource
     */
    public LiteResource getResource() {
        return resource;
    }

    /**
     * This method is used to print the requisite in CLI
     * @param leaderCard to print
     * @param x position
     * @param y position
     */
    @Override
    public void printRequisite(String[][] leaderCard, int x, int y) {
        leaderCard[x + 1][y + 2] = String.valueOf(resource.getAmount());
        leaderCard[x + 1][y + 3] = CLI.colorResource.get(resource.getType());
    }
}
