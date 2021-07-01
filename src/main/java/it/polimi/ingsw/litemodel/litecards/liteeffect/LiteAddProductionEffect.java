package it.polimi.ingsw.litemodel.litecards.liteeffect;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.LiteResource;
import it.polimi.ingsw.litemodel.litewarehouse.LiteProduction;
import it.polimi.ingsw.view.cli.CLI;

/**
 * This class represents on of the effect of the cards
 */
public class LiteAddProductionEffect extends LiteEffect{

    /**
     * This attribute is the added production
     */
    private final LiteProduction prod;

    /**
     * This is the constructor of the class:
     * @param prod LiteProduction to add
     */
    @JsonCreator
    public LiteAddProductionEffect(@JsonProperty("prod") LiteProduction prod) {
        this.prod = prod;
    }

    /**
     * This method returns the production
     * @return the LiteProduction
     */
    public LiteProduction getProd() {
        return prod;
    }

    /**
     * This method is used to print the effect in cli
     * @param leaderCard to print
     * @param x horizontal position
     * @param y vertical position
     */
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
