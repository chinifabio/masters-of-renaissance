package it.polimi.ingsw.litemodel.litecards.liteeffect;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.view.cli.CLI;

public class LiteAddDepotEffect extends LiteEffect {

    private final ResourceType resource;

    @JsonCreator
    public LiteAddDepotEffect(@JsonProperty("resource") ResourceType resource) {
        this.resource = resource;
    }

    public ResourceType getResource() {
        return resource;
    }


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
