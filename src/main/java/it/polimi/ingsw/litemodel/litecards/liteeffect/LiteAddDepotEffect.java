package it.polimi.ingsw.litemodel.litecards.liteeffect;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.model.resource.ResourceType;
import it.polimi.ingsw.view.cli.printer.effectprint.AddDepotPrinter;
import it.polimi.ingsw.view.cli.printer.effectprint.EffectPrinter;

public class LiteAddDepotEffect extends LiteEffect {

    private final ResourceType resource;

    @JsonIgnore
    private final AddDepotPrinter printer;

    @JsonCreator
    public LiteAddDepotEffect(@JsonProperty("resource") ResourceType resource) {
        this.resource = resource;
        this.printer = new AddDepotPrinter(this.resource);
    }

    public ResourceType getResource() {
        return resource;
    }

    @Override
    public EffectPrinter getPrinter() {
        return printer;
    }
}
