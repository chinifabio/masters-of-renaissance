package it.polimi.ingsw.litemodel.litewarehouse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.litemodel.litecards.literequirements.LiteRequisite;

import java.util.List;

public class LiteProduction {

    private String type;

    private List<LiteRequisite> required;

    private List<LiteRequisite> output;

    @JsonCreator
    public LiteProduction(@JsonProperty("required") List<LiteRequisite> required, @JsonProperty("output") List<LiteRequisite> output) {
        this.required = required;
        this.output = output;
    }

    @JsonIgnore
    public String getType() {
        return type;
    }

    public List<LiteRequisite> getRequired() {
        return required;
    }

    public List<LiteRequisite> getOutput() {
        return output;
    }
}
