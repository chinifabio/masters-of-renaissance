package it.polimi.ingsw.litemodel.litewarehouse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.litecards.literequirements.LiteResourceTypeResquisite;

import java.util.List;

public class LiteProduction {

    @JsonIgnore
    private String type;

    private final List<LiteResourceTypeResquisite> required;

    private final List<LiteResourceTypeResquisite> output;

    @JsonCreator
    public LiteProduction(@JsonProperty("required") List<LiteResourceTypeResquisite> required, @JsonProperty("output") List<LiteResourceTypeResquisite> output) {
        this.required = required;
        this.output = output;
    }

    @JsonIgnore
    public String getType() {
        return type;
    }

    public List<LiteResourceTypeResquisite> getRequired() {
        return required;
    }

    public List<LiteResourceTypeResquisite> getOutput() {
        return output;
    }
}
