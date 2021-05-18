package it.polimi.ingsw.litemodel.litecards.liteeffect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.view.cli.CLI;

public class LiteAddDiscountEffect extends LiteEffect{

    private final ResourceType resource;

    @JsonCreator
    public LiteAddDiscountEffect(@JsonProperty("resource") ResourceType resource) {
        this.resource = resource;
    }

    public ResourceType getResource() {
        return resource;
    }


    @Override
    public void printEffect(String[][] leaderCard, int x, int y) {
        leaderCard[x + 4][y + 4] = "";
        leaderCard[x + 4][y + 5] = "-1";
        leaderCard[x + 4][y + 6] = CLI.colorResource.get(resource);

    }
}
