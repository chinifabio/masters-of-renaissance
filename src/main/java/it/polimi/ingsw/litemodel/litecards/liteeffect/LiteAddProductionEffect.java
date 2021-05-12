package it.polimi.ingsw.litemodel.litecards.liteeffect;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.litewarehouse.LiteProduction;

public class LiteAddProductionEffect extends LiteEffect{

    private final LiteProduction production;

    @JsonCreator
    public LiteAddProductionEffect(@JsonProperty("prod") LiteProduction production) {
        this.production = production;
    }

    public LiteProduction getProduction() {
        return production;
    }
}
