package it.polimi.ingsw.litemodel.litecards.literequirements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LiteResourceRequirements extends LiteRequirementsType {

    private LiteResourceTypeResquisite resource;

    @JsonCreator
    public LiteResourceRequirements(@JsonProperty("resource") LiteResourceTypeResquisite resource) {
        this.resource = resource;
    }

    public LiteResourceTypeResquisite getResource() {
        return resource;
    }
}
