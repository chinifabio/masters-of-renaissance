package it.polimi.ingsw.litemodel.litewarehouse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.LiteResource;

import java.util.List;

public class LiteProduction {

    @JsonIgnore
    private String type;

    private final List<LiteResource> required;

    private final List<LiteResource> output;

    @JsonCreator
    public LiteProduction(@JsonProperty("required") List<LiteResource> required, @JsonProperty("output") List<LiteResource> output) {
        this.required = required;
        this.output = output;
    }

    @JsonIgnore
    public String getType() {
        return type;
    }

    public List<LiteResource> getRequired() {
        return required;
    }

    public List<LiteResource> getOutput() {
        return output;
    }
}
