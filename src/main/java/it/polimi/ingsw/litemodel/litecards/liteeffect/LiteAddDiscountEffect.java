package it.polimi.ingsw.litemodel.litecards.liteeffect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.view.cli.printer.effectprint.AddDiscountPrinter;
import it.polimi.ingsw.view.cli.printer.effectprint.EffectPrinter;

public class LiteAddDiscountEffect extends LiteEffect{

    private final ResourceType resourceType;

    @JsonIgnore
    private final AddDiscountPrinter printer;

    @JsonCreator
    public LiteAddDiscountEffect(@JsonProperty("resource") ResourceType resourceType) {
        this.resourceType = resourceType;
        this.printer = new AddDiscountPrinter(this.resourceType);
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    @Override
    public EffectPrinter getPrinter() {
        return this.printer;
    }
}
