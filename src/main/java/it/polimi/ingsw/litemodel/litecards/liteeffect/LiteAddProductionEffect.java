package it.polimi.ingsw.litemodel.litecards.liteeffect;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.LiteResource;
import it.polimi.ingsw.litemodel.litewarehouse.LiteProduction;
import it.polimi.ingsw.view.cli.CLI;

public class LiteAddProductionEffect extends LiteEffect{

    private final LiteProduction prod;

    @JsonCreator
    public LiteAddProductionEffect(@JsonProperty("prod") LiteProduction prod) {
        this.prod = prod;
    }

    public LiteProduction getProd() {
        return prod;
    }

    @Override
    public void printEffect(String[][] leaderCard, int x, int y) {

        int initcol;
        if (this.prod.getRequired().size() == 1){
            initcol = y + 5;
        } else if (this.prod.getRequired().size() == 2){
            initcol = y + 3;
        } else {
            initcol = y + 3;
        }
        for (LiteResource type : this.prod.getRequired()){
            leaderCard[x + 3][initcol] = (String.valueOf(type.getAmount()));
            initcol++;
            leaderCard[x + 3][initcol] = (CLI.colorResource.get(type.getType()));
            initcol++;
            initcol++;
        }
        leaderCard[x + 4][y+5] = "↓";

        if (this.prod.getOutput().size() == 1){
            initcol = y + 5;
        } else if (this.prod.getOutput().size() == 2){
            initcol = y + 3;
        } else {
            initcol = y + 2;
        }

        for (LiteResource type : this.prod.getOutput()){
            leaderCard[x + 5][initcol] = (String.valueOf(type.getAmount()));
            initcol++;
            leaderCard[x + 5][initcol] = (CLI.colorResource.get(type.getType()));
            initcol++;
            initcol++;
        }
    }
}
