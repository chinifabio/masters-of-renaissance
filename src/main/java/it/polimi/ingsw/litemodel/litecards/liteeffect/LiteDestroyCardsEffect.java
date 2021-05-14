package it.polimi.ingsw.litemodel.litecards.liteeffect;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.cards.ColorDevCard;
import it.polimi.ingsw.view.cli.printer.effectprint.EffectPrinter;
import it.polimi.ingsw.view.cli.printer.effectprint.DestroyCardsPrinter;

public class LiteDestroyCardsEffect extends LiteEffect{

    private final ColorDevCard color;

    @JsonIgnore
    private final DestroyCardsPrinter printer;

    @JsonCreator
    public LiteDestroyCardsEffect(@JsonProperty("color") ColorDevCard c) {
        this.color = c;
        this.printer = new DestroyCardsPrinter();
    }

    public ColorDevCard getColor() {
        return color;
    }

    @Override
    public EffectPrinter getPrinter() {
        return null;
    }
}
