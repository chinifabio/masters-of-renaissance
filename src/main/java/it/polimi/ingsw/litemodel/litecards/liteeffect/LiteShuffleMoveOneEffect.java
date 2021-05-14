package it.polimi.ingsw.litemodel.litecards.liteeffect;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.polimi.ingsw.view.cli.printer.effectprint.EffectPrinter;

public class LiteShuffleMoveOneEffect extends LiteEffect {
    /**
     * This method is the constructor of the class
     */
    @JsonCreator
    public LiteShuffleMoveOneEffect() {}

    @Override
    public EffectPrinter getPrinter() {
        return null;
    }
}
