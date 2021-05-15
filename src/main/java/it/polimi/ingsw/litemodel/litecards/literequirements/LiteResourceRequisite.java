package it.polimi.ingsw.litemodel.litecards.literequirements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.LiteResource;
import it.polimi.ingsw.view.cli.CLI;

public class LiteResourceRequisite extends LiteRequisite {

    private final LiteResource resource;

    @JsonCreator
    public LiteResourceRequisite(@JsonProperty("resource") LiteResource resource) {
        this.resource = resource;
    }

    public LiteResource getResource() {
        return resource;
    }

    @Override
    public void printRequisite(String[][] leaderCard, int x, int y) {
        leaderCard[x + 1][y + 2] = String.valueOf(resource.getAmount());
        leaderCard[x + 1][y + 3] = CLI.colorResource.get(resource.getType());
    }
}
