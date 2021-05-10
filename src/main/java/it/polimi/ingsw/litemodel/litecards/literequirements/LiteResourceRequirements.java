package it.polimi.ingsw.litemodel.litecards.literequirements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LiteResourceRequirements extends LiteRequirements{

    private LiteRequisite resource;

    @JsonCreator
    public LiteResourceRequirements(@JsonProperty("resource") LiteRequisite resource) {
        this.resource = resource;
    }

    public LiteRequisite getResource() {
        return resource;
    }
}
