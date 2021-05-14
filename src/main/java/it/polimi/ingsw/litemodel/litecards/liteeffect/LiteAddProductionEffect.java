package it.polimi.ingsw.litemodel.litecards.liteeffect;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.litewarehouse.LiteProduction;
import it.polimi.ingsw.view.cli.printer.effectprint.AddExtraProductionPrinter;
import it.polimi.ingsw.view.cli.printer.effectprint.EffectPrinter;

public class LiteAddProductionEffect extends LiteEffect{

    private final LiteProduction prod;

    @JsonIgnore
    private final AddExtraProductionPrinter printer;

    @JsonCreator
    public LiteAddProductionEffect(@JsonProperty("prod") LiteProduction prod) {
        this.prod = prod;
        this.printer = new AddExtraProductionPrinter(this.prod);
    }

    public LiteProduction getProd() {
        return prod;
    }

    @Override
    public EffectPrinter getPrinter() {
        return printer;
    }

}
