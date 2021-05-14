package it.polimi.ingsw.litemodel.litecards.liteeffect;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.litewarehouse.LiteProduction;
import it.polimi.ingsw.view.cli.printer.effectprint.AddDiscountPrinter;
import it.polimi.ingsw.view.cli.printer.effectprint.AddExtraProductionPrinter;
import it.polimi.ingsw.view.cli.printer.effectprint.EffectPrinter;

public class LiteAddProductionEffect extends LiteEffect{

    private final LiteProduction production;

    private final AddExtraProductionPrinter printer;

    @JsonCreator
    public LiteAddProductionEffect(@JsonProperty("prod") LiteProduction production) {
        this.production = production;
        this.printer = new AddExtraProductionPrinter(this.production);
    }

    public LiteProduction getProduction() {
        return production;
    }

    @Override
    public EffectPrinter getPrinter() {
        return printer;
    }
}
