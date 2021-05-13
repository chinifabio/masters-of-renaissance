package it.polimi.ingsw.litemodel.litecards.liteeffect;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.view.cli.printer.effectprint.EffectPrinter;

public class LiteMoveTwoEffect extends LiteEffect {

    @JsonCreator
    public LiteMoveTwoEffect() {}

    @Override
    public EffectPrinter getPrinter() {
        return null;
    }
}
