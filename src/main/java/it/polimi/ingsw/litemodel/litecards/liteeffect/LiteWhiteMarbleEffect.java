package it.polimi.ingsw.litemodel.litecards.liteeffect;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.litemarkettray.LiteMarble;
import it.polimi.ingsw.view.cli.printer.effectprint.AddDiscountPrinter;
import it.polimi.ingsw.view.cli.printer.effectprint.EffectPrinter;
import it.polimi.ingsw.view.cli.printer.effectprint.WhiteMarblePrinter;

public class LiteWhiteMarbleEffect extends LiteEffect{

    private final LiteMarble marble;

    @JsonIgnore
    private final WhiteMarblePrinter printer;

    @JsonCreator
    public LiteWhiteMarbleEffect(@JsonProperty("marble") LiteMarble marble) {
        this.marble = marble;
        this.printer = new WhiteMarblePrinter(this.marble);

    }

    public LiteMarble getMarble() {
        return marble;
    }

    @Override
    public EffectPrinter getPrinter() {
        return printer;
    }
}
