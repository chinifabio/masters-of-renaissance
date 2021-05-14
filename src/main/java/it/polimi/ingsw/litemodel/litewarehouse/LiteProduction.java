package it.polimi.ingsw.litemodel.litewarehouse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.LiteResource;
import it.polimi.ingsw.litemodel.LiteResource;

import java.util.ArrayList;
import java.util.List;

public class LiteProduction {

    private String type;

    private final List<LiteResource> required = new ArrayList<>();

    private final List<LiteResource> output = new ArrayList<>();

    private final List<LiteResource> added = new ArrayList<>();

    @JsonCreator
    public LiteProduction(@JsonProperty("required") List<LiteResource> required, @JsonProperty("added") List<LiteResource> added, @JsonProperty("output") List<LiteResource> output) {
        this.required.addAll(required);
        this.output.addAll(output);
        this.added.addAll(added == null ? new ArrayList<>() : added);
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

    public List<LiteResource> getAdded() {
        return added;
    }
}
