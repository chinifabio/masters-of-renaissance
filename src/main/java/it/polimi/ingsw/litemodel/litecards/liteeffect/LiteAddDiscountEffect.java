package it.polimi.ingsw.litemodel.litecards.liteeffect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.view.cli.printer.effectprint.AddDiscountPrinter;
import it.polimi.ingsw.view.cli.printer.effectprint.EffectPrinter;

public class LiteAddDiscountEffect extends LiteEffect{

    private final ResourceType resource;

    @JsonIgnore
    private final AddDiscountPrinter printer;

    @JsonCreator
    public LiteAddDiscountEffect(@JsonProperty("resource") ResourceType resource) {
        this.resource = resource;
        this.printer = new AddDiscountPrinter(this.resource);
    }

    public ResourceType getResource() {
        return resource;
    }

    @Override
    public EffectPrinter getPrinter() {
        return this.printer;
    }

}
